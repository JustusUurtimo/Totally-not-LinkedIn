/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.Entities.Account;
import project.Entities.Skill;
import project.Repositories.AccountRepository;

/**
 *
 * @author uurti
 */
@Controller
public class SkillsController {

    @Autowired
    private AccountRepository accountRepo;

    private Account loggedIn;

    @GetMapping("/addSkills")
    public String addSkilsPage(Principal principal, Model model) {
        loggedIn = accountRepo.findByUsername(principal.getName());
        model.addAttribute("accountName", loggedIn.getName());
        model.addAttribute("skills", loggedIn.getSkills());
        return "addSkills";
    }

    @PostMapping("/addSkills")
    public String addSkills(@Valid @ModelAttribute Skill skill, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/addSkills";
        }
        List<Skill> skills = loggedIn.getSkills();
        skill.setEndorsments(0);
        skill.setEndorsers(new ArrayList<>());
        if (!skills.contains(skill)) {
            skills.add(skill);
            skill.getAccounts().add(loggedIn);
            accountRepo.save(loggedIn);
        }
        return "redirect:/addSkills";
    }
}
