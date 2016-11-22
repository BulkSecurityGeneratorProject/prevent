(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('ContactPersonDeleteController',ContactPersonDeleteController);

    ContactPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContactPerson'];

    function ContactPersonDeleteController($uibModalInstance, entity, ContactPerson) {
        var vm = this;

        vm.contactPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ContactPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
