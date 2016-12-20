(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdminEventDeleteController', AdminEventDeleteController);

    AdminEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'Events'];

    function AdminEventDeleteController($uibModalInstance, entity, Events) {
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
