(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('CirculationDialogController', CirculationDialogController);

    CirculationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Circulation'];

    function CirculationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Circulation) {
        var vm = this;

        vm.circulation = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.circulation.id !== null) {
                Circulation.update(vm.circulation, onSaveSuccess, onSaveError);
            } else {
                Circulation.save(vm.circulation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:circulationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
