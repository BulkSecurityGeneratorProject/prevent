(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('CirculationDeleteController',CirculationDeleteController);

    CirculationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Circulation'];

    function CirculationDeleteController($uibModalInstance, entity, Circulation) {
        var vm = this;

        vm.circulation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Circulation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
