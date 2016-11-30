(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('OrderRedaction', OrderRedaction);

    OrderRedaction.$inject = ['$resource'];

    function OrderRedaction ($resource) {
        var resourceUrl =  'api/order-redactions/:id';

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
