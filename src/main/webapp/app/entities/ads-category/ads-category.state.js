(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ads-category', {
            parent: 'entity',
            url: '/ads-category?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'AdsCategories'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/ads-category/ads-categories.html',
                    controller: 'AdsCategoryController',
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
        .state('ads-category-detail', {
            parent: 'entity',
            url: '/ads-category/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'AdsCategory'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/ads-category/ads-category-detail.html',
                    controller: 'AdsCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AdsCategory', function($stateParams, AdsCategory) {
                    return AdsCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ads-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ads-category-detail.edit', {
            parent: 'ads-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads-category/ads-category-dialog.html',
                    controller: 'AdsCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdsCategory', function(AdsCategory) {
                            return AdsCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ads-category.new', {
            parent: 'ads-category',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads-category/ads-category-dialog.html',
                    controller: 'AdsCategoryDialogController',
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
                    $state.go('ads-category', null, { reload: 'ads-category' });
                }, function() {
                    $state.go('ads-category');
                });
            }]
        })
        .state('ads-category.edit', {
            parent: 'ads-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads-category/ads-category-dialog.html',
                    controller: 'AdsCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdsCategory', function(AdsCategory) {
                            return AdsCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ads-category', null, { reload: 'ads-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ads-category.delete', {
            parent: 'ads-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ads-category/ads-category-delete-dialog.html',
                    controller: 'AdsCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdsCategory', function(AdsCategory) {
                            return AdsCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ads-category', null, { reload: 'ads-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
