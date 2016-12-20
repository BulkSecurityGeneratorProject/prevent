(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('UserOrganizerDialogController', UserOrganizerDialogController);

    UserOrganizerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserOrganizer'];

    function UserOrganizerDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, UserOrganizer) {
        var vm = this;

        vm.organizer = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.organizer.id !== null) {
                UserOrganizer.updateOrganizer(vm.organizer).then(onSaveSuccess, onSaveError);
            } else {
                UserOrganizer.createOrganizer(vm.organizer).then(onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('preventApp:organizerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
