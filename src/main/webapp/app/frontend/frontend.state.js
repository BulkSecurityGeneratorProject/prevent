(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('frontend', {
            abstract: true,
            parent: 'app',
            data: {
                authorities: []
            },
            views: {
                'navbar@frontend': {
                    templateUrl: 'app/frontend/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/frontend/frontend.html'
                },
                'footer@frontend': {
                    templateUrl: 'app/frontend/footer/footer.html'
                }
            }
        });
    }
})();
