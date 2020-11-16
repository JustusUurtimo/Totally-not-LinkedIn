/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import project.Entities.Account;
import project.Entities.Skill;
import project.Repositories.SkillRepository;

/**
 *
 * @author uurti
 */
@Service
public class SkillService {
    
    @Autowired
    private SkillRepository skillrepo;

    public List<Skill> findTopSkills(Account acc) {
        //finds top 3 skills
        List<Skill> topSkills = skillrepo.findAllByAccountsId(acc.getId(), PageRequest.of(0, 3, Sort.by(Order.desc("endorsments"), Order.desc("profiency"))));
        return topSkills;
    }

    public List<Skill> otherSkills(Account acc) {
        //rest of the skills
        List<Skill> others = skillrepo.findByAccountsId(acc.getId(), Sort.by(Order.desc("endorsments"), Order.desc("profiency")));
        
        return others.stream().skip(3).collect(Collectors.toList());
    }
}
