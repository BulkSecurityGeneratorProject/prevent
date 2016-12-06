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
            findEventAll: function () {
                return $http.get('api/event-type/search/all');
            },
            findOrganizerAll: function () {
                return $http.get('api/organizer/search/all');
            },

        }
    }
})();
