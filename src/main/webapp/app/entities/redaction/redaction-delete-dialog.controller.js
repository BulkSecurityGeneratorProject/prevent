(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('RedactionDeleteController',RedactionDeleteController);

    RedactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Redaction'];

    function RedactionDeleteController($uibModalInstance, entity, Redaction) {
        var vm = this;

        vm.redaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Redaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
