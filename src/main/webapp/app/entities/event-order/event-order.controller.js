(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('EventOrderController', EventOrderController);

    EventOrderController.$inject = ['$scope', '$state', 'Events', 'EventsSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'EventOrder'];

    function EventOrderController($scope, $state, Events, EventsSearch, ParseLinks, AlertService, pagingParams, paginationConstants, EventOrder) {
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
        vm.accept = accept;

        loadAll();

        function loadAll() {
            if (pagingParams.search) {
                EventsSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Events.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.events = data;
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

        function accept(idx, id) {
            EventOrder.accept(id)
                .then(function (response) {
                    vm.events.splice(idx, 1, response.data);
                }, function (error) {
                    swal(
                        'Error',
                        'Gagal accept event ',
                        'error'
                    );
                })
        }

        function reject(idx, id) {
            EventOrder.reject(id)
                .then(function (response) {
                    vm.events.splice(idx, 1, response.data);
                }, function (error) {
                    swal(
                        'Error',
                        'Gagal reject event ',
                        'error'
                    );
                })
        }
    }
})();
