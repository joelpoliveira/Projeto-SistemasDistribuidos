package projeto.sd.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)    
    private LocalDateTime startTime;

    private int teamAScore;
    private int teamBScore;
    private boolean hasEnded;

    public Game() {

    }

    public Game(String location, LocalDateTime startTime, Team teamA, Team teamB) {
        this.location = location;
        this.startTime = startTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.hasEnded = false;
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

    public int getTeamAScore() {
        return this.teamAScore;
    }

    public void setTeamAScore(int teamAScore) {
        this.teamAScore = teamAScore;
    }

    public int getTeamBScore() {
        return this.teamBScore;
    }

    public void setTeamBScore(int teamBScore) {
        this.teamBScore = teamBScore;
    }

    public boolean getHasEnded() {
        return this.hasEnded;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", location='" + getLocation() + "'" +
                ", teamA='" + getTeamA() + "'" +
                ", teamB='" + getTeamB() + "'" +
                ", startTime='" + getStartTime() + "'" +
                "}";
    }

}
