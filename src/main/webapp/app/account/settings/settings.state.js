(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('settings', {
                parent: 'admin',
                url: '/settings',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'Settings'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/account/settings/settings.html',
                        controller: 'SettingsController',
                        controllerAs: 'vm'
                    }
                }
            })
    }
})();
