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
            'ngFileUpload',
            'geolocation',
            'uiGmapgoogle-maps'
        ])
        .config(mapConfig)
        .run(run);

    run.$inject = ['stateHandler'];
    mapConfig.$inject = ['uiGmapGoogleMapApiProvider'];

    function run(stateHandler) {
        stateHandler.initialize();
    }

    function mapConfig(uiGmapGoogleMapApiProvider) {
        uiGmapGoogleMapApiProvider.configure({
            key: 'AIzaSyAVnX6rmtftZPWezX0dr9V1Q6T74uvUs4o',
            v: '3.20', //defaults to latest 3.X anyhow
            libraries: 'weather,geometry,visualization'
        });
    }
})();
