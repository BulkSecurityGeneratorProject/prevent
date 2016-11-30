(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('redaction', {
            parent: 'entity',
            url: '/redaction?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Redactions'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/redaction/redactions.html',
                    controller: 'RedactionController',
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
        .state('redaction-detail', {
            parent: 'entity',
            url: '/redaction/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Redaction'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/redaction/redaction-detail.html',
                    controller: 'RedactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Redaction', function($stateParams, Redaction) {
                    return Redaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'redaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('redaction-detail.edit', {
            parent: 'redaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/redaction/redaction-dialog.html',
                    controller: 'RedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Redaction', function(Redaction) {
                            return Redaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('redaction.new', {
            parent: 'redaction',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/redaction/redaction-dialog.html',
                    controller: 'RedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('redaction', null, { reload: 'redaction' });
                }, function() {
                    $state.go('redaction');
                });
            }]
        })
        .state('redaction.edit', {
            parent: 'redaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/redaction/redaction-dialog.html',
                    controller: 'RedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Redaction', function(Redaction) {
                            return Redaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('redaction', null, { reload: 'redaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('redaction.delete', {
            parent: 'redaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/redaction/redaction-delete-dialog.html',
                    controller: 'RedactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Redaction', function(Redaction) {
                            return Redaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('redaction', null, { reload: 'redaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
