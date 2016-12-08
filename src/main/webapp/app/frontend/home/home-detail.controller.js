(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('HomeDetailController', HomeDetailController);

    HomeDetailController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'ManageEvents', 'entity'];

    function HomeDetailController($scope, Principal, LoginService, $state, ManageEvents, entity) {
        var vm = this;

        vm.event = entity;

        console.log(entity);

    }
})();
