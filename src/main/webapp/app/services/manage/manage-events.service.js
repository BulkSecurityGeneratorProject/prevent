(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ManageEvents', ManageEvents);

    ManageEvents.$inject = ['$http'];

    function ManageEvents($http) {

        return {
            findEventOrderByCreated: function (page, size, sort, sortBy, query) {
                return $http.get('api/manage/events', {
                    params: {
                        page: page,
                        size: size,
                        sort: sort,
                        sortBy: sortBy,
                        query: query
                    }
                })
            },
            findEventById: function (id) {
                return $http.get('api/manage/events/ ' + id);
            }
        }
    }
})();
