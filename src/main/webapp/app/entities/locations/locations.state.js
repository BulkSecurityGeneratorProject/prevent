(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('locations', {
            parent: 'entity',
            url: '/locations?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Locations'
            },
            views: {
                'main-content@backend': {
                    templateUrl: 'app/entities/locations/locations.html',
                    controller: 'LocationsController',
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
        .state('locations-detail', {
            parent: 'entity',
            url: '/locations/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Locations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/locations/locations-detail.html',
                    controller: 'LocationsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Locations', function($stateParams, Locations) {
                    return Locations.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'locations',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('locations-detail.edit', {
            parent: 'locations-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/locations/locations-dialog.html',
                    controller: 'LocationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Locations', function(Locations) {
                            return Locations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('locations.new', {
            parent: 'locations',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/locations/locations-dialog.html',
                    controller: 'LocationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                city: null,
                                state: null,
                                postalCode: null,
                                latitude: null,
                                longitude: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('locations', null, { reload: 'locations' });
                }, function() {
                    $state.go('locations');
                });
            }]
        })
        .state('locations.edit', {
            parent: 'locations',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/locations/locations-dialog.html',
                    controller: 'LocationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Locations', function(Locations) {
                            return Locations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('locations', null, { reload: 'locations' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('locations.delete', {
            parent: 'locations',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/locations/locations-delete-dialog.html',
                    controller: 'LocationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Locations', function(Locations) {
                            return Locations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('locations', null, { reload: 'locations' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
