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
import project.Services.AccountService;

/**
 *
 * @author uurti
 */
@Controller
public class ConnectionsController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AccountService accService;

    private Account loggedIn;

    @GetMapping("/connections")
    public String connectionsPage(Principal principal, Model model) {
        loggedIn = accountRepo.findByUsername(principal.getName());
        
        model.addAttribute("accountName", loggedIn.getName());
        model.addAttribute("connectionRequests", loggedIn.getConnectionRequests());
        model.addAttribute("connections", loggedIn.getConnections());
        return "connections";
    }

    @PostMapping("/connections/{userID}/{confirm}")
    public String connectionsHandler(@PathVariable Long userID, @PathVariable Boolean confirm, Principal principal) {
        loggedIn = accountRepo.findByUsername(principal.getName());
        Account acceptedUser = accountRepo.getOne(userID);
        if (accService.areNotConnected(loggedIn, acceptedUser) && confirm) {
            accService.addConnections(loggedIn, acceptedUser);
        } else {
            accService.removeConnections(loggedIn, acceptedUser);
        }

        return "redirect:/connections";
    }

}
