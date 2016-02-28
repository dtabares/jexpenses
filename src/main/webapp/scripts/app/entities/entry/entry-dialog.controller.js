'use strict';

angular.module('jexpensesApp').controller('EntryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Entry', 'Type', 'User',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Entry, Type, User) {

        $scope.entry = entity;
        $scope.types = Type.query({filter: 'entry-is-null'});
        $q.all([$scope.entry.$promise, $scope.types.$promise]).then(function() {
            if (!$scope.entry.type || !$scope.entry.type.id) {
                return $q.reject();
            }
            return Type.get({id : $scope.entry.type.id}).$promise;
        }).then(function(type) {
            $scope.types.push(type);
        });
        $scope.users = User.query();
        $scope.load = function(id) {
            Entry.get({id : id}, function(result) {
                $scope.entry = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jexpensesApp:entryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.entry.id != null) {
                Entry.update($scope.entry, onSaveSuccess, onSaveError);
            } else {
                Entry.save($scope.entry, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
