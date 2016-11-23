(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('RedactionDetailController', RedactionDetailController);

    RedactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Redaction'];

    function RedactionDetailController($scope, $rootScope, $stateParams, previousState, entity, Redaction) {
        var vm = this;

        vm.redaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:redactionUpdate', function(event, result) {
            vm.redaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
