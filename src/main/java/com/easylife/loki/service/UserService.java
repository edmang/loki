package com.easylife.loki.service;

import com.easylife.loki.model.Credential;
import com.easylife.loki.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {

    Map<String, User> map = new ConcurrentHashMap<>();

    public UserService() {
    }

    public boolean matchPassword(String username, String pass) {
        User user = map.get(username);
        List<Credential> credentials = user.getCredentials();
        List<Double> similarity = credentials.stream().map(cred -> computeSimilarity(cred.getPassword(), pass)).collect(Collectors.toList());
        return similarity.stream().filter(percent -> percent >= user.getPasswordComplexity()).count() > 0;
    }

    public User getUser(String username) {
        return map.get(username);
    }

    public User createUser(String username, Double complexity) {
        User user = new User(username, complexity);
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

    public void updateCredential(String username, Credential oldCred, Credential newCred) {
        User user = map.get(username);
        List<Credential> remains = this.filterCredential(user.getCredentials(), oldCred, true);
        remains.add(newCred);
        user.setCredentials(remains);
    }


    //
    private List<Credential> filterCredential(List<Credential> credentials, Credential credential, boolean filterOut) {
        if (filterOut) {
            return credentials.stream().filter(cred -> !cred.isSame(credential)).collect(Collectors.toList());
        } else {
            return credentials.stream().filter(cred -> cred.isSame(credential)).collect(Collectors.toList());
        }
    }

    private static Double computeSimilarity(String password, String similar) {
        Double result = password.contains(similar) ? ((double) similar.length() / password.length()) : (0d);
        return result;
    }


    @PostConstruct
    public void bootstrapUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordComplexity(0.5);
        Credential credential = new Credential();
        credential.setDescription("Gmail");
        credential.setLogin("edmond.yizhou.wang@gmail.com");
        credential.setPassword("blablabla");

        Credential credential2 = new Credential();
        credential2.setDescription("Kraken");
        credential2.setLogin("eywang");
        credential2.setPassword("showMeTheMoney");

        Credential credential3 = new Credential();
        credential3.setDescription("maBoite");
        credential3.setLogin("eywang");
        credential3.setPassword("seemsForbidden");

        admin.setCredentials(new ArrayList<>(Arrays.asList(credential, credential2, credential3)));

        map.put(admin.getUsername(), admin);
        System.out.println("Bootstrap successfully with " + map.values());
    }
}
!