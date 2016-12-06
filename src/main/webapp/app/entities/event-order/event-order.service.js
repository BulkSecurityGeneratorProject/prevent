(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('EventOrder', EventOrder);

    EventOrder.$inject = ['$http'];

    function EventOrder($http) {
        return {
            create: function (data) {
                return $http.post('api/events', data);
            },
            update: function (data) {
                return $http.put('api/events', data);
            }
        }
    }
})();
