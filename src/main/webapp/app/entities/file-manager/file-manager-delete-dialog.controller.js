(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('FileManagerDeleteController', FileManagerDeleteController);

    FileManagerDeleteController.$inject = ['$uibModalInstance', 'entity', 'FileManager'];

    function FileManagerDeleteController($uibModalInstance, entity, FileManager) {
        var vm = this;

        vm.fileManager = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            FileManager.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
