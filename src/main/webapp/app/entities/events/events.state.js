(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('events', {
            parent: 'entity',
            url: '/events?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Events'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/events/events.html',
                    controller: 'EventsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
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
        .state('events-detail', {
            parent: 'entity',
            url: '/events/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Events'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/events/events-detail.html',
                    controller: 'EventsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Events', function($stateParams, Events) {
                    return Events.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'events',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('events-detail.edit', {
            parent: 'events-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/events/events-dialog.html',
                    controller: 'EventsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Events', function(Events) {
                            return Events.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('events.new', {
            parent: 'events',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/events/events-dialog.html',
                    controller: 'EventsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                starts: null,
                                ends: null,
                                subtotal: null,
                                accept: false,
                                note: null,
                                isOrder: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('events', null, { reload: 'events' });
                }, function() {
                    $state.go('events');
                });
            }]
        })
        .state('events.edit', {
            parent: 'events',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/events/events-dialog.html',
                    controller: 'EventsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Events', function(Events) {
                            return Events.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('events', null, { reload: 'events' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('events.delete', {
            parent: 'events',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/events/events-delete-dialog.html',
                    controller: 'EventsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Events', function(Events) {
                            return Events.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('events', null, { reload: 'events' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
