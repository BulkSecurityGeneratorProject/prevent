(function () {
    'use strict';

    angular
        .module('preventApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('admin', {
            abstract: true,
            parent: 'dashboard'
        });
    }
})();
