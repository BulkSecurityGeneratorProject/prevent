(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('Ads', Ads);

    Ads.$inject = ['$resource'];

    function Ads ($resource) {
        var resourceUrl =  'api/ads/:id';

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
