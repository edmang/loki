'use strict';

var app = angular.module('loki', ['ngRoute']);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('')
    $routeProvider
        .when("/", {
            templateUrl:'views/users.html',
            controller:'userController'
        })
        .when("/users", {
            templateUrl:'views/users.html',
            controller:'userController'
        })
        .otherwise({
            redirectTo: {
                templateUrl: 'views/users.html',
                controller:'userController'
            }
        })
    ;

});
var baseUrl = window.location.protocol + "//" + window.location.host + "/loki/";
