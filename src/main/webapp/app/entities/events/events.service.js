(function() {
    'use strict';
    angular
        .module('preventApp')
        .factory('Events', Events);

    Events.$inject = ['$resource', 'DateUtils'];

    function Events ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.starts = DateUtils.convertDateTimeFromServer(data.starts);
                        data.ends = DateUtils.convertDateTimeFromServer(data.ends);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
