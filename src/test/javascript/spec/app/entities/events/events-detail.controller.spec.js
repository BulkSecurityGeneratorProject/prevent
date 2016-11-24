'use strict';

describe('Controller Tests', function() {

    describe('Events Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEvents, MockEventType, MockLocations;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEvents = jasmine.createSpy('MockEvents');
            MockEventType = jasmine.createSpy('MockEventType');
            MockLocations = jasmine.createSpy('MockLocations');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Events': MockEvents,
                'EventType': MockEventType,
                'Locations': MockLocations
            };
            createController = function() {
                $injector.get('$controller')("EventsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'preventApp:eventsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
