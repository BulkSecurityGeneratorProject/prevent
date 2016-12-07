(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderCirculationDialogController', OrderCirculationDialogController);

    OrderCirculationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderCirculation', 'Circulation', 'Events'];

    function OrderCirculationDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderCirculation, Circulation, Events) {
        var vm = this;

        vm.orderCirculation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.circulations = Circulation.query();
        vm.events = Events.query();

        $timeout(function () {
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.orderCirculation.id !== null) {
                OrderCirculation.update(vm.orderCirculation, onSaveSuccess, onSaveError);
            } else {
                OrderCirculation.save(vm.orderCirculation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('preventApp:orderCirculationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
