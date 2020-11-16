/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import project.Entities.Account;
import project.Entities.Message;
import project.Entities.Skill;

/**
 *
 * @author uurti
 */
public interface SkillRepository extends JpaRepository<Skill, Long>{
    
    List<Skill> findAllByAccountsId(Long id, Pageable pageable);
    List<Skill> findByAccountsId(Long id, Sort sort);
}
