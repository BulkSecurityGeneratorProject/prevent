(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('OrderCirculationSearch', OrderCirculationSearch);

    OrderCirculationSearch.$inject = ['$resource'];

    function OrderCirculationSearch($resource) {
        var resourceUrl =  'api/_search/order-circulations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
