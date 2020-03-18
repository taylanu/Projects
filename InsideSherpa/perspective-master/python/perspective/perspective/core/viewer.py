# *****************************************************************************
#
# Copyright (c) 2019, the Perspective Authors.
#
# This file is part of the Perspective library, distributed under the terms of
# the Apache License 2.0.  The full license can be found in the LICENSE file.
#
import logging
from random import random
from .validate import validate_plugin, validate_columns, validate_row_pivots, validate_column_pivots, \
    validate_aggregates, validate_sort, validate_filters, validate_plugin_config
from .viewer_traitlets import PerspectiveTraitlets
from ..table import PerspectiveManager, Table


class PerspectiveViewer(PerspectiveTraitlets):
    '''PerspectiveViewer wraps the `perspective.Table` API and exposes an API around creating views, loading data, and updating data.'''
    def __init__(self,
                 plugin='hypergrid',
                 columns=None,
                 row_pivots=None,
                 column_pivots=None,
                 aggregates=None,
                 sort=None,
                 filters=None,
                 plugin_config=None,
                 dark=False):
        '''Initialize an instance of `PerspectiveViewer` with the given viewer configuration.

        Do not pass a `Table` or data into the constructor — use the `load()` method to provide the viewer with data.

        Args:
            plugin (str) : the grid or visualization that will be displayed on render. Defaults to "hypergrid".
            columns (list[str]) : column names that will be actively displayed to the user. Columns not in this list will exist on the viewer sidebar
                but not in the visualization. If not specified, all colummns present in the dataset will be shown.
            row_pivots (list[str]) : columns that will be used to group data together by row.
            column_pivots (list[str]) : columns that will be used to group data together by unique column value.
            aggregates (dict{str:str}) : a mapping of column names to aggregate types, specifying how data should be aggregated when pivots are applied.
            sort (list[list[str]]) : a list of sort specifications to apply to the view.
                Sort specifications are lists of two elements: a string column name to sort by, and a string sort direction ("asc", "desc").
            filters (list[list[str]]) : a list of filter configurations to apply to the view.
                Filter configurations are lists of three elements: a string column name, a string filter operator ("<", ">", "==", "not null", etc.),
                and a value to filter by. Make sure the type of the filter value is the same as the column type, i.e. a string column should not be filtered by integer.
            plugin_config (dict) : an optional configuration containing the interaction state of a `perspective-viewer`.
            dark (bool) : enables/disables dark mode on the viewer. Defaults to `False`.

        Example:
            >>> viewer = PerspectiveViewer(aggregates={"a": "avg"}, row_pivots=["a"], sort=[["b", "desc"]], filter=[["a", ">", 1]])
        '''

        # Create an instance of `PerspectiveManager`, which receives messages from the `PerspectiveJupyterClient` on the front-end.
        self.manager = PerspectiveManager()
        self.table_name = None  # not a traitlet - only used in the python side of the viewer

        # Viewer configuration
        self.plugin = validate_plugin(plugin)
        self.columns = validate_columns(columns) or []
        self.row_pivots = validate_row_pivots(row_pivots) or []
        self.column_pivots = validate_column_pivots(column_pivots) or []
        self.aggregates = validate_aggregates(aggregates) or {}
        self.sort = validate_sort(sort) or []
        self.filters = validate_filters(filters) or []
        self.plugin_config = validate_plugin_config(plugin_config) or {}
        self.dark = dark

    @property
    def table(self):
        '''Returns the `perspective.Table` under management by the viewer.'''
        return self.manager.get_table(self.table_name)

    def load(self, table_or_data, **options):
        '''Given a `perspective.Table` or data that can be handled by `perspective.Table`, pass it to the viewer.

        `load()` resets the state of the viewer.

        If a `perspective.Table` is passed into `table_or_data`, `**options` is ignored as the options already set on the `Table` take precedence.

        If data is passed in, a `perspective.Table` is automatically created by this function,
        and the options passed to `**config` are extended to the new Table.

        Args:
            table_or_data (Table|dict|list|pandas.DataFrame) : a `perspective.Table` instance or a dataset to be displayed in the viewer.
            **options : optional keyword arguments that will be parsed by the `perspective.Table` constructor if data is passed in.
                - index (str) : the name of a column that will be the dataset's primary key. This sorts the dataset in ascending order based on primary key.
                - limit (int) : cannot be applied at the same time as `index` - the total number of rows that will be loaded into Perspective.
                    If the table is updated and the number of rows is greater than `limit`, updates begin overwriting at row 0.

        Examples:
            >>> from perspective import Table, PerspectiveViewer
            >>> data = {"a": [1, 2, 3]}
            >>> tbl = Table(data)
            >>> viewer = PerspectiveViewer()
            >>> viewer.load(tbl)
            >>> viewer.load(data, index="a") # kwargs are forwarded to the `Table` constructor.
        '''
        name = str(random())
        if isinstance(table_or_data, Table):
            table = table_or_data
        else:
            table = Table(table_or_data, **options)

        self.manager.host_table(name, table)

        '''
        if columns are different between the tables, then remove viewer state.

        sorting is expensive, but it prevents errors from applying pivots, etc. on columns that don't exist in the dataset.
        '''
        if self.table_name is not None:
            old_columns = sorted(self.manager.get_table(self.table_name).columns())
            new_columns = sorted(table.columns())

            if str(new_columns) != str(old_columns):
                logging.warning("New dataset has different columns - resetting viewer state.")
                self.columns = table.columns()
                self.row_pivots = []
                self.column_pivots = []
                self.aggregates = {}
                self.sort = []
                self.filters = []

        # If the user does not set columns to show, synchronize viewer state with dataset.
        if len(self.columns) == 0:
            self.columns = table.columns()

        self.table_name = name

    def update(self, data):
        '''Update the table under management by the viewer with new data.

        This function follows the semantics of `Table.update()`, and will be affected by whether an index is set on the underlying table.

        Args:
            data (dict|list|pandas.DataFrame) : the update data for the table.
        '''
        self.table.update(data)
