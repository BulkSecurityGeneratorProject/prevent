(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsDeleteController',AdsDeleteController);

    AdsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ads'];

    function AdsDeleteController($uibModalInstance, entity, Ads) {
        var vm = this;

        vm.ads = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ads.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
