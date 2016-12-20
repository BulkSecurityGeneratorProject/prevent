(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('UserOrganizerDeleteController', UserOrganizerDeleteController);

    UserOrganizerDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserOrganizer'];

    function UserOrganizerDeleteController($uibModalInstance, entity, UserOrganizer) {
        var vm = this;

        vm.organizer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            UserOrganizer.deleteOrganizer(id)
                .then(function (response) {
                    $uibModalInstance.close(true);
                })
        }
    }
})();
