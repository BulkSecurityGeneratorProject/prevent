(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function HeaderController($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.account = null;
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;


        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        Principal.identity().then(function (account) {
            console.log('account', account);
            vm.account = account;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
