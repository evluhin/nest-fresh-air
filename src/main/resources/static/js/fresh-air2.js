jQuery(document).ready(function ($) {

    $.postJSON = function (url, data, callback) {
        return jQuery.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': url,
            'data': JSON.stringify(data),
            'dataType': 'json',
            'success': callback
        });
    };

    var $container = $(".nest-content");

    var nestToken = Cookies.get('nest-token');
    if (nestToken) {
        new ControlPanel(nestToken).render($container);
    } else {
        $.get("/api/settings", function (s) {
            new LoginPanel(s).render($container);
        });
    }

    /**
     * Simply renders login button.
     */
    var LoginPanel = function (settings) {
        this.render = function ($container) {
            $('<button class="btn btn-primary">Login to Nest</button>').on('click', function () {
                window.location.href = settings.authUrl;
            }).appendTo($container);
        }
    };

    /**
     * Controller function that manages logged-in users.
     * @param nestToken token to communicate with nest api.
     */
    function ControlPanel (nestToken) {
        var $container;

        var dataRef;

        /**
         * Main entry point;
         * @param $c
         */
        this.render = function ($c) {
            $container = $c;
            connect();
        };

        var connect = function () {
            dataRef = new Firebase('wss://developer-api.nest.com');
            dataRef.authWithCustomToken(nestToken, onComplete);
            function onComplete() {
                console.log(arguments)
            }

            dataRef.on('value', function (snapshot) {
                var data = snapshot.val();
                console.log("update", data);

                $.postJSON('api/data',
                    {
                        thermostats: toObjectArray(data.devices.thermostats),
                        alarms: toObjectArray(data.devices.smoke_co_alarms),
                        structures: toObjectArray(data.structures)
                    },
                    function(structures){
                        //we could update view only when children are added or removed, for value changes just update the view
                        updateView(structures);

                        //we can just process data if no children are added or removed,
                        processStructures(structures);
                     });
                });
        };

        /**
         * Makes decision whether thermostat should de turned on.
         * @param structures organized structure with its devices and pairs (saved).
         */
        function processStructures(structures) {
            structures.forEach(function (structure) {
                console.log("processing structure: ", structure);
                structure.pairs.forEach(function (pair) {

                    processPair(pair);
                    updatePairView(pair);
                });
            });
        }

        /**
         * @param pair contains a pair of devices to control depends on their measurements.
         */
        function processPair(pair) {
            var alarm = pair.alarm;
            var thermostat = pair.thermostat;
            var currentMode = thermostat.hvac_mode;
            var thermostatId = thermostat.device_id;

            console.log("processPair: ", pair);
            var turnOn = ["warning", "emergency"].indexOf(alarm.smoke_alarm_state) >= 0;
            if (turnOn) {
                if ("off" == currentMode) {
                    var mode = getMode(thermostat);
                    console.log("turn on thermostat: ", mode);
                    dataRef.child("/devices/thermostats/" + thermostatId).update({"hvac_mode": mode});
                }
            } else if (currentMode != 'off' && !turnOn) {
                //turn off, even if it was turned on manually
                console.log("turn off thermostat");
                dataRef.child("/devices/thermostats/" + thermostatId).update({"hvac_mode": 'off'});
            }
        }

        //--------- VIEW

        /**
         * Update view be re-rendering all structures
         * @param structures
         */
        function updateView(structures) {
            $container.empty();

            for (var i = 0; i < structures.length; i++) {
                displayStructure(structures[i]);
            }
        }

        var STRUCTURE_TEMPLATE = "<h3 id='${structure_id}'>${name}</h3><hr>"
        var PAIR_TEMPLATE = "<h3 id='${id}'><span id='${alarm.device_id}'>${alarm.name}<span class='badge a'/></span> - <span id='${thermostat.device_id}'>${thermostat.name}<span class='badge t'/></span></h3>"

        /**
         * Add UI components.
         * @param structure
         */
        function displayStructure(structure) {

            var structureElement = $.tmpl(STRUCTURE_TEMPLATE, structure);
            $container.append(structureElement);

            structure.pairs.forEach(function (p) {
                $container.append($.tmpl(PAIR_TEMPLATE, p));
            });
        }


        /**
         * Update UI of a pair.
         * @param pair
         */
        function updatePairView(pair) {
            var alarmState = pair.alarm.smoke_alarm_state;

            var alarmId = pair.alarm.device_id;
            var thermostatId = pair.thermostat.device_id;

            $('#' + pair.id).attr('class', alarmState);

            $('span', "#" + alarmId).text(alarmState);

            var currentMode = pair.thermostat.hvac_mode;
            $('span', "#" + thermostatId).text(currentMode);
        }


        /**
         * Get possible mod for thermostat depends on its possibilities.
         * @param thermostat
         * @returns {string} {@code heat-cool} if available or any other available mode.
         */
        function getMode(thermostat) {
            if (thermostat.can_cool && thermostat.can_heat) {
                return "heat-cool"
            } else if (thermostat.can_cool) {
                return "cool"
            }
            return "heat";
        }


        /**
         * Helper function that converts <code>{'a': {'b':'c'}, '1':{'2':'3'}}</code> into <code>[{'b':'c'}, {'2':'3'}]</code>
         * @param obj any object
         * @returns {Array}
         */
        function toObjectArray(obj) {
            var ids = Object.getOwnPropertyNames(obj);
            return ids.map(function (e) {
                return obj[e]
            });
        }
    };
});
