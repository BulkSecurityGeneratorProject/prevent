(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('Organizer', Organizer);

    Organizer.$inject = ['$resource'];

    function Organizer ($resource) {
        var resourceUrl =  'api/organizers/:id';

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
