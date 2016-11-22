(function () {
    'use strict';

    angular
        .module('preventApp')
        .factory('FileManagerSearch', FileManagerSearch);

    FileManagerSearch.$inject = ['$resource'];

    function FileManagerSearch($resource) {
        var resourceUrl = 'api/_search/file-managers/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
