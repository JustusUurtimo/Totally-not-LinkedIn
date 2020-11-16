package project.Repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.Entities.Account;
import project.Entities.Message;
import project.Entities.Skill;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @EntityGraph(attributePaths = {"sender", "comments"})
    List<Message> findAllBySenderIdIn(List<Long> ids, Pageable pageable);
}
