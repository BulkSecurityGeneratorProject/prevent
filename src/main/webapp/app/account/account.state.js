(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('account', {
            abstract: true,
            parent: 'frontend',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_USER']
            }
        });
    }
})();
