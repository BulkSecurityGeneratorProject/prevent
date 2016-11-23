(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('AdsCategory', AdsCategory);

    AdsCategory.$inject = ['$resource'];

    function AdsCategory ($resource) {
        var resourceUrl =  'api/ads-categories/:id';

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
