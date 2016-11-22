(function() {
    'use strict';

    angular
        .module('preventApp')
        .factory('ContactPersonSearch', ContactPersonSearch);

    ContactPersonSearch.$inject = ['$resource'];

    function ContactPersonSearch($resource) {
        var resourceUrl =  'api/_search/contact-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
