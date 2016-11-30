'use strict';

describe('Controller Tests', function() {

    describe('OrderRedaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrderRedaction, MockRedaction, MockEvents;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrderRedaction = jasmine.createSpy('MockOrderRedaction');
            MockRedaction = jasmine.createSpy('MockRedaction');
            MockEvents = jasmine.createSpy('MockEvents');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OrderRedaction': MockOrderRedaction,
                'Redaction': MockRedaction,
                'Events': MockEvents
            };
            createController = function() {
                $injector.get('$controller')("OrderRedactionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'preventApp:orderRedactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
