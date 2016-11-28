'use strict';

describe('Controller Tests', function() {

    describe('OrderMerchandise Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrderMerchandise, MockMerchandise, MockEvents;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrderMerchandise = jasmine.createSpy('MockOrderMerchandise');
            MockMerchandise = jasmine.createSpy('MockMerchandise');
            MockEvents = jasmine.createSpy('MockEvents');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OrderMerchandise': MockOrderMerchandise,
                'Merchandise': MockMerchandise,
                'Events': MockEvents
            };
            createController = function() {
                $injector.get('$controller')("OrderMerchandiseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'preventApp:orderMerchandiseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
