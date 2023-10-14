package si.ita.services.users.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Users")
public class User {
    @Id
    private String ID;
    @Field(name = "name")
    private String name;
    @Field(name = "surname")
    private String surname;
    @Field(name = "email")
    private String email;
    @Field(name = "age")
    private Integer age;
    @Field(name = "username")
    private String username;
    @Field(name = "password")
    private String password;
    @Field(name = "isAdmin")
    private Boolean isAdmin;

    public User(String name, String surname, String email, Integer age, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.age = age;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public User() {}


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}