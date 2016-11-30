(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderRedactionDialogController', OrderRedactionDialogController);

    OrderRedactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderRedaction', 'Redaction', 'Events'];

    function OrderRedactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderRedaction, Redaction, Events) {
        var vm = this;

        vm.orderRedaction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.redactions = Redaction.query();
        vm.events = Events.query();

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderRedaction.id !== null) {
                OrderRedaction.update(vm.orderRedaction, onSaveSuccess, onSaveError);
            } else {
                OrderRedaction.save(vm.orderRedaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:orderRedactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
