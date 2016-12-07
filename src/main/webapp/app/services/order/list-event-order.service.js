(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ListEventOrder', ListEventOrder);

    ListEventOrder.$inject = ['$http'];

    function ListEventOrder($http) {
        return {
            getOrderMerchandiseByEvent: function (id) {
                return $http.get('api/order-merchandises/' + id + "/event")
            }
        }
    }
})();
