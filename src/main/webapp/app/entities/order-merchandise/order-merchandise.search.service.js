(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('OrderMerchandiseSearch', OrderMerchandiseSearch);

    OrderMerchandiseSearch.$inject = ['$resource'];

    function OrderMerchandiseSearch($resource) {
        var resourceUrl =  'api/_search/order-merchandises/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
