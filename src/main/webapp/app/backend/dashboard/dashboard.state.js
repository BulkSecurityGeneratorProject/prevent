(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dashboard', {
            parent: 'backend',
            url: '/dashboard',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/backend/dashboard/dashboard.html'
                }
            }
        });
    }
})();
