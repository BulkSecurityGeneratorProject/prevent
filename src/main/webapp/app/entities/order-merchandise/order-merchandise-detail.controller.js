(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderMerchandiseDetailController', OrderMerchandiseDetailController);

    OrderMerchandiseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderMerchandise', 'Merchandise', 'Events'];

    function OrderMerchandiseDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderMerchandise, Merchandise, Events) {
        var vm = this;

        vm.orderMerchandise = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:orderMerchandiseUpdate', function(event, result) {
            vm.orderMerchandise = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
