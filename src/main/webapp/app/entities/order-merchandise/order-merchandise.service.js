(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('OrderMerchandise', OrderMerchandise);

    OrderMerchandise.$inject = ['$resource'];

    function OrderMerchandise ($resource) {
        var resourceUrl =  'api/order-merchandises/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
