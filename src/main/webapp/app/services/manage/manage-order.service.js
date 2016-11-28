(function () {
    'use strict';
    angular
        .module('preventApp')
        .factory('ManageOrder', ManageOrder);

    ManageOrder.$inject = ['$http'];

    function ManageOrder($http) {

        return {
            agreeOrderMerchandise: function (id) {
                return $http.get('api/manage/order-merchandise/' + id + '/agree')
            },
            disagreeOrderMerchandise: function (id) {
                return $http.get('api/manage/order-merchandise/' + id + '/disagree')
            }
        }
    }
})();
