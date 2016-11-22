(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('file-manager', {
                parent: 'entity',
                url: '/file-manager?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FileManagers'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/file-manager/file-managers.html',
                        controller: 'FileManagerController',
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
            .state('file-manager-detail', {
                parent: 'entity',
                url: '/file-manager/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'FileManager'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/file-manager/file-manager-detail.html',
                        controller: 'FileManagerDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'FileManager', function ($stateParams, FileManager) {
                        return FileManager.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'file-manager',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('file-manager-detail.edit', {
                parent: 'file-manager-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/file-manager/file-manager-dialog.html',
                        controller: 'FileManagerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['FileManager', function (FileManager) {
                                return FileManager.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('file-manager.new', {
                parent: 'file-manager',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/file-manager/file-manager-dialog.html',
                        controller: 'FileManagerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    original: null,
                                    name: null,
                                    path: null,
                                    extension: null,
                                    size: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('file-manager', null, {reload: 'file-manager'});
                    }, function () {
                        $state.go('file-manager');
                    });
                }]
            })
            .state('file-manager.edit', {
                parent: 'file-manager',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/file-manager/file-manager-dialog.html',
                        controller: 'FileManagerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['FileManager', function (FileManager) {
                                return FileManager.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('file-manager', null, {reload: 'file-manager'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('file-manager.delete', {
                parent: 'file-manager',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/file-manager/file-manager-delete-dialog.html',
                        controller: 'FileManagerDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['FileManager', function (FileManager) {
                                return FileManager.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('file-manager', null, {reload: 'file-manager'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
