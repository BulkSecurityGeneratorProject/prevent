(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('UserEventDeleteController', UserEventDeleteController);

    UserEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserEvent'];

    function UserEventDeleteController($uibModalInstance, entity, UserEvent) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            UserEvent.delete(id)
                .then(function (response) {
                    $uibModalInstance.close(true);
                }, function (error) {
                    console.log(error)
                });
        }
    }
})();
