(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('Circulation', Circulation);

    Circulation.$inject = ['$resource'];

    function Circulation ($resource) {
        var resourceUrl =  'api/circulations/:id';

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
