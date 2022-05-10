package projeto.sd.model;

import java.util.Map;
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

    @Column(name = "name")
    @NotNull
    private String name;

    @NotNull
    @OneToOne
    private Game game;

    @Column(name = "metadata")
    private String metadata;

    public Event() {
    }

    public Event(String name, String metadata) {
        this.name = name;
        this.metadata = metadata;
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

    public String getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

}
