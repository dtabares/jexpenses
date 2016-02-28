 'use strict';

angular.module('jexpensesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jexpensesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jexpensesApp-params')});
                }
                return response;
            }
        };
    });
