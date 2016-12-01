(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventCreateController', EventOrderController);

    EventOrderController.$inject = ['$scope', '$state', 'Events', 'entity', 'EventType', 'Locations', 'ManageLocations'];

    function EventOrderController($scope, $state, Events, entity, EventType, Locations, ManageLocations) {
        var vm = this;
        vm.events = entity;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.eventtypes = EventType.query();
        vm.locations = Locations.query();
        vm.getLocation = getLocation;
        vm.create = create;

        function getLocation(val) {
            ManageLocations.findByName(val)
                .then(function (response) {
                    console.log(response)
                    // return response.data.results.map(function (item) {
                    //     return item.formatted_address;
                    // });
                },function (error) {
                    console.log(error);
                });
        };

        function create() {
            swal({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then(function () {
                swal(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success'
                )
            })
        }

        vm.datePickerOpenStatus.starts = false;
        vm.datePickerOpenStatus.ends = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();
