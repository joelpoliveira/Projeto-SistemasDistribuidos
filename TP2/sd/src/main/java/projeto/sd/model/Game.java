package projeto.sd.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "location")
    // @NotNull
    private String location;

    @NotNull
    @OneToOne
    private Team teamA;

    @NotNull
    @OneToOne
    private Team teamB;

    @Column(name = "startTime")
    // @NotNull
    private LocalDateTime startTime;

    @Column(name = "endTime")
    // @NotNull
    private LocalDateTime endTime;

    public Game() {

    }

    public Game(String location, LocalDateTime startTime, LocalDateTime endTime, Team teamA, Team teamB) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Team getTeamA() {
        return this.teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return this.teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", location='" + getLocation() + "'" +
                ", teamA='" + getTeamA() + "'" +
                ", teamB='" + getTeamB() + "'" +
                ", startTime='" + getStartTime() + "'" +
                ", endTime='" + getEndTime() + "'" +
                "}";
    }

}
