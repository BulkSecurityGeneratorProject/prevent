(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('EventTypeSearch', EventTypeSearch);

    EventTypeSearch.$inject = ['$resource'];

    function EventTypeSearch($resource) {
        var resourceUrl =  'api/_search/event-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
