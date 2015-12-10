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

    var ControlPanel = function (nestToken) {
        var $container;
        var structures;
        var rendered = false;
        var dataRef;
        var connect = function () {
            dataRef = new Firebase('wss://developer-api.nest.com');
            dataRef.authWithCustomToken(nestToken, onComplete);
            function onComplete() {
                console.log(arguments)
            }

            dataRef.on('value', function (snapshot) {
                console.log("update", snapshot);
                var data = snapshot.val();
                if (!rendered) {
                    rendered = true;

                    function log() {
                        console.log(arguments);
                    }

                    $.postJSON(
                        'api/thermostats',
                        toObjectArray(data.devices.thermostats), log
                    );

                    $.postJSON(
                        'api/alarms',
                        toObjectArray(data.devices.smoke_co_alarms), log
                    );

                    $.postJSON('api/structures', toObjectArray(data.structures), function (s) {

                        structures = s;

                        for (var i = 0; i < structures.length; i++) {
                            displayStructure(structures[i]);
                        }
                        processData(data);

                    });
                } else {
                    processData(data);
                }

            });
        };

        var STRUCTURE_TEMPLATE = "<h3 id='${structure_id}'>${name}</h3>"
        var PAIR_TEMPLATE = "<h3 id='${id}'><span id='${alarm.device_id}'><span class='badge'/>${alarm.name}</span> - <span id='${thermostat.device_id}'>${thermostat.name}<span class='badge'/></span></h3>"

        function processData(data) {
            console.log("UPD: ", data)

            structures.forEach(function (storedStructure) {

                var structure = data.structures[storedStructure.structure_id];
                if (structure) {

                    storedStructure.pairs.forEach(function (p) {

                        var aId = p.alarm.device_id;
                        var tId = p.thermostat.device_id;

                        if (structure.smoke_co_alarms.indexOf(aId) >= 0 && structure.thermostats.indexOf(tId) >= 0) {
                            //pair still exists

                            var alarm = data.devices.smoke_co_alarms[aId];

                            $('span', "#" + aId).css("backgroundColor", alarm.ui_color_state).text(alarm.smoke_alarm_state);


                            var thermostat = data.devices.thermostats[tId];
                            var currentMode = thermostat.hvac_mode;

                            $('span', "#" + tId).text(currentMode);
                            var turnOnStates = ["warning", "emergency"];
                            var turnOn = turnOnStates.indexOf(alarm.smoke_alarm_state) >= 0;
                            if (turnOn) {


                                if ("off" == currentMode) {
                                    var mode = "heat";
                                    if (thermostat.can_cool && thermostat.can_heat) {
                                        mode = "heat-cool"
                                    } else if (thermostat.can_cool) {
                                        mode = "cool"
                                    }
                                    console.log("turn on fan");
                                    dataRef.child("/devices/thermostats/" + tId).update({"hvac_mode": mode});

                                }

                            } else if (currentMode != 'off' && !turnOn) {
                                //turn off
                                console.log("turn off fan");
                                dataRef.child("/devices/thermostats/" + tId).update({"hvac_mode": 'off'});
                            }
                        } else {

                            //pair doesn't exists need to update structures.
                        }
                    });
                } else {
                    //structures not found, need to update structures.
                }
            });
        }

        function displayStructure(s) {

            var structureElement = $.tmpl(STRUCTURE_TEMPLATE, s);
            $container.append(structureElement);

            s.pairs.forEach(function (p) {
                $container.append($.tmpl(PAIR_TEMPLATE, p));
            })
        }


        this.render = function ($c) {
            $container = $c;
            connect();
        };

        function toObjectArray(obj) {
            var ids = Object.getOwnPropertyNames(obj);
            return ids.map(function (e) {
                return obj[e]
            });
        }
    };
});
