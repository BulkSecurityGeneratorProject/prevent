(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('AdsCategorySearch', AdsCategorySearch);

    AdsCategorySearch.$inject = ['$resource'];

    function AdsCategorySearch($resource) {
        var resourceUrl =  'api/_search/ads-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
