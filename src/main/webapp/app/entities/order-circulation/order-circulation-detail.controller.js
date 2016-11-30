(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderCirculationDetailController', OrderCirculationDetailController);

    OrderCirculationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderCirculation', 'Redaction', 'Events'];

    function OrderCirculationDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderCirculation, Redaction, Events) {
        var vm = this;

        vm.orderCirculation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:orderCirculationUpdate', function(event, result) {
            vm.orderCirculation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
