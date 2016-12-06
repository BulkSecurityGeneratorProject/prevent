(function () {
    'use strict';

    angular
        .module('preventApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ds.objectDiff',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'oitozero.ngSweetAlert',
            'ngFileUpload'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();

    }
})();
