'use strict';

app.controller('userController', ['$scope', '$http', '$window', function($scope, $http, $window) {

    $scope.credentials = [];
    $scope.currentUserExists = false;
    $scope.username;
    $scope.localUserName = "";

    $scope.initPage = function() {
        $scope.credentials = [];
        $scope.currentUserExists = false;
        $scope.localUserName = "";
    }

    /*
     * User
     */
    $scope.createUser = function(username) {
        $http.post(baseUrl + 'createUser/' + username)
            .then(function successCallback(response) {
                if (response.data.userInfo === "USER_ALREADY_EXIST") {
                    window.alert("User " + username + " already exists please create another one")
                    $scope.initPage();
                } else {
                    window.alert("User " + $scope.username + " created successfully")
                    $scope.localUserName = username;
                    $scope.currentUserExists = true;
                    $scope.credentials = [];
                }
            }, function errorCallback(response) {
                console.log("Error while creating user " + username);
                $scope.initPage();
            });
    }

    $scope.findUser = function(username) {
        $http.get(baseUrl + 'getUser/' + username)
            .then(function successCallback(response) {
                if (response.data.userInfo === "USER_NOT_EXIST") {
                    window.alert("User " + username + " does not exist please create it first")
                    $scope.initPage();
                } else {
                    $scope.currentUserExists = true;
                    $scope.getCredential(response.data.user.username);
                    $scope.localUserName = username;
                }
            }, function errorCallback(response) {
                console.log("Error while getting user " + username);
                $scope.initPage();
            });
    }


    /*
     * Credential
     */
    $scope.getCredential = function(username) {
        $http.get(baseUrl + 'getCredential/' + username)
            .then(function successCallback(response) {
                $scope.credentials = response.data;
                console.log($scope.credentials);
            }, function errorCallback(response) {
                console.log("Error while getting credentials for " + username);
                $scope.initPage();
            });
    }

    $scope.addCredential = function(username) {

        let credential = {
            login: $scope.login,
            password: $scope.password,
            description: $scope.description
        };

        let addCredentialRequest = {
            method: 'POST',
            url: baseUrl + 'addCredential/' + username,
            headers: {
              'Content-Type': 'application/json'
            },
            data: credential
        };

        $http(addCredentialRequest)
            .then(function successCallback(response) {
                console.log("new credential added successfully");
                $scope.getCredential(username);
            }, function errorCallback(response) {
                console.log("Error while adding new credentials");
                $scope.initPage();
            });
    }

    $scope.deleteCredential = function(credential) {

        let deleteCredentialRequest = {
            method: 'POST',
            url: baseUrl + 'deleteCredential/' + $scope.localUserName,
            headers: {
                'Content-Type': 'application/json'
            },
            data: credential
        };

        $http(deleteCredentialRequest)
            .then(function successCallback(response) {
                console.log("Credential deleted successfully");
                $scope.getCredential($scope.localUserName);
            }, function errorCallback(response) {
                console.log("Error while deleting credentials");
                $scope.initPage();
            });
    }

    $scope.updateCredential = function(cred, description, login, password) {

        let newCredential = {
            login: cred.login,
            password: cred.password,
            description: cred.description,

        };

        if (description !== undefined) {
            newCredential.description = description;
        }
        if (login !== undefined) {
            newCredential.login = login;
        }
        if (password !== undefined) {
            newCredential.password = password;
        }

        let credentialHolder = {
            oldCredential: cred,
            newCredential: newCredential
        };

        let updateCredentialRequest = {
            method: 'POST',
            url: baseUrl + 'updateCredential/' + $scope.localUserName,
            headers: {
                'Content-Type': 'application/json'
            },
            data: credentialHolder
        };
        console.log(description);
        $http(updateCredentialRequest)
            .then(function successCallback(response) {
                console.log("Credential update successfully");
                $scope.getCredential($scope.localUserName);
            }, function errorCallback(response) {
                console.log("Error while updating credentials");
                $scope.initPage();
            });
    }
}]);