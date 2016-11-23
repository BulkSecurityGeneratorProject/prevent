(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrganizerDetailController', OrganizerDetailController);

    OrganizerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Organizer', 'User'];

    function OrganizerDetailController($scope, $rootScope, $stateParams, previousState, entity, Organizer, User) {
        var vm = this;

        vm.organizer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:organizerUpdate', function(event, result) {
            vm.organizer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
