(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventTypeDeleteController',EventTypeDeleteController);

    EventTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'EventType'];

    function EventTypeDeleteController($uibModalInstance, entity, EventType) {
        var vm = this;

        vm.eventType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EventType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
