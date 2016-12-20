(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('UserOrganizer', UserOrganizer);

    UserOrganizer.$inject = ['$http'];

    function UserOrganizer($http) {
        return {
            getOrganizer: function (param) {
                return $http.get('api/user/organizers', {params: param})
            },
            getAllOrganizer: function () {
                return $http.get('api/user/organizers/list')
            },
            createOrganizer: function (data) {
                return $http.post('api/user/organizers', data);
            },
            updateOrganizer: function (data) {
                return $http.put('api/user/organizers', data);
            },
            deleteOrganizer: function (id) {
                return $http.delete('api/user/organizers/' + id);
            },
            getOneOrganizer: function (id) {
                return $http.get('api/user/organizers/' + id);
            }
            // accept: function (id) {
            //     return $http.get('api/order/event/' + id + "/accept");
            // },
            // reject: function (id) {
            //     return $http.get('api/order/event/' + id + "/reject");
            // }
        }
    }
})();
