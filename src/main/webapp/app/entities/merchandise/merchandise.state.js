(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('merchandise', {
            parent: 'entity',
            url: '/merchandise?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Merchandises'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/merchandise/merchandises.html',
                    controller: 'MerchandiseController',
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
        .state('merchandise-detail', {
            parent: 'entity',
            url: '/merchandise/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Merchandise'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/merchandise/merchandise-detail.html',
                    controller: 'MerchandiseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Merchandise', function($stateParams, Merchandise) {
                    return Merchandise.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'merchandise',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('merchandise-detail.edit', {
            parent: 'merchandise-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/merchandise/merchandise-dialog.html',
                    controller: 'MerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Merchandise', function(Merchandise) {
                            return Merchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('merchandise.new', {
            parent: 'merchandise',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/merchandise/merchandise-dialog.html',
                    controller: 'MerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                price: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('merchandise', null, { reload: 'merchandise' });
                }, function() {
                    $state.go('merchandise');
                });
            }]
        })
        .state('merchandise.edit', {
            parent: 'merchandise',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/merchandise/merchandise-dialog.html',
                    controller: 'MerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Merchandise', function(Merchandise) {
                            return Merchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('merchandise', null, { reload: 'merchandise' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('merchandise.delete', {
            parent: 'merchandise',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/merchandise/merchandise-delete-dialog.html',
                    controller: 'MerchandiseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Merchandise', function(Merchandise) {
                            return Merchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('merchandise', null, { reload: 'merchandise' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
