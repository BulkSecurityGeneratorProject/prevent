'use strict';

describe('Controller Tests', function() {

    describe('OrderCirculation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrderCirculation, MockRedaction, MockEvents;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrderCirculation = jasmine.createSpy('MockOrderCirculation');
            MockRedaction = jasmine.createSpy('MockRedaction');
            MockEvents = jasmine.createSpy('MockEvents');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OrderCirculation': MockOrderCirculation,
                'Redaction': MockRedaction,
                'Events': MockEvents
            };
            createController = function() {
                $injector.get('$controller')("OrderCirculationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'preventApp:orderCirculationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
