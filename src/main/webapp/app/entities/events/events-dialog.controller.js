(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventsDialogController', EventsDialogController);

    EventsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Events', 'EventType', 'Locations'];

    function EventsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Events, EventType, Locations) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.eventtypes = EventType.query();
        vm.locations = Locations.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.events.id !== null) {
                Events.update(vm.events, onSaveSuccess, onSaveError);
            } else {
                Events.save(vm.events, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:eventsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.starts = false;
        vm.datePickerOpenStatus.ends = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
