(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('ContactPerson', ContactPerson);

    ContactPerson.$inject = ['$resource'];

    function ContactPerson ($resource) {
        var resourceUrl =  'api/contact-people/:id';

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
