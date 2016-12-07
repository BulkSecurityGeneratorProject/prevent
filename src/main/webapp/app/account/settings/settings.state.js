(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('settings', {
                parent: 'account',
                url: '/settings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Settings'
                },
                views: {
                    'content-frontend@frontend': {
                        templateUrl: 'app/account/settings/settings.html',
                        controller: 'SettingsController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('settings-admin', {
                parent: 'admin',
                url: '/settings/admin',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Settings'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/account/settings/settings.html',
                        controller: 'SettingsController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
