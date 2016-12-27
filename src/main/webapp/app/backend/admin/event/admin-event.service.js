(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('AdminEvent', AdminEvent);

    AdminEvent.$inject = ['$http'];

    function AdminEvent($http) {
        return {
            create: function (data) {
                return $http.post('api/events', data);
            },
            update: function (data) {
                return $http.put('api/events', data);
            },
            getAll: function (param) {
                return $http.get('api/events', {params: param});
            },
            getOne: function (id) {
                return $http.get('api/events/' + id);
            },
            delete: function (id) {
                return $http.delete('api/events/' + id);
            },
            search: function (param) {
                return $http.get('api/_search/events', {params: param});
            },
            accept: function (id) {
                return $http.get('api/order/event/' + id + "/accept");
            },
            reject: function (id) {
                return $http.get('api/order/event/' + id + "/reject");
            }
        }
    }
})();
