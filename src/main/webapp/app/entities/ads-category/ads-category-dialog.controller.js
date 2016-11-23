(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsCategoryDialogController', AdsCategoryDialogController);

    AdsCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdsCategory'];

    function AdsCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AdsCategory) {
        var vm = this;

        vm.adsCategory = entity;
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
            if (vm.adsCategory.id !== null) {
                AdsCategory.update(vm.adsCategory, onSaveSuccess, onSaveError);
            } else {
                AdsCategory.save(vm.adsCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:adsCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
