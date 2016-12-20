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
                return $http.post('api/events', data);
            },
            updateEvent: function (data) {
                return $http.put('api/events', data);
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
