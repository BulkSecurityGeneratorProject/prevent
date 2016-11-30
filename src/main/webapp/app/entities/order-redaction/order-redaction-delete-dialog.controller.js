(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderRedactionDeleteController',OrderRedactionDeleteController);

    OrderRedactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderRedaction'];

    function OrderRedactionDeleteController($uibModalInstance, entity, OrderRedaction) {
        var vm = this;

        vm.orderRedaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderRedaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
