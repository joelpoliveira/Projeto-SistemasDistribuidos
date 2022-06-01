package projeto.sd.model;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "position")
    @NotNull
    private String position;

    @Column(name = "birthDate")
    // @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date birthDate;

    @OneToOne
    @NotNull
    private Team team;

    private int goals;

    public Player() {

    }

    public Player(String name, String position, Date birthDate, Team team) {
        this.name = name;
        this.position = position;
        this.birthDate = birthDate;
        this.team = team;
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

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getGoals() {
        return this.goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", position='" + getPosition() + "'" +
                ", birthDate='" + getBirthDate() + "'" +
                ", team='" + getTeam() + "'" +
                "}";
    }

}
