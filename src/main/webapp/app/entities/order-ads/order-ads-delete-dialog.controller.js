(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderAdsDeleteController',OrderAdsDeleteController);

    OrderAdsDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderAds'];

    function OrderAdsDeleteController($uibModalInstance, entity, OrderAds) {
        var vm = this;

        vm.orderAds = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderAds.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
