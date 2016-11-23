(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('MerchandiseDetailController', MerchandiseDetailController);

    MerchandiseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Merchandise'];

    function MerchandiseDetailController($scope, $rootScope, $stateParams, previousState, entity, Merchandise) {
        var vm = this;

        vm.merchandise = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:merchandiseUpdate', function(event, result) {
            vm.merchandise = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
