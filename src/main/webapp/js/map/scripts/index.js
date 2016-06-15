/*  --------------------
*  Demo starter - Alibaba
*  (c) Kingcean Tuan, 2014.
*
*  File  index.ts
*  Description  Demo starter.
*  Owner  Kingcean Tuan <kingcean@live.com>
*  --------------------  */
define(["require", "exports", "map-selection/scripts/editor"], function (require, exports, editor) {
    var Main;
    (function (Main) {
        if (!false) {
            require("../css/demo.css");
            require("map-selection/scripts/editor");
        }
        function load() {
            Main.map = new editor.MapControl("page_show_map");
        }
        Main.load = load;
    })(Main || (Main = {}));
    return Main;
});
