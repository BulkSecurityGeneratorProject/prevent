(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('OrderCirculation', OrderCirculation);

    OrderCirculation.$inject = ['$resource'];

    function OrderCirculation ($resource) {
        var resourceUrl =  'api/order-circulations/:id';

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
