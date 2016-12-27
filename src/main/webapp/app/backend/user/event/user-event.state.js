(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('event-user', {
                parent: 'entity',
                url: '/user/event?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/user/event/user-event.html',
                        controller: 'UserEventController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'createdDate,desc',
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
            .state('event-user.create', {
                parent: 'entity',
                url: '/user/event/create',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/user/event/user-event-create.html',
                        controller: 'UserEventCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            title: null,
                            description: null,
                            starts: null,
                            ends: null,
                            subtotal: 0,
                            accept: false,
                            note: null,
                            image: null,
                            file: null,
                            locationName: null,
                            locationAddress: null,
                            locationLatitude: 0,
                            locationLongitude: 0,
                            eventType: null,
                            organizer: null,
                            isOrder: false,
                            id: null,
                            orderMerchandises: [],
                            orderCirculations: [],
                            orderAds: [],
                            orderRedactions: []

                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'event-user',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('user-event-edit', {
                parent: 'entity',
                url: '/user/event/edit/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Events'
                },
                views: {
                    'main-content@backend': {
                        templateUrl: 'app/backend/user/event/user-event-create.html',
                        controller: 'UserEventCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserEvent', 'DateUtils', function ($stateParams, UserEvent, DateUtils) {
                        return UserEvent.get($stateParams.id)
                            .then(function (response) {
                                var data = angular.fromJson(response.data);
                                data.starts = DateUtils.convertDateTimeFromServer(data.starts);
                                data.ends = DateUtils.convertDateTimeFromServer(data.ends);

                                return data;
                            });
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'event-user',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('event-user.delete', {
                parent: 'event-user',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/backend/user/event/user-event-delete-dialog.html',
                        controller: 'UserEventDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['UserEvent', function (UserEvent) {
                                return UserEvent.get($stateParams.id)
                                    .then(function (response) {
                                        return response.data;
                                    });
                            }]
                        }
                    }).result.then(function () {
                        $state.go('event-user', null, {reload: 'event-user'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
    }
})();
