'use strict';

angular.module('jexpensesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


