(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdminEventDetailController', AdminEventDetailController);

    AdminEventDetailController.$inject = ['$state', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EventType', 'Locations'];

    function AdminEventDetailController($state, $scope, $rootScope, $stateParams, previousState, entity, EventType, Locations) {
        var vm = this;

        vm.events = entity;
        vm.previousState = previousState;

        var unsubscribe = $rootScope.$on('preventApp:eventsUpdate', function (event, result) {
            vm.events = result;
        });

        vm.back = function () {
            $state.go(previousState.name, previousState.params, {reload: previousState.name});
        };

        $scope.$on('$destroy', unsubscribe);
    }
})();
