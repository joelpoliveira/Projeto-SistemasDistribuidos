package projeto.sd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto.sd.model.*;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>  {
    @Query("Select e from Event e where e.game.id = ?1")
    List<Event> findByGameId(Integer id);
}   
