(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('OrderAdsSearch', OrderAdsSearch);

    OrderAdsSearch.$inject = ['$resource'];

    function OrderAdsSearch($resource) {
        var resourceUrl =  'api/_search/order-ads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
