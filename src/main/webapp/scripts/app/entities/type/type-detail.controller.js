'use strict';

angular.module('jexpensesApp')
    .controller('TypeDetailController', function ($scope, $rootScope, $stateParams, entity, Type, Entry) {
        $scope.type = entity;
        $scope.load = function (id) {
            Type.get({id: id}, function(result) {
                $scope.type = result;
            });
        };
        var unsubscribe = $rootScope.$on('jexpensesApp:typeUpdate', function(event, result) {
            $scope.type = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
