(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ImageManager', ImageManager);

    ImageManager.$inject = ['$http'];

    function ImageManager($http) {

        return {
            deleteImage: function (id) {
                return $http.delete('api/image-managers/' + id)
            }
        }
    }
})();
