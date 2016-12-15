(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('UserEventDeleteController', UserEventDeleteController);

    UserEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'Events'];

    function UserEventDeleteController($uibModalInstance, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Events.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
