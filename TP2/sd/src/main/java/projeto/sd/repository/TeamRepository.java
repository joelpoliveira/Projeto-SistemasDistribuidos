package projeto.sd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto.sd.model.*;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("Select t from Team t where t.name = ?1")
    Team findByName(String name);

    List<Player> findPlayersByName(String name);

    Optional<Team> findById(int teamID);

    @Query("Select p from Player p where p.team.id = ?1")
    List<Player> findPlayersByTeamID(Integer id);
}
