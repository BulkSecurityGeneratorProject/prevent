(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('MerchandiseDeleteController',MerchandiseDeleteController);

    MerchandiseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Merchandise'];

    function MerchandiseDeleteController($uibModalInstance, entity, Merchandise) {
        var vm = this;

        vm.merchandise = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Merchandise.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
