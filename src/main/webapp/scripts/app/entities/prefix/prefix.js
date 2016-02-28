'use strict';

angular.module('jexpensesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prefix', {
                parent: 'entity',
                url: '/prefixs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jexpensesApp.prefix.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefix/prefixs.html',
                        controller: 'PrefixController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefix');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prefix.detail', {
                parent: 'entity',
                url: '/prefix/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jexpensesApp.prefix.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prefix/prefix-detail.html',
                        controller: 'PrefixDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prefix');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Prefix', function($stateParams, Prefix) {
                        return Prefix.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prefix.new', {
                parent: 'prefix',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prefix/prefix-dialog.html',
                        controller: 'PrefixDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    prefix: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prefix', null, { reload: true });
                    }, function() {
                        $state.go('prefix');
                    })
                }]
            })
            .state('prefix.edit', {
                parent: 'prefix',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prefix/prefix-dialog.html',
                        controller: 'PrefixDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Prefix', function(Prefix) {
                                return Prefix.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prefix', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('prefix.delete', {
                parent: 'prefix',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/prefix/prefix-delete-dialog.html',
                        controller: 'PrefixDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Prefix', function(Prefix) {
                                return Prefix.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prefix', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
