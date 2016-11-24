(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('EventsSearch', EventsSearch);

    EventsSearch.$inject = ['$resource'];

    function EventsSearch($resource) {
        var resourceUrl =  'api/_search/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
