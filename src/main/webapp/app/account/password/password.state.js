(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('password', {
                parent: 'admin',
                url: '/password',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Password'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/account/password/password.html',
                        controller: 'PasswordController',
                        controllerAs: 'vm'
                    }
                }
            })
    }
})();
