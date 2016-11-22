(function() {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact-person', {
            parent: 'entity',
            url: '/contact-person?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ContactPeople'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-person/contact-people.html',
                    controller: 'ContactPersonController',
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
        .state('contact-person-detail', {
            parent: 'entity',
            url: '/contact-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ContactPerson'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-person/contact-person-detail.html',
                    controller: 'ContactPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ContactPerson', function($stateParams, ContactPerson) {
                    return ContactPerson.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contact-person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contact-person-detail.edit', {
            parent: 'contact-person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-person/contact-person-dialog.html',
                    controller: 'ContactPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactPerson', function(ContactPerson) {
                            return ContactPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-person.new', {
            parent: 'contact-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-person/contact-person-dialog.html',
                    controller: 'ContactPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                email: null,
                                phone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contact-person', null, { reload: 'contact-person' });
                }, function() {
                    $state.go('contact-person');
                });
            }]
        })
        .state('contact-person.edit', {
            parent: 'contact-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-person/contact-person-dialog.html',
                    controller: 'ContactPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactPerson', function(ContactPerson) {
                            return ContactPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-person', null, { reload: 'contact-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-person.delete', {
            parent: 'contact-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-person/contact-person-delete-dialog.html',
                    controller: 'ContactPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContactPerson', function(ContactPerson) {
                            return ContactPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-person', null, { reload: 'contact-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
