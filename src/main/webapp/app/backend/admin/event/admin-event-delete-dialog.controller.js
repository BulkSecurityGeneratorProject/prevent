(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdminEventDeleteController', AdminEventDeleteController);

    AdminEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdminEvent'];

    function AdminEventDeleteController($uibModalInstance, entity, AdminEvent) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            AdminEvent.delete(id)
                .then(function (response) {
                    $uibModalInstance.close(true);
                }, function (error) {
                    console.log(error)
                });
        }
    }
})();
