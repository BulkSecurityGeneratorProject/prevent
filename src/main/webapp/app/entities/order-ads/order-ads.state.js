(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-ads', {
            parent: 'entity',
            url: '/order-ads?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderAds'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-ads/order-ads.html',
                    controller: 'OrderAdsController',
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
        .state('order-ads-detail', {
            parent: 'entity',
            url: '/order-ads/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderAds'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-ads/order-ads-detail.html',
                    controller: 'OrderAdsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OrderAds', function($stateParams, OrderAds) {
                    return OrderAds.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-ads',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-ads-detail.edit', {
            parent: 'order-ads-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-ads/order-ads-dialog.html',
                    controller: 'OrderAdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderAds', function(OrderAds) {
                            return OrderAds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-ads.new', {
            parent: 'order-ads',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-ads/order-ads-dialog.html',
                    controller: 'OrderAdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                title: null,
                                content: null,
                                note: null,
                                accept: false,
                                publishDate: null,
                                total: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-ads', null, { reload: 'order-ads' });
                }, function() {
                    $state.go('order-ads');
                });
            }]
        })
        .state('order-ads.edit', {
            parent: 'order-ads',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-ads/order-ads-dialog.html',
                    controller: 'OrderAdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderAds', function(OrderAds) {
                            return OrderAds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-ads', null, { reload: 'order-ads' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-ads.delete', {
            parent: 'order-ads',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-ads/order-ads-delete-dialog.html',
                    controller: 'OrderAdsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderAds', function(OrderAds) {
                            return OrderAds.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-ads', null, { reload: 'order-ads' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
