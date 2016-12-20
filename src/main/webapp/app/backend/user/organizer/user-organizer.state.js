(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('user-organizer', {
                parent: 'entity',
                url: '/user/organizer?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Organizers'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/user/organizer/user-organizers.html',
                        controller: 'UserOrganizerController',
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
            // .state('organizer-detail', {
            //     parent: 'entity',
            //     url: '/organizer/{id}',
            //     data: {
            //         authorities: ['ROLE_ADMIN'],
            //         pageTitle: 'Organizer'
            //     },
            //     views: {
            //         'main-content@backend': {
            //             templateUrl: 'app/entities/organizer/organizer-detail.html',
            //             controller: 'OrganizerDetailController',
            //             controllerAs: 'vm'
            //         }
            //     },
            //     resolve: {
            //         entity: ['$stateParams', 'Organizer', function ($stateParams, Organizer) {
            //             return Organizer.get({id: $stateParams.id}).$promise;
            //         }],
            //         previousState: ["$state", function ($state) {
            //             var currentStateData = {
            //                 name: $state.current.name || 'organizer',
            //                 params: $state.params,
            //                 url: $state.href($state.current.name, $state.params)
            //             };
            //             return currentStateData;
            //         }]
            //     }
            // })
            // .state('organizer-detail.edit', {
            //     parent: 'organizer-detail',
            //     url: '/detail/edit',
            //     data: {
            //         authorities: ['ROLE_ADMIN']
            //     },
            //     onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
            //         $uibModal.open({
            //             templateUrl: 'app/entities/organizer/organizer-dialog.html',
            //             controller: 'OrganizerDialogController',
            //             controllerAs: 'vm',
            //             backdrop: 'static',
            //             size: 'lg',
            //             resolve: {
            //                 entity: ['Organizer', function (Organizer) {
            //                     return Organizer.get({id: $stateParams.id}).$promise;
            //                 }]
            //             }
            //         }).result.then(function () {
            //             $state.go('^', {}, {reload: false});
            //         }, function () {
            //             $state.go('^');
            //         });
            //     }]
            // })
            .state('user-organizer.new', {
                parent: 'user-organizer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/backend/user/organizer/user-organizer-dialog.html',
                        controller: 'UserOrganizerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    facebook: null,
                                    twitter: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('user-organizer', null, {reload: 'user-organizer'});
                    }, function () {
                        $state.go('user-organizer');
                    });
                }]
            })
            .state('user-organizer.edit', {
                parent: 'user-organizer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/backend/user/organizer/user-organizer-dialog.html',
                        controller: 'UserOrganizerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['UserOrganizer', function (UserOrganizer) {
                                return UserOrganizer.getOneOrganizer($stateParams.id)
                                    .then(function (response) {
                                        return response.data;
                                    });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('user-organizer', null, {reload: 'user-organizer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('user-organizer.delete', {
                parent: 'user-organizer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/backend/user/organizer/user-organizer-delete-dialog.html',
                        controller: 'UserOrganizerDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['UserOrganizer', function (UserOrganizer) {
                                return UserOrganizer.getOneOrganizer($stateParams.id)
                                    .then(function (response) {
                                        return response.data;
                                    });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('user-organizer', null, {reload: 'user-organizer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
        // .state('organizer.import', {
        //     parent: 'organizer',
        //     url: '/import',
        //     data: {
        //         authorities: ['ROLE_ADMIN']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'app/entities/organizer/organizer-import.html',
        //             controller: 'OrganizerImportController',
        //             controllerAs: 'vm',
        //             backdrop: 'static',
        //             size: 'lg'
        //         }).result.then(function () {
        //             $state.go('organizer', null, {reload: 'organizer'});
        //         }, function () {
        //             $state.go('organizer');
        //         });
        //     }]
        // })
    }

})();
