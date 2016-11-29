(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderAdsDetailController', OrderAdsDetailController);

    OrderAdsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderAds', 'Ads', 'Events'];

    function OrderAdsDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderAds, Ads, Events) {
        var vm = this;

        vm.orderAds = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:orderAdsUpdate', function(event, result) {
            vm.orderAds = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
