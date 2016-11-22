(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
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

        vm.slides = [];

        vm.myInterval = 5000;
        var currIndex = 0;

        function addSlide() {
            vm.slides.push({
                image: 'http://loremflickr.com/1280/500/beach',
                text: ['Nice image', 'Awesome photograph', 'That is so cool', 'I love that'][vm.slides.length % 4],
                id: currIndex++
            });
        };

        for (var i = 0; i < 4; i++) {
            addSlide();
        }

        addSlide();


    }
})();
