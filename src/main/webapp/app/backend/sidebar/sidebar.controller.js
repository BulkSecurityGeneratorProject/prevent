(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        vm.logout = logout;
        vm.$state = $state;

        function logout() {
            Auth.logout();
            $state.go('home');
        }


    }
})();
