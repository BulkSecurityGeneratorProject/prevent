(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'frontend',
                url: '/',
                views: {
                    'content-frontend@frontend': {
                        templateUrl: 'app/frontend/home/home.html',
                        controller: 'HomeController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('home-event-id', {
                parent: 'frontend',
                url: '/events/{id}',
                views: {
                    'content-frontend@frontend': {
                        templateUrl: 'app/frontend/home/home-detail.html',
                        controller: 'HomeDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ManageEvents', function ($stateParams, ManageEvents) {
                        return ManageEvents.findEventById($stateParams.id)
                            .then(function (response) {
                                return response.data;
                            })
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'home',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
    }
})();
