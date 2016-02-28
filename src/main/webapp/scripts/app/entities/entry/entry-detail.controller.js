'use strict';

angular.module('jexpensesApp')
    .controller('EntryDetailController', function ($scope, $rootScope, $stateParams, entity, Entry, Type, User) {
        $scope.entry = entity;
        $scope.load = function (id) {
            Entry.get({id: id}, function(result) {
                $scope.entry = result;
            });
        };
        var unsubscribe = $rootScope.$on('jexpensesApp:entryUpdate', function(event, result) {
            $scope.entry = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
