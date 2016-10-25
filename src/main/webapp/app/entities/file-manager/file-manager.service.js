(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('FileManager', FileManager);

    FileManager.$inject = ['$resource'];

    function FileManager ($resource) {
        var resourceUrl =  'api/file-managers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
