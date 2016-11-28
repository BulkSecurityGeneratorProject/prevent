(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-merchandise', {
            parent: 'entity',
            url: '/order-merchandise?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderMerchandises'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-merchandise/order-merchandises.html',
                    controller: 'OrderMerchandiseController',
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
        .state('order-merchandise-detail', {
            parent: 'entity',
            url: '/order-merchandise/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderMerchandise'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-merchandise/order-merchandise-detail.html',
                    controller: 'OrderMerchandiseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OrderMerchandise', function($stateParams, OrderMerchandise) {
                    return OrderMerchandise.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-merchandise',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-merchandise-detail.edit', {
            parent: 'order-merchandise-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-merchandise/order-merchandise-dialog.html',
                    controller: 'OrderMerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderMerchandise', function(OrderMerchandise) {
                            return OrderMerchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-merchandise.new', {
            parent: 'order-merchandise',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-merchandise/order-merchandise-dialog.html',
                    controller: 'OrderMerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                accept: false,
                                note: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-merchandise', null, { reload: 'order-merchandise' });
                }, function() {
                    $state.go('order-merchandise');
                });
            }]
        })
        .state('order-merchandise.edit', {
            parent: 'order-merchandise',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-merchandise/order-merchandise-dialog.html',
                    controller: 'OrderMerchandiseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderMerchandise', function(OrderMerchandise) {
                            return OrderMerchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-merchandise', null, { reload: 'order-merchandise' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-merchandise.delete', {
            parent: 'order-merchandise',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-merchandise/order-merchandise-delete-dialog.html',
                    controller: 'OrderMerchandiseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderMerchandise', function(OrderMerchandise) {
                            return OrderMerchandise.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-merchandise', null, { reload: 'order-merchandise' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
