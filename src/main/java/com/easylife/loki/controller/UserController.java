package com.easylife.loki.controller;

import com.easylife.loki.model.Credential;
import com.easylife.loki.model.User;
import com.easylife.loki.model.UserHolder;
import com.easylife.loki.model.UserInfo;
import com.easylife.loki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Users
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<UserHolder> login(@RequestBody LoginHolder loginHolder) {
        User user = userService.getUser(loginHolder.getUsername());
        if (user == null) {
            return new ResponseEntity<>(new UserHolder(null, UserInfo.USER_NOT_EXIST), HttpStatus.OK);

        } else if (user.getCredentials().size() == 0) {
            return new ResponseEntity<>(new UserHolder(user, UserInfo.SUCCESS), HttpStatus.OK);

        } else if (userService.matchPassword(loginHolder.getUsername(), loginHolder.pass)) {
            return new ResponseEntity<>(new UserHolder(user, UserInfo.SUCCESS), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new UserHolder(null, UserInfo.WRONG_PASS), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/createUser/{username}/{complexity}")
    public ResponseEntity<UserHolder> createUser(@PathVariable("username") String username, @PathVariable("complexity") Double complexity) {
        if (userService.getUser(username) != null) {
            return new ResponseEntity<>(new UserHolder(null, UserInfo.USER_ALREADY_EXIST), HttpStatus.OK);
        } else {
            userService.createUser(username, complexity);
            return new ResponseEntity<>(new UserHolder(new User(username), UserInfo.SUCCESS), HttpStatus.OK);
        }
    }


    /**
     * Credentials
     */
    @GetMapping(value = "/getCredential/{username}/{pass}")
    public ResponseEntity<Credential[]> getCredential(@PathVariable("username") String username, @PathVariable("pass") String pass) {

        if (userService.matchPassword(username, pass)) {
            return new ResponseEntity<>(this.getCredentials(username), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addCredential/{username}", consumes = "application/json")
    public ResponseEntity<Credential[]> addCredential(@PathVariable("username") String username, @RequestBody Credential credential) {
        userService.addCredential(username, credential);
        return new ResponseEntity<>(this.getCredentials(username), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteCredential/{username}", consumes = "application/json")
    public ResponseEntity<Credential[]> deleteCredential(@PathVariable("username") String username, @RequestBody Credential credential) {
        userService.deleteCredential(username, credential);
        return new ResponseEntity<>(this.getCredentials(username), HttpStatus.OK);
    }

    @PostMapping(value = "/updateCredential/{username}", consumes = "application/json")
    public ResponseEntity<Credential[]> updateCredential(@PathVariable("username") String username, @RequestBody CredentialHolder credentialHolder) {
        userService.updateCredential(username, credentialHolder.getOldCredential(), credentialHolder.getNewCredential());
        return new ResponseEntity<>(this.getCredentials(username), HttpStatus.OK);
    }


    private Credential[] getCredentials(String username) {
        User user = userService.getUser(username);
        List<Credential> credentialList = user.getCredentials();
        Credential[] credentials = new Credential[credentialList.size()];
        credentialList.toArray(credentials);
        return credentials;
    }

    //
    private static class CredentialHolder {
        private Credential oldCredential;
        private Credential newCredential;

        public CredentialHolder(Credential oldCredential, Credential newCredential) {
            this.oldCredential = oldCredential;
            this.newCredential = newCredential;
        }

        public Credential getOldCredential() {
            return oldCredential;
        }

        public Credential getNewCredential() {
            return newCredential;
        }
    }


    private static class LoginHolder {
        private String username;
        private String pass;

        public LoginHolder(String username, String pass) {
            this.username = username;
            this.pass = pass;
        }

        public String getUsername() {
            return username;
        }

        public String getPass() {
            return pass;
        }
    }
}
