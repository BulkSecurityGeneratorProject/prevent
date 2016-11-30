(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ManageEvents', ManageEvents);

    ManageEvents.$inject = ['$http'];

    function ManageEvents($http) {

        return {
            findEventOrderByCreated: function (id) {
                return $http.get('api/manage/events')
            }
        }
    }
})();
