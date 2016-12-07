(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ManageLocations', ManageLocations);

    ManageLocations.$inject = ['$http'];

    function ManageLocations($http) {
        return {
            findByName: function (val) {
                return $http.get('api/location/search', {
                    params: {
                        name: val
                    }
                })
            },
            checkLocation: function (data) {
                return $http.post('api/location/check', data);
            }
        }
    }
})();
