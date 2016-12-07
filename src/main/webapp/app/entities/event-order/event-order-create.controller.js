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
        'geolocation',
        'FileManager',
        'ImageManager'
    ];

    function EventOrderController($scope, $state, Events, entity,
                                  EventType, Locations, ManageLocations, Upload, $timeout,
                                  EventOrder, ManageSearch, geolocation, FileManager, ImageManager) {
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

        vm.removeFile = removeFile;
        vm.removeImage = removeImage;

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
            if (vm.events.id == null) {
                swal({
                    title: 'Create?',
                    text: "Create Event",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Ya'
                }).then(function () {
                    EventOrder.create(vm.events)
                        .then(function (response) {
                            $state.go('event', null, {reload: true});
                            swal(
                                'Created',
                                'Event success has been created',
                                'success'
                            )
                        }, function (error) {
                            console.log(error);
                            swal(
                                'Error',
                                error.headers('X-preventApp-error'),
                                'error'
                            )
                        });


                })
            } else {
                swal({
                    title: 'Update ?',
                    text: "Update Event",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Ya'
                }).then(function () {
                    EventOrder.update(vm.events)
                        .then(function (response) {
                            $state.go('event', null, {reload: true});
                            swal(
                                'Update',
                                'Event success has been updated',
                                'success'
                            )
                        }, function (error) {
                            swal(
                                'Error',
                                error.headers('X-preventApp-error'),
                                'error'
                            )
                        });


                })

            }

        };


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

        function removeFile() {
            FileManager
                .deleteFile(vm.events.file.id)
                .then(function (response) {
                    console.log(response);
                    vm.events.file = null;
                    vm.file = null;
                    vm.progressFile = 0;
                }, function (error) {
                    console.log(error);
                });
        };

        function removeImage() {
            ImageManager
                .deleteImage(vm.events.image.id)
                .then(function (response) {
                    console.log(response);
                    vm.events.image = null;
                    vm.image = null;
                    vm.progressImage = 0;
                }, function (error) {
                    console.log(error);
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
                if (vm.events.locationLatitude == 0) {
                    vm.events.locationLatitude = response.coords.latitude;
                }
                if (vm.events.locationLongitude == 0) {
                    vm.events.locationLongitude = response.coords.longitude;
                }
            }, function (error) {
                console.log(error)
            });
        }

        init();

    }
})();
