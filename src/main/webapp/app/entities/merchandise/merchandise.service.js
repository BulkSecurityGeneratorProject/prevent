(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('Merchandise', Merchandise);

    Merchandise.$inject = ['$resource'];

    function Merchandise ($resource) {
        var resourceUrl =  'api/merchandises/:id';

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
