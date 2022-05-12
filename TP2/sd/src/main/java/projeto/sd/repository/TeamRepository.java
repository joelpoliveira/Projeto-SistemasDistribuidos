package projeto.sd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto.sd.model.*;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByName(String name);
    List<Player> findPlayersByName(String name);
    Team findById(int teamID);
}
