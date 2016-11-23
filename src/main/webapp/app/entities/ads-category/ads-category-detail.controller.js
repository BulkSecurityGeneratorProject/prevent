(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdsCategoryDetailController', AdsCategoryDetailController);

    AdsCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AdsCategory'];

    function AdsCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, AdsCategory) {
        var vm = this;

        vm.adsCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:adsCategoryUpdate', function(event, result) {
            vm.adsCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
