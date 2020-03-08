package com.easylife.loki.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Credential {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10000);

    private transient long id = ID_GENERATOR.incrementAndGet();
    private String login;
    private String password;
    private String description;

    public Credential() {
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSame(Credential credential) {
        return Objects.equals(login, credential.login) &&
                Objects.equals(password, credential.password) &&
                Objects.equals(description, credential.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return id == that.id &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, description);
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
