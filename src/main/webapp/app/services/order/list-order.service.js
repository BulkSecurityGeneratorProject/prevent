(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ListOrder', ListOrder);

    ListOrder.$inject = ['$http'];

    function ListOrder($http) {
        return {
            getListMerchandise: function () {
                return $http.get('api/list/merchandise')
            },
            getListCirculation: function () {
                return $http.get('api/list/circulation')
            },
            getListAds: function () {
                return $http.get('api/list/ads')
            },
            getListRedaction: function () {
                return $http.get('api/list/redaction')
            }
        }
    }
})();
