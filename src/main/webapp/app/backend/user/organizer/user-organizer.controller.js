(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('UserOrganizerController', UserOrganizerController);

    UserOrganizerController.$inject = ['$scope', '$state', 'Organizer', 'OrganizerSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'UserOrganizer'];

    function UserOrganizerController($scope, $state, Organizer, OrganizerSearch, ParseLinks, AlertService, pagingParams, paginationConstants, UserOrganizer) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();

        function loadAll() {
            if (pagingParams.search) {
                UserOrganizer.getOrganizer({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }).then(onSuccess, onError);
            } else {
                UserOrganizer.getOrganizer({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }).then(onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(response) {
                vm.links = ParseLinks.parse(response.headers('link'));
                vm.totalItems = response.headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.organizers = response.data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery) {
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
