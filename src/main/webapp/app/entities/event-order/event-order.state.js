(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('event', {
                parent: 'entity',
                url: '/event?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/entities/event-order/event-order.html',
                        controller: 'EventOrderController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'createdDate,desc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                }
            })
            .state('event.create', {
                parent: 'entity',
                url: '/event/create',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/entities/event-order/event-order-create.html',
                        controller: 'EventCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            title: null,
                            description: null,
                            starts: null,
                            ends: null,
                            subtotal: 0,
                            accept: false,
                            note: null,
                            image: null,
                            file: null,
                            locationName: null,
                            locationAddress: null,
                            locationLatitude: 0,
                            locationLongitude: 0,
                            eventType: null,
                            organizer: null,
                            isOrder: false,
                            id: null,
                            orderMerchandises: [],
                            orderCirculations: []

                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'event',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('event-edit', {
                parent: 'entity',
                url: '/event/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/entities/event-order/event-order-create.html',
                        controller: 'EventCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Events', function ($stateParams, Events) {
                        return Events.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'event',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('event.delete', {
                parent: 'event',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/events/events-delete-dialog.html',
                        controller: 'EventsDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Events', function (Events) {
                                return Events.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('event', null, {reload: 'event'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }
})();
