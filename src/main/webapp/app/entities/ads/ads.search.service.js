(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('AdsSearch', AdsSearch);

    AdsSearch.$inject = ['$resource'];

    function AdsSearch($resource) {
        var resourceUrl =  'api/_search/ads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
