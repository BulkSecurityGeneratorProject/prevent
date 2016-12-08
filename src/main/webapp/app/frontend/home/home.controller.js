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

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }

        loadAll(0, 12);

        vm.page = 0;
        vm.size = 12;
        vm.query = null;

        function loadAll(page, size) {
            ManageEvents
                .findEventOrderByCreated(page, size, 'DESC', 'starts', vm.query)
                .then(function (response) {
                    vm.totalItems = response.headers('X-Total-Count');
                    vm.events = response.data;
                }, function (error) {
                    console.log(error);
                })
        }

        vm.search = function () {
            ManageEvents
                .findEventOrderByCreated(vm.page, vm.size, 'DESC', 'starts', vm.query)
                .then(function (response) {
                    vm.totalItems = response.headers('X-Total-Count');
                    vm.events = response.data;
                }, function (error) {
                    console.log(error);
                })
        };

        vm.pageChanged = function () {
            if (vm.page == 1) {
                loadAll(0, 12);
            } else {
                loadAll(vm.page, vm.size);
            }
        };


    }
})();
