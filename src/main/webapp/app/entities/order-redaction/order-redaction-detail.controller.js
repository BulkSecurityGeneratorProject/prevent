(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrderRedactionDetailController', OrderRedactionDetailController);

    OrderRedactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderRedaction', 'Redaction', 'Events'];

    function OrderRedactionDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderRedaction, Redaction, Events) {
        var vm = this;

        vm.orderRedaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:orderRedactionUpdate', function(event, result) {
            vm.orderRedaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
