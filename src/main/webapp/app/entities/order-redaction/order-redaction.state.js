(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-redaction', {
            parent: 'entity',
            url: '/order-redaction?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderRedactions'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-redaction/order-redactions.html',
                    controller: 'OrderRedactionController',
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
        .state('order-redaction-detail', {
            parent: 'entity',
            url: '/order-redaction/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'OrderRedaction'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/order-redaction/order-redaction-detail.html',
                    controller: 'OrderRedactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OrderRedaction', function($stateParams, OrderRedaction) {
                    return OrderRedaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-redaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-redaction-detail.edit', {
            parent: 'order-redaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-redaction/order-redaction-dialog.html',
                    controller: 'OrderRedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderRedaction', function(OrderRedaction) {
                            return OrderRedaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-redaction.new', {
            parent: 'order-redaction',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-redaction/order-redaction-dialog.html',
                    controller: 'OrderRedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                title: null,
                                content: null,
                                accept: false,
                                note: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-redaction', null, { reload: 'order-redaction' });
                }, function() {
                    $state.go('order-redaction');
                });
            }]
        })
        .state('order-redaction.edit', {
            parent: 'order-redaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-redaction/order-redaction-dialog.html',
                    controller: 'OrderRedactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderRedaction', function(OrderRedaction) {
                            return OrderRedaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-redaction', null, { reload: 'order-redaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-redaction.delete', {
            parent: 'order-redaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-redaction/order-redaction-delete-dialog.html',
                    controller: 'OrderRedactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderRedaction', function(OrderRedaction) {
                            return OrderRedaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-redaction', null, { reload: 'order-redaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
