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
import java.util.NoSuchElementException;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Users
     */
    @GetMapping(value = "/getUser/{username}")
    public ResponseEntity<UserHolder> getUser(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        if (user == null) {
            return new ResponseEntity<>(new UserHolder(null, UserInfo.USER_NOT_EXIST), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserHolder(user, UserInfo.SUCCESS), HttpStatus.OK);
    }

    @PostMapping(value = "/createUser/{username}")
    public ResponseEntity<UserHolder> createUser(@PathVariable("username") String username) {
        if (userService.getUser(username) != null) {
            return new ResponseEntity<>(new UserHolder(null, UserInfo.USER_ALREADY_EXIST), HttpStatus.OK);
        } else {
            userService.createUser(username);
            return new ResponseEntity<>(new UserHolder(new User(username), UserInfo.SUCCESS), HttpStatus.OK);
        }
    }


    /**
     * Credentials
     */
    @GetMapping(value = "/getCredential/{username}")
    public ResponseEntity<Credential[]> getCredential(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        List<Credential> credentialList = user.getCredentials();
        Credential[] credentials = new Credential[credentialList.size()];
        credentialList.toArray(credentials);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    @PostMapping(value = "/addCredential/{username}", consumes = "application/json")
    public ResponseEntity addCredential(@PathVariable("username") String username, @RequestBody Credential credential) {
        userService.addCredential(username, credential);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/deleteCredential/{username}", consumes = "application/json")
    public ResponseEntity<String> deleteCredential(@PathVariable("username") String username, @RequestBody Credential credential) {
        userService.deleteCredential(username, credential);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/updateCredential/{username}", consumes = "application/json")
    public ResponseEntity updateCredential(@PathVariable("username") String username, @RequestBody CredentialHolder credentialHolder) {
        try {
            userService.updateCredential(username, credentialHolder.getOldCredential(), credentialHolder.getNewCredential());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }


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
}
