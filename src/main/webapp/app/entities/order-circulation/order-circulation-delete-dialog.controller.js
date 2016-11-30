(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderCirculationDeleteController',OrderCirculationDeleteController);

    OrderCirculationDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderCirculation'];

    function OrderCirculationDeleteController($uibModalInstance, entity, OrderCirculation) {
        var vm = this;

        vm.orderCirculation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderCirculation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
