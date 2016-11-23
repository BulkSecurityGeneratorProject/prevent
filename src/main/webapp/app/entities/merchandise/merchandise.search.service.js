(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('MerchandiseSearch', MerchandiseSearch);

    MerchandiseSearch.$inject = ['$resource'];

    function MerchandiseSearch($resource) {
        var resourceUrl =  'api/_search/merchandises/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
