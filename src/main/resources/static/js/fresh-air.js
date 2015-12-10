jQuery(document).ready(function ($) {
    var settings;

    var $container = $(".nest-content");


    var nestToken = Cookies.get('nest-token');
    if (nestToken) {


        new ControlPanel(nestToken).render($container);

    } else {
        $.get("/api/settings", function (s) {
            settings = s;

            new LoginPanel(s).render($container);
        });

    }


    var LoginPanel = function () {
        this.render = function ($container) {
            $('<button class="btn btn-primary">Login to Nest</button>').on('click', function () {
                window.location.href = settings.authUrl;
            }).appendTo($container);
        }
    };


});
var ControlPanel = function (nestToken) {
    var structures;

    var connect = function () {
        var dataRef = new Firebase('wss://developer-api.nest.com');
        dataRef.auth(nestToken);


        dataRef.on('value', function (snapshot) {
            var data = snapshot.val();

            structures = toObjectArray(data.structures);
            structures.forEach(function (structure) {

                var pairs = findPairs(structure);
                //TODO display pairs with button pair
            });



        });
    };


    /**
     * Find pairs of devices in a structure with the same location (where_id).
     *
     * @param structure
     * @param devices
     * @returns {Array} of {@link Pair} or empty array.
     */
    var findPairs = function (structure, devices) {
        var tIds = structure.thermostats;
        var aIds = structure.smoke_co_alarms;
        var thermostats = toObjectArray(devices.thermostats);
        var alarms = toObjectArray(devices.smoke_co_alarms);


        if (tIds.length && aIds.length) {
            var structureThermostats = thermostats.filter(function (t) {
                return tIds.indexOf(t.device_id);
            });

            var structureAlarms = alarms.filter(function (t) {
                return aIds.indexOf(t.device_id);
            });

            var pairs = {};

            structureThermostats.forEach(function (t) {
                var whereId = t.where_id;
                var cachedPair = pairs[whereId];
                if (cachedPair) {
                    if (!cachedPair.hasOwnProperty('thermostats')) {
                        cachedPair.thermostats = [];
                    }
                    cachedPair.thermostats.push(t);
                } else {

                    pairs[whereId] = {
                        thermostats: [t],
                        where_id: whereId,
                        structure: structure
                    };
                }
            });

            structureAlarms.forEach(function (a) {
                var cached = pairs[a.where_id];
                if (cached) {
                    if (!cached.hasOwnProperty('alarms')) {
                        cached.alarms = [];
                    }
                    cached.alarms.push(a);
                }
            });

            var pairArray = toObjectArray(pairs);

            return pairArray;
        }
        return [];
    }

    this.render = function ($container) {
        connect();
    };

    function toObjectArray(obj) {
        var ids = Object.getOwnPropertyNames(obj);
        return ids.map(function (e) {
            return obj[e]
        });
    }


    function firstChild(object) {
        for (var key in object) {
            return object[key];
        }
    }

    function Pair(t, a, s) {

        this.thermostats = t;
        this.alarms = a;
        this.structure = s;

        this.paired = false;
    }
};

