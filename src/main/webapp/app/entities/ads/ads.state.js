(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ads', {
            parent: 'entity',
            url: '/ads?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Ads'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/ads/ads.html',
                    controller: 'AdsController',
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
        .state('ads-detail', {
            parent: 'entity',
            url: '/ads/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Ads'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/ads/ads-detail.html',
                    controller: 'AdsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Ads', function($stateParams, Ads) {
                    return Ads.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ads',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ads-detail.edit', {
            parent: 'ads-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads/ads-dialog.html',
                    controller: 'AdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ads', function(Ads) {
                            return Ads.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ads.new', {
            parent: 'ads',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads/ads-dialog.html',
                    controller: 'AdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                cols: null,
                                millimeter: null,
                                totalPrice: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ads', null, { reload: 'ads' });
                }, function() {
                    $state.go('ads');
                });
            }]
        })
        .state('ads.edit', {
            parent: 'ads',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads/ads-dialog.html',
                    controller: 'AdsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ads', function(Ads) {
                            return Ads.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ads', null, { reload: 'ads' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ads.delete', {
            parent: 'ads',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads/ads-delete-dialog.html',
                    controller: 'AdsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ads', function(Ads) {
                            return Ads.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ads', null, { reload: 'ads' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
