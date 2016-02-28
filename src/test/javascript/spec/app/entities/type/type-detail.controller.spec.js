'use strict';

describe('Controller Tests', function() {

    describe('Type Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockType, MockEntry;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockType = jasmine.createSpy('MockType');
            MockEntry = jasmine.createSpy('MockEntry');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Type': MockType,
                'Entry': MockEntry
            };
            createController = function() {
                $injector.get('$controller')("TypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jexpensesApp:typeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
