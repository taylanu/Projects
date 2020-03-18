---
id: usage
title: User Guide
---

## Overview

The core concepts of Perspective are the `table()`, `view()` and
`<perspective-viewer>` web component, though your project need not necessarily
use them all. A `table()` represents a single data set, and is the interface
used to input static and streaming data into Perspective; in the Browser,
`table()`s live in a Web Worker to isolate their runtime from the renderer.
A `view()` represents a specific continuous query of a `table()`, and is
used to read data or calculate analytics from a `table()`; `view()`s also
live in a Web Worker when used in a Browser, and a single `table()` may have
many `view()`s attached at once. `<perspective-viewer>` is a UI widget
which allows the user to interact and create `view()`s on a loaded `table()`.
Each `<perspective-viewer>` encapsulates and manages a single `view()` at a
time, and optionally manages it's underlying `table()` for simple use cases,
though you can instantiate this separately if you wish - this is helpful for
e.g. [sharing a table](<(#sharing-a-table-between-multiple-perspective-viewers)>)
between multiple `<perspective-viewer>`s

<img src="https://perspective.finos.org/svg/architecture.svg">

Perspective is designed for flexibility, allowing developers to pick and choose
which modules they need for their specific use case. The main modules are:

-   `@finos/perspective`  
    The data engine library, as both a browser ES6 and Node.js module. Provides a
    WebAssembly, WebWorker (browser) and Process (node.js)
    runtime.

-   `@finos/perspective-viewer`  
    A user-configurable visualization widget, bundled as a [Web Component](https://www.webcomponents.org/introduction).
    This module includes the core data engine module as a dependency.

`<perspective-viewer>` by itself only implements a trivial debug renderer, which
prints the currently configured `view()` as a CSV. Plugin modules for popular
Javascript libraries such as [d3fc](https://d3fc.io/)
are packaged separately and must be imported individually; in this way,
developers can choose to opt into the features, bundle size inflation and
licensing for these dependencies as needed. When imported after
`@finos/perspective-viewer`, the plugin modules will register
themselves automatically, and the renderers they export will be available in the
`view` dropdown in the `<perspective-viewer>` UI.

-   `@finos/perspective-viewer-hypergrid`  
    A `<perspective-viewer>` plugin for [Hypergrid](https://github.com/fin-hypergrid/core).

-   `@finos/perspective-viewer-d3fc`  
    A `<perspective-viewer>` plugin for the [d3fc](https://d3fc.io) charting library.

-   `@finos/perspective-viewer-highcharts`  
    A `<perspective-viewer>` plugin for [HighCharts](https://github.com/highcharts/highcharts).
    This plugin has a dependency on Highcharts' [mixed commercial license](https://creativecommons.org/licenses/by-nc/3.0/),
    and is no longer under active development.

Depending on your requirements, you may need just one, or all Perspective modules.
Some basic guidelines to help you decide what is most appropriate for your
project:

-   If you are only interested in using Perspective as a simple, browser-based
    data visualization widget, you probably only need the
    `@finos/perspective-viewer` module and optionally its plugins
    `@finos/perspective-viewer-hypergrid` for data grids, and
    `@finos/perspective-viewer-d3fc`
    for charting. The core data engine `@finos/perspective` is a 
    depedency of these packages and does not need to be imported on its own for 
    basic usage. Details for these can be found [here](#perspective-viewer-web-component).

-   If you are only interested in the high-performance streaming data engine
    (the WebAssembly part), or your project is purely Node.js based, you need only
    the `@finos/perspective` module, detailed [here](#perspective-library).

-   For more complex cases, such as
    [sharing tables between viewers](#sharing-a-table-between-multiple-perspective-viewers)
    and [binding a viewer to a remote view in node.js](#remote-perspective-via-workerhost),
    you will likely need all Perspective modules.

## `perspective` library

The main module exporting `table()` and `view()`, as well as process
management functions such as `worker()` and `websocket()` (in the browser) and 
`WebSocketServer()` (in node.js). This module is
not needed if you only intend to use `<perspective-viewer>` to visualize
simple data, and is a dependency of the `@finos/perspective-viewer`
module. For a complete reference on all exported methods in `perspective`, see the
[full API Docs](https://github.com/finos/perspective/tree/master/packages/perspective);
presented here is a basic overview of the module's usage.

As a library, `perspective` provides a suite of streaming pivot, aggregate, filter
and sort operations for tabular data. The engine can be instantiated in process,
or in a Web Worker (browser only); in both cases, `perspective` exports a
nearly identical API.

### Importing in the browser

`perspective` can be imported ES6 module and/or `require` syntax if you're using
a bundler such as Webpack (and the `@finos/perspective-webpack-plugin`):

```javascript
import perspective from "@finos/perspective";
```

or

```javascript
const perspective = require("@finos/perspective");
```

`@finos/perspective` also comes with pre-built bundle which exports the global
`perspective` module name in vanilla Javascript, when e.g. importing
[via a CDN](https://unpkg.com/@finos/perspective/build/perspective.js).

```html
<script src="perspective.js"></script>
```

Once imported, you'll need to instance a `perspective` engine via the `worker()`
method. This will create a new WebWorker (browser) or Process (node.js), and
load the WebAssembly binary; all calculation
and data accumulation will occur in this separate process.

```javascript
const worker = perspective.worker();
```

The `worker` symbol will expose the full `perspective` API for one managed
Web Worker process. You are free to create as many as your browser supports,
but be sure to keep track of the `worker` instances themselves, as you'll
need them to interact with your data in each instance.

### Importing in Node.js

The `Node.js` runtime for the `@finos/perpsective` module runs
in-process by default, and does not implement a `child_process` interface.
Hence, there is no `worker()` method, and the module object itself directly
exports the full `perspective` API.

```javascript
const perspective = require("@finos/perspective");
```

### Loading data with `table()`

The basic data primitive of `perspective` is the `table`, which you can
instantiate via the `table()` method on a `worker`. Further data can be
supplied to the table via its `update()` method.

```javascript
// With data (also works with strings in CSV format)
var data = [
    { x: 1, y: "a", z: true },
    { x: 2, y: "b", z: false },
    { x: 3, y: "c", z: true },
    { x: 4, y: "d", z: false }
];

const table1 = worker.table(data);
```

`table()`s are columnar data structures, and each column must have a single
type - perspective supports `integer`, `float`, `string`, `boolean`, and `datetime` types.
When passing simple data directly to the `table()` constructor, the type of
each column is inferred automatically; however, in some case, the inferrence
algorithm may not return exactly what you'd like. For example, a column may
be interpretted as a `datetime` when you intend it to be a `string`, or a
column may have no values at all (yet), as it will be updated with values
from a real-time data source later on. In these cases, it may be useful to
instead instatiate a `table()` with a <i>schema</i>:

```javascript
// With a schema
var schema = {
    x: "integer",
    y: "string",
    z: "boolean"
};

const table2 = worker.table(schema);
```

Once instatiated, a `table()` can later be updated with new data via the
`update()` method. New data is appended by default, for in-place updates, see
[index and limit tables](#index-and-limit)

```javascript
// Add a new row to each table
table1.update([{ x: 5, y: "e", z: true }]);
table2.update([{ x: 5, y: "e", z: true }]);
```

#### `index` and `limit`

The `table()` method also takes an options object, with which you can provide
the name of an <i>index</i> column in the underlying dataset, which will act as a
primary key on the `table`, and the `update()` method will replace or append
rows, based on the value of the <i>index</i>. When a `table()` is indexed, the `update()` rows must contain this value, otherwise it will have a default
<i>index</i> value of `null`.

```javascript
// Use the 'x' column as a primary key
const table3 = worker.table(data, { index: "x" });
```

Mutually exclusive to `index`, you may limit the total number of rows in a
`table()` via the `limit` property, which preserves only the most recently
added rows. <i>Limit</i> `table()`s otherwise share the same append-only
`update()` behavior as default.

```javascript
// Keep only the most recent 1000 rows
const table3 = worker.table(data, { limit: 1000 });
```

#### <i>Unset</i> values via `null`

Perspective `table()` allow any value of a <i>column</i> to be <i>unset</i>
on construction, or later be reverted to <i>unset</i> in the case of a `table()`
with an <i>index</i>. Perspective uses the Javascript value `null` to indicate
values which should be explicitly <i>unset</i>, in either the constructor or in
a subsequent call to the `update()` method.

```javascript
// Unsets the 'y' column for row 3
table.update([{ x: 3, y: null }]);
```

#### Partial <i>row</i> updates via `undefined`

When calling `update()`, you need not provide values for all <i>column</i>s
defined in the `table()`s schema. Missing keys, or keys with `undefined`
values, will be omitted from `table()`s with an <i>index</i>, and will be
populated with `null` for default and <i>limit</i> `table()`s.

```javascript
// Only updates the  'y' column for row 3, leaving the 'z' column alone
table.update([{ x: 3, y: "Just Y" }]);
```

#### Deleting <i>rows</i> with `remove()`

For `table()`s with an <i>index</i>, rows can be removed entirely by calling
the `remove()` method with a list of the <i>index</i> values to be removed.

```javascript
// Remove rows indexed 1 and 2
table.remove([1, 2]);
```

### Querying data with `view()`

In order to query the table, you must then create a `view()`, an immutable set
of transformations for an associated `table()`. A `view()` is created from a
`table()` via a configuration object, and a `view()` instance can return data
from queries to its various methods. For a full description of the available
configuration properties for `view()`, see the
[full API documentation](https://github.com/finos/perspective/tree/master/packages/perspective).

```javascript
const table = worker.table(data);

const view = table.view({
    columns: ["Sales"],
    aggregates: {"Sales": "sum"},
    row_pivot: ["Region", "Country"],
    filter: [["Category", "in", ["Furniture", "Technology"]]]
});
```

The `view()` object has several options for extracting slices of data for the
provided configuration, the most commonly used of which are the `to_json()` and
`to_columns` methods, which return a Javascript `promise`` for the calculated data
in row-oriented or column-oriented JSON format, respectively.

```javascript
// Via standard `promise`
view.to_json().then(json => console.log(json));
view.to_columns().then(json => console.log(json));

// Via ES6 await/async
console.log(await view.to_json());
console.log(await view.to_columns());
```

A `table()` can have as many `view()` associated with it as you need!
Perspective will conserve memory and updates by relying on a single instance of
a `table()` to power multiple `view()`s concurrently.

### Flattening a `view()` into a `table()`

You can provide a `view()` instance to the `perspective.table()` method to
create a new `table()` based off of this view's data, including it's future
updates.  This is especially useful for piping together `perspective` data from
different context, such as streaming a server-side `view()` in node.js into a
`table()` in a WebWorker on the browser-side.

```javascript
// Create two WebWorkers.
const worker1 = perspective.worker();
const worker2 = perspective.worker();

// Create a `table and `view` on `worker1`.
const table = worker1.table(data);
const view = table.view({filter: [["State", "==", "Texas"]]});

// Create a table from `view` in `worker2`
const table2 = worker2.table(view);
const view2 = table2.view({filter: [["City", "==", "Austin"]]});

//  Both `view1` and `view2` are updated.
table.update([{State: "Texas", City: "Austin"}]);

//  Only `view1` is updated.
table.update([{State: "Texas", City: "San Antonio"}]);
```

### Deleting a `table()` or `view()`

Unlike standard Javascript objects, Perspective objects such as `table()` and
`view()` store their associated data in the WebAssembly heap. Due of this,
and the current lack of a hook into the Javascript runtime's garbage collector
from WebAssembly, the memory allocated to these Perspective objects does not
automatically get cleaned up when the object falls out of scope. In order to
prevent memory leaks and reclaim the memory associated with a Perspective
`table()` or `view()`, you must call the `delete()` method.

```javascript
view.delete();

// This method will throw an exception if there are still `view()`s depending
// on this `table()`!
table.delete();
```
### Customizing behavior with `perspective.config.js`

For ease of configu synchronization between the Node.js, WebWorker and Browser,
Perspective supports configuration statically.  You may override Perspective's
[default settings](https://github.com/finos/perspective/blob/master/packages/perspective/src/js/config/settings.js)
via a `perspective.config.js` or `perspective.config.json`
file in your project's root or parent path, or via the `"perspective"` key in
your project's `package.json`.

Note that, while in Node.js this config file is read at runtime, for the browser
this file must be read at compile time (handled automatically via
[`@finos/perspective-webpack-plugin`](https://github.com/finos/perspective/tree/master/packages/perspective-webpack-plugin)).
Thus, to update it you must either
rebuild your code, or supply the JSON configuration object to the `worker()`
constructor on initialization.

```javascript
module.exports = {
    types: {
        string: {
            aggregate: "dominant"
        },
        float: {
            format: {
                style: "decimal",
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }
        }
    }
};
```

#### Creating new types

For customizing the behavior or style of specific columns, perspective supports
the definition of new types, deriving from an existing built-in type.  First,
add a new type and declare its base in your `perspctive.config.js`.

```javascript
module.exports = {
    types: {
        price: {type: "float"}
    }
};
```

Perspective will not infer these types for you, so you'll need to create your
table [from a schema](#loading-data-with-table) to use them.

```javascript
const table = worker.table({volume: "integer", price: "price"});
table.update([{volume: 10, price: 100.75}]);
```

## `perspective-viewer` web component

As a component, `perspective-viewer` provides a complete graphical UI for
configuring the `perspective` library and formatting its output to the
provided visualization plugins.

If you are using babel or another build environment which supports ES6 modules,
you need only import the `perspective-viewer` libraries somewhere in your
application - these modules export nothing, but rather register the components
for use within your site's regular HTML:

```javascript
import "@finos/perspective-viewer";
import "@finos/perspective-viewer-hypergrid";
import "@finos/perspective-viewer-d3fc";
```

While each plugin module by default will register all of its visualization types,
you may choose to import these individually as well:

```javascript
import "@finos/perspective-viewer-d3fc/bar";
import "@finos/perspective-viewer-d3fc/column";
import "@finos/perspective-viewer-d3fc/xy-scatter";
import "@finos/perspective-viewer-highcharts/treemap";
```

You may also import a theme when bundling perspective-viewer. Even though there
are only 2 of them.

```javascript
// A theme based on Google's Material Design Language
import "@finos/perspective-viewer/build/material.css";
```

Alternatively, if you're fine with a default theme and don't want to bundle yourself,
you can just import the pre-bundled assets from their respective modules, which
export their default visualizations.

```html
<script src="perspective-viewer.js"></script>
<script src="perspective-viewer-hypergrid.js"></script>
<script src="perspective-viewer-d3fc.js"></script>

<!-- Theme available separately if you are so inclined -->
<link rel='stylesheet' href='material.css'>
```

Once imported, the `<perspective-viewer>` Web Component will be available in
any standard HTML on your site. A simple example:

```html
<perspective-viewer id="view1"></perspective-viewer>
```

### Loading data

To load data into a `<perspective-viewer>`, you can call the `load()` method
from Javascript on simple JSON or CSV data, or an `arraybuffer` of an Apache
Arrow. If you do so via an inline `<script>` tag, you must wait for the
document `WebComponentsReady` event to fire, which indicates that the provided
[webcomponents.js polyfill](https://github.com/webcomponents/webcomponentsjs)
has loaded.

```javascript
document.addEventListener("WebComponentsReady", function() {
    var data = [
        { x: 1, y: "a", z: true },
        { x: 2, y: "b", z: false },
        { x: 3, y: "c", z: true },
        { x: 4, y: "d", z: false }
    ];

    var viewer = document.getElementById("view1");
    viewer.load(data);

    // Add new row
    viewer.update([{ x: 5, y: "e", z: true }]);
});
```

In some situations, you may want finer grained control over the `table()`
construction (see below), and `<perspective-viewer>`'s `load()` method
also supports loading `table()`s directly. Remember to import the `perspective.js`
library also, or the `perspective` symbol will not be available - though you
can always use the `worker` property on a `<perspective-viewer>` to get the
default worker singleton instantiated when another is not provided.

```javascript
// Create a new worker, then a new table on that worker.
var table = perspective.worker().table(data);

// Bind a viewer element to this table.
viewer.load(table);
```

### Sharing a `table()` between multiple `perspective-viewer`s

Views can share a table by instancing it separately and passing it to the
`load()` method. Both Views will update when the underlying `table`'
s `update()` method is invoked, but `table.delete()` will fail
until all Views which use it are also deleted.

```javascript
var view1 = document.getElementById("view1");
var view2 = document.getElementById("view2");

// Create a new Web Worker
var worker = perspective.worker();

// Create a table in this worker
var table = worker.table(data);

// Load the same table in 2 different <perspective-viewer>s
view1.load(table);
view2.load(table);

// Both `view1` and `view2` will reflect this update
table.update([{ x: 5, y: "e", z: true }]);
```

### Remote Perspective via `WebSocketServer()`

For exceptionally large datasets, a `<perspective-viewer>` can be bound to a
`perspective.table()` instance running in node.js remotely, rather than
creating one in a Web Worker and downloading the entire data set. This
trades off network bandwidth and server resource requirements, for a smaller
browser memory and CPU footprint.

In Node.js:

```javascript
const {
    WebSocketServer,
    table
} = require("@finos/perspective");
const fs = require("fs");

// Start a WS/HTTP host on port 8080.  The `assets` property allows
// the `WebSocketServer()` to also serves the file structure rooted in this
// module's directory.
const host = new WebSocketServer({ assets: [__dirname], port: 8080 });

// Read an arrow file from the file system and load it as a named table.
const arr = fs.readFileSync(__dirname + "/superstore.arrow");
const tbl = table(arr);
host.host_table("table_one", tbl);

// Or host a view 
const view = tbl.view({filter: [["State", "==", "Texas"]]});
host.host_view("view_one", view);
```

In the browser:

```javascript
const elem = document.getElementsByTagName("perspective-viewer")[0];

// Bind to the server's worker instead of instantiating a Web Worker.
const websocket = perspective.websocket(window.location.origin.replace("http", "ws"));

// Bind the viewer to the preloaded data source.  `table` and `view` objects 
// live on the server.
elem.load(websocket.open_table("table_one"));

// Or load data from a view.  The browser now also has a copy of this view in
// its own `table`, as well as its updates.  Transfer uses Arrows.
elem.load(websocket.open_view("view_one"));
```

`<perspective-viewer>` instances bound in this way are otherwise no different
than `<perspective-viewer>`s which rely on a Web Worker, and can even share a
host application with Web Worker bound `table()`s. The same `promise` based
API is used to communicate with the server instantiated `view()`, only in this
case it is over a Web Socket.

### Setting & reading viewer configuration via Attributes

`<perspective-viewer>` uses the DOM's regular attribute API to set it's
initial state, by reading JSON encoded properties from attributes for each
`perspective` configuration property. For example, this HTML will apply
`row_pivot` and `filter` configuration to the initial `view()` created when
data is loaded via the `load()` method, as well as set the UI controls to
reflect this config. See the
[full Attribute API documentation](https://github.com/finos/perspective/tree/master/packages/perspective-viewer)
for a full description of the available Attributes.

```html
<perspective-viewer
    row-pivots='["Category","Sub-Category"]'
    filters='[["State","==","Texas"]]'>

</perspective-viewer>
```

Attributes on a `<perspective-viewer>` are reactive. When the user interacts
with the viewer, the attribtues will update to reflect the current viewer
state, and these can be read with the standard DOM API method `getAttribute()`;
likewise, the `setAttribute()` method will update the `view()` and UI state.

```javascript
// Gets  `elem`'s pivot state
var pivots = elem.getAttribute("row-pivots");

// Set `elems`'s sort state
elem.setAttribute("sort", JSON.stringify([["Sales", "desc"]]));
```

Alternatively, the `save()` and `restore()` methods allow you to read and write,
respectively, all `view()` configuration properties at once.

```javascript
// Get the current elem config
var state = elem.save();

// ... later

// Restore the previously saved state
elem.restore(state);
```

### Update events

Whenever a `<perspective-viewer>`s underlying `table()` is changed via the
`load()` or `update()` methods, a `perspective-view-update` DOM event is fired.
Similarly, `view()` updates instigated either throught he Attribute API, or
through user interaction will fire a `perspective-config-update` event.

```javascript
elem.addEventListener("perspective-config-update", function() {
    var config = elem.save();
    console.log("The view() config has changed to " + JSON.stringify(config));
});
```

### Click events

Whenever a `<perspective-viewer>`'s grid or chart are clicked, a 
`perspective-click` DOM event is fired containing a detail object with `config`,
`column_names` and `row`.
The `config` object contains an array of `filters` that can be applied to a 
`<perspective-viewer>` through the use of `restore()` updating it to show the 
filtered subset of data.
The `column_names` property contains an array of matching columns and the `row` 
property returns the associated row data.

```javascript
elem.addEventListener("perspective-click", function(event) {
    var config = event.detail.config;
    elem.restore(config);
});
```