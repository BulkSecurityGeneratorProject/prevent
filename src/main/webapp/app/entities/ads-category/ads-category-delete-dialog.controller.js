(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsCategoryDeleteController',AdsCategoryDeleteController);

    AdsCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdsCategory'];

    function AdsCategoryDeleteController($uibModalInstance, entity, AdsCategory) {
        var vm = this;

        vm.adsCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AdsCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
