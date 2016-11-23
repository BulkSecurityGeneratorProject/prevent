(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event-type', {
            parent: 'entity',
            url: '/event-type?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'EventTypes'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/event-type/event-types.html',
                    controller: 'EventTypeController',
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
        .state('event-type-detail', {
            parent: 'entity',
            url: '/event-type/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'EventType'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/event-type/event-type-detail.html',
                    controller: 'EventTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'EventType', function($stateParams, EventType) {
                    return EventType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'event-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('event-type-detail.edit', {
            parent: 'event-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-type/event-type-dialog.html',
                    controller: 'EventTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventType', function(EventType) {
                            return EventType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-type.new', {
            parent: 'event-type',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-type/event-type-dialog.html',
                    controller: 'EventTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('event-type', null, { reload: 'event-type' });
                }, function() {
                    $state.go('event-type');
                });
            }]
        })
        .state('event-type.edit', {
            parent: 'event-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-type/event-type-dialog.html',
                    controller: 'EventTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventType', function(EventType) {
                            return EventType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-type', null, { reload: 'event-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-type.delete', {
            parent: 'event-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-type/event-type-delete-dialog.html',
                    controller: 'EventTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EventType', function(EventType) {
                            return EventType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-type', null, { reload: 'event-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
