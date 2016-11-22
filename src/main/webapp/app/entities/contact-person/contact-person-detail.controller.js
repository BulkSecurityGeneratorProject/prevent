(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('ContactPersonDetailController', ContactPersonDetailController);

    ContactPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ContactPerson'];

    function ContactPersonDetailController($scope, $rootScope, $stateParams, previousState, entity, ContactPerson) {
        var vm = this;

        vm.contactPerson = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:contactPersonUpdate', function(event, result) {
            vm.contactPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
