(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('FileManagerDialogController', FileManagerDialogController);

    FileManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FileManager', 'User'];

    function FileManagerDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, FileManager, User) {
        var vm = this;

        vm.fileManager = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.fileManager.id !== null) {
                FileManager.update(vm.fileManager, onSaveSuccess, onSaveError);
            } else {
                FileManager.save(vm.fileManager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('preventApp:fileManagerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
