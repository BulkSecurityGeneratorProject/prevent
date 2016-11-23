(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('CirculationSearch', CirculationSearch);

    CirculationSearch.$inject = ['$resource'];

    function CirculationSearch($resource) {
        var resourceUrl =  'api/_search/circulations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
