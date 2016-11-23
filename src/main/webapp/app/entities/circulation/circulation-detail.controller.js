(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('CirculationDetailController', CirculationDetailController);

    CirculationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Circulation'];

    function CirculationDetailController($scope, $rootScope, $stateParams, previousState, entity, Circulation) {
        var vm = this;

        vm.circulation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:circulationUpdate', function(event, result) {
            vm.circulation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
