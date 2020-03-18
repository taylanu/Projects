/******************************************************************************
 *
 * Copyright (c) 2019, the Perspective Authors.
 *
 * This file is part of the Perspective library, distributed under the terms of
 * the Apache License 2.0.  The full license can be found in the LICENSE file.
 *
 */

#include <perspective/arrow.h>

#include <arrow/api.h>
#include <arrow/io/memory.h>
#include <arrow/ipc/reader.h>

using namespace perspective;
using namespace ::arrow;

namespace perspective {
namespace arrow {

    t_dtype
    convert_type(const std::string& src) {
        if (src == "dictionary" || src == "utf8" || src == "binary") {
            return DTYPE_STR;
        } else if (src == "bool") {
            return DTYPE_BOOL;
        } else if (src == "int8") {
            return DTYPE_INT8;
        } else if (src == "int16") {
            return DTYPE_INT16;
        } else if (src == "int32") {
            return DTYPE_INT32;
        } else if (src == "int64") {
            return DTYPE_INT64;
        } else if (src == "float") {
            return DTYPE_FLOAT32;
        } else if (src == "double") {
            return DTYPE_FLOAT64;
        } else if (src == "timestamp") {
            return DTYPE_TIME;
        }
        std::cout << "No type valid type found for: " << src << std::endl;
        PSP_COMPLAIN_AND_ABORT("No type valid type found");
        return DTYPE_STR;
    }

    ArrowLoader::ArrowLoader() {}
    ArrowLoader::~ArrowLoader() {}

    void
    ArrowLoader::initialize(const uintptr_t ptr, const uint32_t length) {
        io::BufferReader buffer_reader(reinterpret_cast<const std::uint8_t*>(ptr), length);
        std::shared_ptr<ipc::RecordBatchFileReader> batch_reader;
        auto status = ipc::RecordBatchFileReader::Open(&buffer_reader, &batch_reader);

        if (!status.ok()) {
            PSP_COMPLAIN_AND_ABORT(status.message());
        } else {
            auto num_batches = batch_reader->num_record_batches();
            for (int i = 0; i < num_batches; ++i) {
                std::shared_ptr<RecordBatch> chunk;
                auto status = batch_reader->ReadRecordBatch(i, &chunk);
                if (!status.ok()) {
                    PSP_COMPLAIN_AND_ABORT(status.message());
                }
                m_batches.push_back(chunk);
            }

            // Get names and schema
            std::shared_ptr<Schema> schema = m_batches[0]->schema();
            std::vector<std::shared_ptr<Field>> fields = schema->fields();

            for (auto field : fields) {
                m_names.push_back(field->name());
                m_types.push_back(convert_type(field->type()->name()));
            }
        }
    }

    void
    ArrowLoader::fill_table(t_data_table& tbl, const std::string& index, std::uint32_t offset,
        std::uint32_t limit, bool is_update) {
        bool implicit_index = false;
        std::shared_ptr<Schema> schema = m_batches[0]->schema();
        std::vector<std::shared_ptr<Field>> fields = schema->fields();

        for (long unsigned int cidx = 0; cidx < m_names.size(); ++cidx) {
            auto name = m_names[cidx];
            auto type = m_types[cidx];
            auto raw_type = fields[cidx]->type()->name();

            if (name == "__INDEX__") {
                implicit_index = true;
                std::shared_ptr<t_column> pkey_col_sptr
                    = tbl.add_column_sptr("psp_pkey", type, true);
                fill_column(tbl, pkey_col_sptr, "psp_pkey", cidx, type, raw_type, is_update);
                tbl.clone_column("psp_pkey", "psp_okey");
                continue;
            } else {
                auto col = tbl.get_column(name);
                fill_column(tbl, col, name, cidx, type, raw_type, is_update);
            }
        }

        // Fill index column - recreated every time a `t_data_table` is created.
        if (!implicit_index) {
            if (index == "") {
                // Use row number as index if not explicitly provided or provided with
                // `__INDEX__`
                auto key_col = tbl.add_column("psp_pkey", DTYPE_INT32, true);
                auto okey_col = tbl.add_column("psp_okey", DTYPE_INT32, true);

                for (std::uint32_t ridx = 0; ridx < tbl.size(); ++ridx) {
                    key_col->set_nth<std::int32_t>(ridx, (ridx + offset) % limit);
                    okey_col->set_nth<std::int32_t>(ridx, (ridx + offset) % limit);
                }
            } else {
                tbl.clone_column(index, "psp_pkey");
                tbl.clone_column(index, "psp_okey");
            }
        }
    }

    template <typename T, typename V>
    void
    iter_col_copy(std::shared_ptr<t_column> dest, std::shared_ptr<::arrow::Array> src,
        const uint32_t len) {
        std::shared_ptr<T> scol = std::static_pointer_cast<T>(src);
        const typename T::value_type* vals = scol->raw_values();
        for (uint32_t i = 0; i < len; i++) {
            dest->set_nth<V>(i, static_cast<V>(vals[i]));
        }
    }

    void
    copy_array(std::shared_ptr<t_column> dest, std::shared_ptr<::arrow::Array> src,
        const uint32_t len) {
        switch (src->type()->id()) {
            case ::arrow::DictionaryType::type_id: {
                auto scol = std::static_pointer_cast<::arrow::DictionaryArray>(src);
                std::shared_ptr<::arrow::StringArray> dict
                    = std::static_pointer_cast<::arrow::StringArray>(scol->dictionary());
                const int32_t* offsets = dict->raw_value_offsets();
                const uint8_t* values = dict->value_data()->data();
                const std::uint64_t dsize = dict->length();

                t_vocab* vocab = dest->_get_vocab();
                std::string elem;

                for (std::uint64_t i = 0; i < dsize; ++i) {
                    std::int32_t bidx = offsets[i];
                    std::size_t es = offsets[i + 1] - bidx;
                    elem.assign(reinterpret_cast<const char*>(values) + bidx, es);
                    t_uindex idx = vocab->get_interned(elem);
                }
                auto indices = scol->indices();
                switch (indices->type()->id()) {
                    case ::arrow::Int8Type::type_id: {
                        iter_col_copy<::arrow::Int8Array, uint32_t>(dest, indices, len);
                    } break;
                    case ::arrow::Int16Type::type_id: {
                        iter_col_copy<::arrow::Int16Array, uint32_t>(dest, indices, len);
                    } break;
                    case ::arrow::Int32Type::type_id: {
                        auto sindices = std::static_pointer_cast<::arrow::Int32Array>(indices);
                        std::memcpy(dest->get_nth<std::int32_t>(0),
                            (void*)sindices->raw_values(), len * 4);
                    } break;
                    default:
                        PSP_COMPLAIN_AND_ABORT(indices->type());
                }
            } break;
            case ::arrow::BinaryType::type_id:
            case ::arrow::StringType::type_id: {
                std::shared_ptr<::arrow::StringArray> scol
                    = std::static_pointer_cast<::arrow::StringArray>(src);
                const int32_t* offsets = scol->raw_value_offsets();
                const uint8_t* values = scol->value_data()->data();

                std::string elem;

                for (std::uint32_t i = 0; i < len; ++i) {
                    std::int32_t bidx = offsets[i];
                    std::size_t es = offsets[i + 1] - bidx;
                    elem.assign(reinterpret_cast<const char*>(values) + bidx, es);
                    dest->set_nth(i, elem);
                }
            } break;
            case ::arrow::Int8Type::type_id: {
                auto scol = std::static_pointer_cast<::arrow::Int8Array>(src);
                std::memcpy(dest->get_nth<std::int8_t>(0), (void*)scol->raw_values(), len);
            } break;
            case ::arrow::Int16Type::type_id: {
                auto scol = std::static_pointer_cast<::arrow::Int16Array>(src);
                std::memcpy(dest->get_nth<std::int16_t>(0), (void*)scol->raw_values(), len * 2);
            } break;
            case ::arrow::Int32Type::type_id: {
                auto scol = std::static_pointer_cast<::arrow::Int32Array>(src);
                std::memcpy(dest->get_nth<std::int32_t>(0), (void*)scol->raw_values(), len * 4);
            } break;
            case ::arrow::Int64Type::type_id: {
                auto scol = std::static_pointer_cast<::arrow::Int64Array>(src);
                std::memcpy(dest->get_nth<std::int64_t>(0), (void*)scol->raw_values(), len * 8);
            } break;
            case ::arrow::TimestampType::type_id: {
                std::shared_ptr<::arrow::TimestampType> tunit
                    = std::static_pointer_cast<::arrow::TimestampType>(src->type());
                auto scol = std::static_pointer_cast<::arrow::TimestampArray>(src);
                switch (tunit->unit()) {
                    case ::arrow::TimeUnit::MILLI: {
                        std::memcpy(
                            dest->get_nth<double>(0), (void*)scol->raw_values(), len * 8);
                    } break;
                    case ::arrow::TimeUnit::NANO: {
                        const int64_t* vals = scol->raw_values();
                        for (uint32_t i = 0; i < len; i++) {
                            dest->set_nth<int64_t>(i, vals[i] / 1000000);
                        }
                    } break;
                    case ::arrow::TimeUnit::MICRO: {
                        const int64_t* vals = scol->raw_values();
                        for (uint32_t i = 0; i < len; i++) {
                            dest->set_nth<int64_t>(i, vals[i] / 1000);
                        }
                    } break;
                    case ::arrow::TimeUnit::SECOND: {
                        const int64_t* vals = scol->raw_values();
                        for (uint32_t i = 0; i < len; i++) {
                            dest->set_nth<int64_t>(i, vals[i] * 1000);
                        }
                    } break;
                }
            } break;
            case ::arrow::FloatType::type_id: {
                auto scol = std::static_pointer_cast<::arrow::FloatArray>(src);
                std::memcpy(dest->get_nth<float>(0), (void*)scol->raw_values(), len * 4);
            } break;
            case ::arrow::DoubleType::type_id: {
                auto scol = std::static_pointer_cast<::arrow::DoubleArray>(src);
                std::memcpy(dest->get_nth<double>(0), (void*)scol->raw_values(), len * 8);
            } break;
            case ::arrow::BooleanType::type_id: {
                auto scol = std::static_pointer_cast<::arrow::BooleanArray>(src);
                const uint8_t* null_bitmap = scol->values()->data();
                for (uint32_t i = 0; i < len; ++i) {
                    std::uint8_t elem = null_bitmap[i / 8];
                    bool v = elem & (1 << (i % 8));
                    dest->set_nth<bool>(i, v);
                }
            } break;
            default: {
                PSP_COMPLAIN_AND_ABORT(src->type()->name());
            }
        }
    }

    void
    ArrowLoader::fill_column(t_data_table& tbl, std::shared_ptr<t_column> col,
        const std::string& name, std::int32_t cidx, t_dtype type, std::string& raw_type,
        bool is_update) {
        uint32_t len = m_batches[0]->num_rows();
        std::shared_ptr<::arrow::Array> array = m_batches[0]->GetColumnByName(name);
        copy_array(col, array, len);

        // Fill validity bitmap
        std::int64_t null_count = array->null_count();
        if (null_count == 0) {
            col->valid_raw_fill();
        } else {
            const uint8_t* null_bitmap = array->null_bitmap_data();

            // arrow packs bools into a bitmap
            for (uint32_t i = 0; i < len; ++i) {
                std::uint8_t elem = null_bitmap[i / 8];
                bool v = elem & (1 << (i % 8));
                col->set_valid(i, v);
            }
        }
    }

    std::uint32_t
    ArrowLoader::num_rows() const {
        std::uint32_t row_count = 0;
        for (auto batch : m_batches) {
            row_count += batch->num_rows();
        }
        return row_count;
    }

    std::vector<std::string>
    ArrowLoader::names() const {
        return m_names;
    }

    std::vector<t_dtype>
    ArrowLoader::types() const {
        return m_types;
    }

} // namespace arrow
} // namespace perspective