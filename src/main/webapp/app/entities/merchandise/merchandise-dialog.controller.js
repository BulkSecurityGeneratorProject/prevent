(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('MerchandiseDialogController', MerchandiseDialogController);

    MerchandiseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Merchandise'];

    function MerchandiseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Merchandise) {
        var vm = this;

        vm.merchandise = entity;
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
            if (vm.merchandise.id !== null) {
                Merchandise.update(vm.merchandise, onSaveSuccess, onSaveError);
            } else {
                Merchandise.save(vm.merchandise, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:merchandiseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
