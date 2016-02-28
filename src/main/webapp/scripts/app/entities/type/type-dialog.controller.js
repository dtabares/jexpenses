'use strict';

angular.module('jexpensesApp').controller('TypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Type', 'Entry',
        function($scope, $stateParams, $uibModalInstance, entity, Type, Entry) {

        $scope.type = entity;
        $scope.entrys = Entry.query();
        $scope.load = function(id) {
            Type.get({id : id}, function(result) {
                $scope.type = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jexpensesApp:typeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.type.id != null) {
                Type.update($scope.type, onSaveSuccess, onSaveError);
            } else {
                Type.save($scope.type, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
