(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('backend', {
            abstract: true,
            parent: 'app',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/backend/backend.html'
                },

                'main-content-header@backend': {
                    templateUrl: 'app/backend/header/header.html',
                    controller: 'HeaderController',
                    controllerAs: 'vm'
                },
                'main-content-sidebar@backend': {
                    templateUrl: 'app/backend/sidebar/sidebar.html',
                    controller: 'SidebarController',
                    controllerAs: 'vm'
                },
                'main-content-footer@backend': {
                    templateUrl: 'app/backend/footer/footer.html'
                }
            }
        });
    }
})();
