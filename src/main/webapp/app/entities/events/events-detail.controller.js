(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventsDetailController', EventsDetailController);

    EventsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Events', 'EventType', 'Locations'];

    function EventsDetailController($scope, $rootScope, $stateParams, previousState, entity, Events, EventType, Locations) {
        var vm = this;

        vm.events = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:eventsUpdate', function(event, result) {
            vm.events = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
