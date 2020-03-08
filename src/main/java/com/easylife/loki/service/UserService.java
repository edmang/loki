package com.easylife.loki.service;

import com.easylife.loki.model.Credential;
import com.easylife.loki.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    Map<String, User> map = new HashMap<>();

    public UserService() {
    }

    public User getUser(String username) {
        return map.get(username);
    }

    public User createUser(String username) {
        User user = new User(username);
        map.put(username, user);
        return user;
    }

    public void addCredential(String username, Credential credential) {
        User user = map.get(username);
        List<Credential> credentials = new ArrayList<>(user.getCredentials());
        credentials.add(credential);
        user.setCredentials(credentials);
    }

    public void deleteCredential(String username, Credential other) {
        User user = map.get(username);
        List<Credential> remains = this.filterCredential(user.getCredentials(), other, true);
        user.setCredentials(remains);
    }

    public void updateCredential(String username, Credential oldCred, Credential newCred) throws NoSuchElementException {
        User user = map.get(username);
        List<Credential> remains = this.filterCredential(user.getCredentials(), oldCred, true);
        if (remains.isEmpty()) {
            throw new NoSuchElementException(username + " does not have " + oldCred);
        }
        remains.add(newCred);
        user.setCredentials(remains);
    }

    private List<Credential> filterCredential(List<Credential> credentials, Credential credential, boolean filterOut) {
        if (filterOut) {
            return credentials.stream().filter(cred -> !cred.isSame(credential)).collect(Collectors.toList());
        } else {
            return credentials.stream().filter(cred -> cred.isSame(credential)).collect(Collectors.toList());
        }
    }


    @PostConstruct
    public void bootstrapUser() {
        User admin = new User();
        admin.setUsername("admin");
        Credential credential = new Credential();
        credential.setDescription("Gmail");
        credential.setLogin("edmond.yizhou.wang@gmail.com");
        credential.setPassword("blablabla");

        Credential credential2 = new Credential();
        credential2.setDescription("Kraken");
        credential2.setLogin("eywang");
        credential2.setPassword("showMeTheMoney");

        Credential credential3 = new Credential();
        credential3.setDescription("SGCIB");
        credential3.setLogin("eywang031119");
        credential3.setPassword("0311119");

        admin.setCredentials(new ArrayList<>(Arrays.asList(credential, credential2, credential3)));

        map.put(admin.getUsername(), admin);
        System.out.println("Bootstrap successfully with " + map.values());
    }
}
