(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrganizerDeleteController',OrganizerDeleteController);

    OrganizerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Organizer'];

    function OrganizerDeleteController($uibModalInstance, entity, Organizer) {
        var vm = this;

        vm.organizer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Organizer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
