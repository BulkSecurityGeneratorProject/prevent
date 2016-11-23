(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('RedactionSearch', RedactionSearch);

    RedactionSearch.$inject = ['$resource'];

    function RedactionSearch($resource) {
        var resourceUrl =  'api/_search/redactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
