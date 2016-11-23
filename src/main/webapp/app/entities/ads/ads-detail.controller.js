(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsDetailController', AdsDetailController);

    AdsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ads', 'AdsCategory'];

    function AdsDetailController($scope, $rootScope, $stateParams, previousState, entity, Ads, AdsCategory) {
        var vm = this;

        vm.ads = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:adsUpdate', function(event, result) {
            vm.ads = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
