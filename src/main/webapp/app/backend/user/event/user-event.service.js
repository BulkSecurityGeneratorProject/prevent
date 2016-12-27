(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('UserEvent', UserEvent);

    UserEvent.$inject = ['$http'];

    function UserEvent($http) {
        return {
            getEvent: function (param) {
                return $http.get('api/user/events', {params: param})
            },
            createEvent: function (data) {
                return $http.post('api/user/events', data);
            },
            updateEvent: function (data) {
                return $http.put('api/user/events', data);
            },
            get: function (id) {
                return $http.get('api/user/events/' + id);
            },
            delete: function (id) {
                return $http.delete('api/user/events/' + id);
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
