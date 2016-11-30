(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('order-circulation', {
                parent: 'entity',
                url: '/order-circulation?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Order Sirkulasi'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/entities/order-circulation/order-circulations.html',
                        controller: 'OrderCirculationController',
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
            .state('order-circulation-detail', {
                parent: 'entity',
                url: '/order-circulation/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Order Sirkulasi'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/entities/order-circulation/order-circulation-detail.html',
                        controller: 'OrderCirculationDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'OrderCirculation', function ($stateParams, OrderCirculation) {
                        return OrderCirculation.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'order-circulation',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('order-circulation-detail.edit', {
                parent: 'order-circulation-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/order-circulation/order-circulation-dialog.html',
                        controller: 'OrderCirculationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['OrderCirculation', function (OrderCirculation) {
                                return OrderCirculation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('order-circulation.new', {
                parent: 'order-circulation',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/order-circulation/order-circulation-dialog.html',
                        controller: 'OrderCirculationDialogController',
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
                    }).result.then(function () {
                        $state.go('order-circulation', null, {reload: 'order-circulation'});
                    }, function () {
                        $state.go('order-circulation');
                    });
                }]
            })
            .state('order-circulation.edit', {
                parent: 'order-circulation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/order-circulation/order-circulation-dialog.html',
                        controller: 'OrderCirculationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['OrderCirculation', function (OrderCirculation) {
                                return OrderCirculation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('order-circulation', null, {reload: 'order-circulation'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('order-circulation.delete', {
                parent: 'order-circulation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/order-circulation/order-circulation-delete-dialog.html',
                        controller: 'OrderCirculationDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['OrderCirculation', function (OrderCirculation) {
                                return OrderCirculation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('order-circulation', null, {reload: 'order-circulation'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
