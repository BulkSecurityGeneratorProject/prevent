(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('LocationsSearch', LocationsSearch);

    LocationsSearch.$inject = ['$resource'];

    function LocationsSearch($resource) {
        var resourceUrl =  'api/_search/locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
