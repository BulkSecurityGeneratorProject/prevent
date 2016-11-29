'use strict';

describe('Controller Tests', function() {

    describe('OrderAds Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrderAds, MockAds, MockEvents;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrderAds = jasmine.createSpy('MockOrderAds');
            MockAds = jasmine.createSpy('MockAds');
            MockEvents = jasmine.createSpy('MockEvents');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OrderAds': MockOrderAds,
                'Ads': MockAds,
                'Events': MockEvents
            };
            createController = function() {
                $injector.get('$controller')("OrderAdsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'preventApp:orderAdsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
