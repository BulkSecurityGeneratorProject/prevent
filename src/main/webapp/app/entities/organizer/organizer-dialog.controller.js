(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrganizerDialogController', OrganizerDialogController);

    OrganizerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organizer', 'ManageSearch'];

    function OrganizerDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Organizer, ManageSearch) {
        var vm = this;

        vm.organizer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = [];

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.organizer.id !== null) {
                Organizer.update(vm.organizer, onSaveSuccess, onSaveError);
            } else {
                Organizer.save(vm.organizer, onSaveSuccess, onSaveError);
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

        function init() {
            ManageSearch.findAllUsers()
                .then(function (response) {
                    vm.users = response.data;
                }, function (error) {
                    console.log(error);
                })
        }

        init();


    }
})();
