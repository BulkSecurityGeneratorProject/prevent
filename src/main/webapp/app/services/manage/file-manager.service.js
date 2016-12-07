(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('FileManager', FileManager);

    FileManager.$inject = ['$http'];

    function FileManager($http) {

        return {
            deleteFile: function (id) {
                return $http.delete('api/file-managers/' + id)
            }
        }
    }
})();
