package projeto.sd.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username", unique = true)
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Column(name = "password")
    @NotNull
    @Size(min = 3, max = 50)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    private boolean isActive;
    private String roles;

    public User() {

    }

    public User(MyUserDetails user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = "USER";
        this.isActive = true;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = "USER";
        this.isActive = true;
    }

    public User(String username, String password, String email, String contact) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.roles = "USER";
        this.isActive = true;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", username=" + this.username + "}";
    }
}
