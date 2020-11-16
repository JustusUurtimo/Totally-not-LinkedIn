package project.Repositories;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import project.Entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByProfileString(String profileString);
    List<Account> findByNameContainingIgnoreCase(String username, Sort sort);
    
    //for testing
    Long countByUsername(String username);
   
}
