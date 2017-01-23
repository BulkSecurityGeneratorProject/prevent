(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('AdminEventsController', AdminEventsController);

    AdminEventsController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'AdminEvent','DateUtils'];

    function AdminEventsController($scope, $state, ParseLinks, AlertService, pagingParams, paginationConstants, AdminEvent,DateUtils) {
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
        vm.reject = reject;

        loadAll();

        var starts = DateUtils.convertLocalDateTimeToServer(vm.starts);
        var ends = DateUtils.convertLocalDateTimeToServer(vm.ends);


        function loadAll() {
            if (pagingParams.search) {
                AdminEvent.search({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }).then(onSuccess, onError);
            } else {
                AdminEvent.getAll({
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
                vm.events = response.data;
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
            AdminEvent.accept(id)
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
            AdminEvent.reject(id)
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
