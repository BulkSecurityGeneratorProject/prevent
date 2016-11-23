(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('LocationsDetailController', LocationsDetailController);

    LocationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Locations'];

    function LocationsDetailController($scope, $rootScope, $stateParams, previousState, entity, Locations) {
        var vm = this;

        vm.locations = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:locationsUpdate', function(event, result) {
            vm.locations = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
