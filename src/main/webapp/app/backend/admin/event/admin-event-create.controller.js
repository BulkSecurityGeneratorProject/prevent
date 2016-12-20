(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdminEventCreateController', AdminEventCreateController);

    AdminEventCreateController.$inject = [
        '$scope',
        '$state',
        'entity',
        'ManageLocations',
        'Upload',
        '$timeout',
        'AdminEvent',
        'ManageSearch',
        'geolocation',
        'FileManager',
        'ImageManager',
        'ListOrder',
        'OrderMerchandise',
        'OrderCirculation',
        'OrderAds',
        'OrderRedaction',
        'previousState'
    ];

    function AdminEventCreateController($scope,
                                        $state, entity,
                                        ManageLocations,
                                        Upload, $timeout,
                                        AdminEvent, ManageSearch,
                                        geolocation, FileManager,
                                        ImageManager, ListOrder,
                                        OrderMerchandise, OrderCirculation,
                                        OrderAds, OrderRedaction, previousState) {
        var vm = this;
        vm.isMap = false;
        vm.location = {};

        vm.previousState = previousState;
        console.log(vm.previousState);

        vm.events = entity;

        vm.merchandises = [];
        vm.circulations = [];

        vm.progressImage = 0;
        vm.progressFile = 0;


        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.eventtypes = [];
        vm.organizers = [];
        vm.ads = [];
        vm.redactions = [];


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
            ManageSearch.findEventTypeAll()
                .then(function (response) {
                    vm.eventtypes = response.data;
                });
        };

        function create() {
            if (vm.events.id == null) {
                swal({
                    title: 'Buat?',
                    text: "Apakah anda yakin akan buat Event ini ?",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Ya'
                }).then(function () {
                    AdminEvent.create(vm.events)
                        .then(function (response) {
                            $state.go('event', null, {reload: true});
                            swal(
                                'Created',
                                'Event sukses dibust',
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
                    AdminEvent.update(vm.events)
                        .then(function (response) {
                            $state.go('event', null, {reload: true});
                            swal(
                                'Update',
                                'Event sukses di update',
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

        function getAllMerchandise() {
            ListOrder.getListMerchandise()
                .then(function (response) {
                    vm.merchandises = response.data;
                }, function (error) {
                    console.log(error);
                })
        }

        function getAllCirculation() {
            ListOrder.getListCirculation()
                .then(function (response) {
                    vm.circulations = response.data;
                }, function (error) {
                    console.log(error);
                })
        }


        function getAllAds() {
            ListOrder.getListAds()
                .then(function (response) {
                    vm.ads = response.data;
                }, function (error) {
                    console.log(error);
                })
        }

        function getAllRedaction() {
            ListOrder.getListRedaction()
                .then(function (response) {
                    vm.redactions = response.data;
                }, function (error) {
                    console.log(error);
                })
        }


        //order merchandise
        vm.addOrderMerchandise = addOrderMerchandise;
        vm.orderMerchandise = {};

        function addOrderMerchandise(merchandise) {
            vm.orderMerchandise.merchandise = merchandise;
            for (var i = 0; i < vm.events.orderMerchandises.length; i++) {
                if (vm.events.orderMerchandises[i].merchandise.id == vm.orderMerchandise.merchandise.id) {
                    swal(
                        'Error',
                        'Order ' + merchandise.name + " sudah ada dalam list order",
                        'error'
                    );
                    return false;
                }
            }
            vm.events.orderMerchandises.push(vm.orderMerchandise);
            vm.orderMerchandise = {};
        };

        vm.deleteOrderMerchandise = function deleteOrderMerchandise(id, idx) {
            OrderMerchandise.delete({id: id},
                function (response) {
                    vm.events.orderMerchandises.splice(idx, 1);
                }, function (error) {
                    console.log(error);
                });
        };

        //end order merchandise


        // order circulation
        vm.addOrderCirculations = addOrderCirculations;
        vm.orderCirculation = {};

        function addOrderCirculations(circulation) {
            vm.orderCirculation.circulation = circulation;
            for (var i = 0; i < vm.events.orderCirculations.length; i++) {
                if (vm.events.orderCirculations[i].circulation.id == vm.orderCirculation.circulation.id) {
                    swal(
                        'Error',
                        'Order ' + circulation.name + " sudah ada dalam list order",
                        'error'
                    );
                    return false;
                }
            }
            vm.events.orderCirculations.push(vm.orderCirculation);
            vm.orderCirculation = {};
        };

        vm.deleteOrderCirculation = function deleteOrderCirculation(id, idx) {
            OrderCirculation.delete({id: id},
                function (response) {
                    vm.events.orderCirculations.splice(idx, 1);
                }, function (error) {
                    console.log(error);
                });
        };
        //end order circulation

        // order ads
        vm.orderAds = {};
        vm.addOrderAds = function addOrderAds(ads) {
            vm.orderAds.ads = ads;
            for (var i = 0; i < vm.events.orderAds.length; i++) {
                if (vm.events.orderAds[i].ads.id == vm.orderAds.ads.id) {
                    swal(
                        'Error',
                        'Order ' + ads.name + " sudah ada dalam list order",
                        'error'
                    );
                    return false;
                }
            }
            vm.events.orderAds.push(vm.orderAds);
            vm.orderAds = {};
        };

        vm.deleteOrderAds = function deleteOrderAds(id, idx) {
            OrderAds.delete({id: id},
                function (response) {
                    vm.events.orderAds.splice(idx, 1);
                }, function (error) {
                    console.log(error);
                });
        };
        //end order ads


        // order redactions
        vm.orderRedaction = {};
        vm.addOrderRedaction = function (redaction) {
            vm.orderRedaction.redaction = redaction;
            for (var i = 0; i < vm.events.orderRedactions.length; i++) {
                if (vm.events.orderRedactions[i].redaction.id == vm.orderRedaction.ads.id) {
                    swal(
                        'Error',
                        'Order ' + redaction.name + " sudah ada dalam list order",
                        'error'
                    );
                    return false;
                }
            }
            vm.events.orderRedactions.push(vm.orderRedaction);
            vm.orderRedaction = {};
        };

        vm.deleteOrderRedaction = function deleteOrderRedaction(id, idx) {
            OrderRedaction.delete({id: id},
                function (response) {
                    vm.events.orderRedactions.splice(idx, 1);
                }, function (error) {
                    console.log(error);
                });
        };
        //end order ads


        vm.openPublisDate = openPublisDate;
        vm.datePublishOpenStatus = {};
        vm.datePublishOpenStatus.publishDate = false;

        function openPublisDate(date) {
            vm.datePublishOpenStatus[date] = true;
        }

        vm.back = function () {
            $state.go(previousState.name, previousState.params, {reload: previousState.name});
        };

        function init() {
            getAllEventType();
            getOrganizer();
            getAllMerchandise();
            getAllCirculation();
            getAllRedaction();
            getAllAds();
        }

        init();


    }
})();
