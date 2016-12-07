(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('password', {
                parent: 'account',
                url: '/password',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Password'
                },
                views: {
                    'content-frontend@frontend': {
                        templateUrl: 'app/account/password/password.html',
                        controller: 'PasswordController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('password-admin', {
                parent: 'admin',
                url: '/password',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Password'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/account/password/password.html',
                        controller: 'PasswordController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
