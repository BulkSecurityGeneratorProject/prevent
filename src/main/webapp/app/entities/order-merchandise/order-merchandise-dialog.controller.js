(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderMerchandiseDialogController', OrderMerchandiseDialogController);

    OrderMerchandiseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderMerchandise', 'Merchandise', 'Events'];

    function OrderMerchandiseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderMerchandise, Merchandise, Events) {
        var vm = this;

        vm.orderMerchandise = entity;
        vm.clear = clear;
        vm.save = save;
        vm.merchandises = Merchandise.query();
        vm.events = Events.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderMerchandise.id !== null) {
                OrderMerchandise.update(vm.orderMerchandise, onSaveSuccess, onSaveError);
            } else {
                OrderMerchandise.save(vm.orderMerchandise, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:orderMerchandiseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
