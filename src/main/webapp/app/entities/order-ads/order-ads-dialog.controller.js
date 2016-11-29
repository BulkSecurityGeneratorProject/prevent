(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderAdsDialogController', OrderAdsDialogController);

    OrderAdsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderAds', 'Ads', 'Events'];

    function OrderAdsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderAds, Ads, Events) {
        var vm = this;

        vm.orderAds = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.ads = Ads.query();
        vm.events = Events.query();

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderAds.id !== null) {
                OrderAds.update(vm.orderAds, onSaveSuccess, onSaveError);
            } else {
                OrderAds.save(vm.orderAds, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('preventApp:orderAdsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publishDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
