(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsDialogController', AdsDialogController);

    AdsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ads', 'AdsCategory'];

    function AdsDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Ads, AdsCategory) {
        var vm = this;

        vm.ads = entity;
        vm.clear = clear;
        vm.save = save;
        vm.calculate = calculate;
        vm.adscategories = AdsCategory.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.ads.id !== null) {
                Ads.update(vm.ads, onSaveSuccess, onSaveError);
            } else {
                Ads.save(vm.ads, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('preventApp:adsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function calculate() {
            vm.ads.totalPrice = vm.ads.adsCategory.price * vm.ads.cols * vm.ads.millimeter;
        }


    }
})();
