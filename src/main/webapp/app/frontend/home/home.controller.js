(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'ManageEvents'];

    function HomeController($scope, Principal, LoginService, $state, ManageEvents) {
        var vm = this;
        vm.events = [];
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.loadAll = loadAll;
        vm.register = register;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();
        loadAll();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }

        function loadAll() {
            ManageEvents
                .findEventOrderByCreated()
                .then(function (response) {
                    vm.events = response.data;
                }, function (error) {
                    console.log(error);
                })
        }


    }
})();
