package projeto.sd.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", unique = true)
    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @Column(name = "image")
    private String imageUrl;

    @OneToMany(mappedBy = "team")
    private Set<Player> players = new HashSet<Player>();

    private String location;
    private int wins;
    private int losses;
    private int ties;
    private int goals;

    @OneToOne
    private Player bestScorer;

    public Team() {
    }

    public Team(String name, String imageUrl, String location) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGoals() {
        return this.goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return this.ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getNumberGames() {
        return this.wins + this.losses + this.ties;
    }


    public Player getBestScorer() {
        return this.bestScorer;
    }

    public void setBestScorer(Player bestScorer) {
        this.bestScorer = bestScorer;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", imageUrl='" + getImageUrl() + "'" +
                "}";
    }

}
