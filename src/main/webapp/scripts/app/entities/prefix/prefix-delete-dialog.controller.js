'use strict';

angular.module('jexpensesApp')
	.controller('PrefixDeleteController', function($scope, $uibModalInstance, entity, Prefix) {

        $scope.prefix = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Prefix.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
