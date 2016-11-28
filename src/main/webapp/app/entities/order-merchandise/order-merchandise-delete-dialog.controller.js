(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderMerchandiseDeleteController',OrderMerchandiseDeleteController);

    OrderMerchandiseDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderMerchandise'];

    function OrderMerchandiseDeleteController($uibModalInstance, entity, OrderMerchandise) {
        var vm = this;

        vm.orderMerchandise = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderMerchandise.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
