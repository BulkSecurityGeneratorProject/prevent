(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('RedactionDialogController', RedactionDialogController);

    RedactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Redaction'];

    function RedactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Redaction) {
        var vm = this;

        vm.redaction = entity;
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
            if (vm.redaction.id !== null) {
                Redaction.update(vm.redaction, onSaveSuccess, onSaveError);
            } else {
                Redaction.save(vm.redaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:redactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
