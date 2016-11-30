(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('OrderRedactionSearch', OrderRedactionSearch);

    OrderRedactionSearch.$inject = ['$resource'];

    function OrderRedactionSearch($resource) {
        var resourceUrl =  'api/_search/order-redactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
