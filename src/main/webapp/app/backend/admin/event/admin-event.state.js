(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('admin-event', {
                parent: 'entity',
                url: '/admin/event?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/admin/event/admin-events.html',
                        controller: 'AdminEventsController',
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
            .state('admin-event.create', {
                parent: 'entity',
                url: '/admin/event/create',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/admin/event/admin-event-create.html',
                        controller: 'AdminEventCreateController',
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
                            orderCirculations: [],
                            orderAds: [],
                            orderRedactions: []

                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'admin-event',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('admin-event.edit', {
                parent: 'entity',
                url: '/admin/event/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/admin/event/admin-event-create.html',
                        controller: 'AdminEventCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AdminEvent', function ($stateParams, AdminEvent) {
                        return AdminEvent.getOne($stateParams.id).then(function (response) {
                            return response.data;
                        });
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'admin-event',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('admin-event.delete', {
                parent: 'admin-event',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/backend/admin/event/admin-event-delete-dialog.html',
                        controller: 'AdminEventDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['AdminEvent', function (AdminEvent) {
                                return AdminEvent.getOne($stateParams.id).then(function (response) {
                                    return response.data;
                                });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('admin-event', null, {reload: 'admin-event'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('admin-event.detail', {
                parent: 'entity',
                url: '/admin/{id}/event',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/admin/event/admin-event-detail.html',
                        controller: 'AdminEventDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'AdminEvent', function ($stateParams, AdminEvent) {
                        return AdminEvent.getOne($stateParams.id)
                            .then(function (response) {
                                return response.data;
                            });
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'admin-event',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }
})();
