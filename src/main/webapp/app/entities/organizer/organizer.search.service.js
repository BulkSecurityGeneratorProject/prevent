(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('OrganizerSearch', OrganizerSearch);

    OrganizerSearch.$inject = ['$resource'];

    function OrganizerSearch($resource) {
        var resourceUrl =  'api/_search/organizers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
