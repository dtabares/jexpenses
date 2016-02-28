'use strict';

angular.module('jexpensesApp')
    .controller('PrefixDetailController', function ($scope, $rootScope, $stateParams, entity, Prefix, User) {
        $scope.prefix = entity;
        $scope.load = function (id) {
            Prefix.get({id: id}, function(result) {
                $scope.prefix = result;
            });
        };
        var unsubscribe = $rootScope.$on('jexpensesApp:prefixUpdate', function(event, result) {
            $scope.prefix = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
