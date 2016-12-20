(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ManageSearch', ManageSearch);

    ManageSearch.$inject = ['$http'];

    function ManageSearch($http) {
        return {
            findEventByName: function (val) {
                return $http.get('api/event-type/search', {
                    params: {
                        name: val
                    }
                })
            },
            findEventTypeAll: function () {
                return $http.get('api/list/event-type');
            },
            findOrganizerAll: function () {
                return $http.get('api/list/organizer');
            },
            findAllUsers: function () {
                return $http.get('api/list/user');
            }

        }
    }
})();
