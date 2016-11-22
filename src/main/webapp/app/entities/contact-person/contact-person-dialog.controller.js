(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('ContactPersonDialogController', ContactPersonDialogController);

    ContactPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContactPerson'];

    function ContactPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ContactPerson) {
        var vm = this;

        vm.contactPerson = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contactPerson.id !== null) {
                ContactPerson.update(vm.contactPerson, onSaveSuccess, onSaveError);
            } else {
                ContactPerson.save(vm.contactPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:contactPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
