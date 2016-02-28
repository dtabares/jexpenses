'use strict';

angular.module('jexpensesApp').controller('PrefixDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Prefix', 'User',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Prefix, User) {

        $scope.prefix = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Prefix.get({id : id}, function(result) {
                $scope.prefix = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jexpensesApp:prefixUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.prefix.id != null) {
                Prefix.update($scope.prefix, onSaveSuccess, onSaveError);
            } else {
                Prefix.save($scope.prefix, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
