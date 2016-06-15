/*  --------------------
 *  Tests loader - Map Selection - Alibaba
 *  (c) Kingcean Tuan, 2014.
 *
 *  File  config.js
 *  Description  The loader for tests and demo.
 *  Owner  Kingcean Tuan <kingcean@live.com>
 *  --------------------  */

(function () {
    if (typeof DEBUG === "undefined") DEBUG = true;
    AliHub.Runtime.KissyLoader.enable(true, "http://g.tbcdn.cn/hub/quark/1.0.150/", "http://g.tbcdn.cn/hub/quark-bindings/1.0.110/");
    var now = new Date();
    if (typeof KISSY === "undefined") KISSY = modulex;
    KISSY.config({
        packages: {
            "map-selection": {
                path: "http://g.tbcdn.cn/hub/map-selection/0.2.21/",
                debug: DEBUG,
                tag: now.getTime().toString(),
                combine: false,
                ignorePackageNameInUri: true,
                charset: "utf-8"
            },
            local: {
                path: "/js/map/",
                debug: DEBUG,
                tag: now.getTime().toString(),
                combine: false,
                ignorePackageNameInUri: true,
                charset: "utf-8"
            },
            zeroClipboard: {
                path: "https://alinw.alicdn.com/alinw/zero-clipboard/1.0.0/",
                debug: DEBUG,
                tag: now.getTime().toString(),
                combine: false,
                ignorePackageNameInUri: true,
                charset: "utf-8"
            },
            jquery: {
                path: "http://g.tbcdn.cn/hub/jquery/",
                debug: DEBUG,
                tag: now.getTime().toString(),
                combine: false,
                ignorePackageNameInUri: true,
                charset: "utf-8"
            },
            knockout: {
                path: "http://g.tbcdn.cn/hub/knockout/",
                debug: DEBUG,
                tag: now.getTime().toString(),
                combine: false,
                ignorePackageNameInUri: true,
                charset: "utf-8"
            }
        }
    });
})();