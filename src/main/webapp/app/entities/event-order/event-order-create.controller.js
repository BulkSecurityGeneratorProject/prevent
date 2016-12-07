(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventCreateController', EventOrderController);

    EventOrderController.$inject = ['$scope',
        '$state',
        'Events',
        'entity',
        'EventType',
        'Locations',
        'ManageLocations',
        'Upload',
        '$timeout',
        'EventOrder',
        'ManageSearch',
        'geolocation'
    ];

    function EventOrderController($scope, $state, Events, entity,
                                  EventType, Locations, ManageLocations, Upload, $timeout,
                                  EventOrder, ManageSearch, geolocation) {
        var vm = this;
        vm.events = entity;

        vm.progressImage = 0;
        vm.progressFile = 0;


        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.eventtypes = [];
        vm.organizers = [];


        vm.getLocation = getLocation;


        vm.create = create;
        vm.uploadImage = uploadImage;
        vm.uploadFile = uploadFile;

        function getLocation(val) {
            if (val != null) {
                return ManageLocations.findByName(val)
                    .then(function (response) {
                        return response.data;
                    });
            }
        };

        function getOrganizer() {
            ManageSearch.findOrganizerAll()
                .then(function (response) {
                    vm.organizers = response.data;
                });
        };

        function getAllEventType() {
            return ManageSearch.findEventAll()
                .then(function (response) {
                    vm.eventtypes = response.data;
                });
        };

        function create() {
            swal({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Ya'
            }).then(function () {
                EventOrder.create(vm.events)
                    .then(function (response) {
                        swal(
                            'Created',
                            'Event success has been created',
                            'success'
                        )
                    }, function (error) {
                        console.log(error);
                        swal(
                            'Error',
                            'Event fail to be created ' + error.message,
                            'error'
                        )
                    });


            })
        };

        function save(data) {
            EventOrder.create(data)
                .then(function (response) {
                    swal(
                        'Created',
                        'Event success has been created',
                        'success'
                    )
                }, function (error) {
                    console.log(error);
                    swal(
                        'Error',
                        'Event fail to be created ' + error.message,
                        'error'
                    )
                });
        }

        function uploadImage(file) {
            Upload.upload({
                url: 'api/upload/image',
                data: {image: file}
            })
                .then(
                    function (response) {
                        $timeout(function () {
                            vm.events.image = response.data;
                        });
                    },
                    function (error) {
                    }, function (evt) {
                        vm.progressImage = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                    });
        };


        function uploadFile(file) {
            Upload.upload({
                url: 'api/upload/file',
                data: {file: file}
            })
                .then(
                    function (response) {
                        $timeout(function () {
                            vm.events.file = response.data;
                        });
                    },
                    function (error) {
                    }, function (evt) {
                        vm.progressFile = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                    });
        };

        vm.datePickerOpenStatus.starts = false;
        vm.datePickerOpenStatus.ends = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function init() {
            getAllEventType();
            getOrganizer();
            geolocation.getLocation().then(function (response) {
                vm.events.locations.latitude = response.coords.latitude;
                vm.events.locations.longitude = response.coords.longitude;
            }, function (error) {
                console.log(error)
            });
        }

        init();

    }
})();
