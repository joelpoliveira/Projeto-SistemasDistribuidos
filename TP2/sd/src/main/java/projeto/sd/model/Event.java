package projeto.sd.model;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "event")
    private String name;

    @NotNull
    @OneToOne
    private Game game;

    private LocalDateTime created_at;

    public Event() {
    }

    public Event(String name, Game game) {
        this.name = name;
        this.game = game;
        this.created_at = LocalDateTime.now();
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

    public Game getGameId() {
        return this.game;
    }

    public void setGameId(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public LocalDateTime getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", game='" + getGame() + "'" +
                "}";
    }

}
