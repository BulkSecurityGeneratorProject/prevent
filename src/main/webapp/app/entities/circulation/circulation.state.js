(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('circulation', {
            parent: 'entity',
            url: '/circulation?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Circulations'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/circulation/circulations.html',
                    controller: 'CirculationController',
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
        .state('circulation-detail', {
            parent: 'entity',
            url: '/circulation/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Circulation'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/circulation/circulation-detail.html',
                    controller: 'CirculationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Circulation', function($stateParams, Circulation) {
                    return Circulation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'circulation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('circulation-detail.edit', {
            parent: 'circulation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/circulation/circulation-dialog.html',
                    controller: 'CirculationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Circulation', function(Circulation) {
                            return Circulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('circulation.new', {
            parent: 'circulation',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/circulation/circulation-dialog.html',
                    controller: 'CirculationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                price: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('circulation', null, { reload: 'circulation' });
                }, function() {
                    $state.go('circulation');
                });
            }]
        })
        .state('circulation.edit', {
            parent: 'circulation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/circulation/circulation-dialog.html',
                    controller: 'CirculationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Circulation', function(Circulation) {
                            return Circulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('circulation', null, { reload: 'circulation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('circulation.delete', {
            parent: 'circulation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/circulation/circulation-delete-dialog.html',
                    controller: 'CirculationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Circulation', function(Circulation) {
                            return Circulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('circulation', null, { reload: 'circulation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
