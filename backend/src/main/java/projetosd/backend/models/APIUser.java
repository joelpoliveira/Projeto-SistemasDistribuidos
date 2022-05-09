package projetosd.backend.models;

public class APIUser {
    private int id;
    private String username;

    public APIUser() {
    }

    public APIUser(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
