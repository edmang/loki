package com.easylife.loki.model;

import com.easylife.loki.serializerDeserializer.UserDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@JsonDeserialize(using = UserDeserializer.class)
public class User {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10000);

    private long id = ID_GENERATOR.incrementAndGet();
    private String username;
    private List<Credential> credentials = new ArrayList<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(credentials, user.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, credentials);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", credentials=" + credentials +
                '}';
    }
}
