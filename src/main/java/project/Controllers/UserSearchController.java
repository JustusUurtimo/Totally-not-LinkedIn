/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.Entities.Account;
import project.Services.AccountService;

/**
 *
 * @author uurti
 */
@Controller
public class UserSearchController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/searchedAccounts")
    public String searchedAccounts(@RequestParam String searchedAccounts, Model model) {
        return "redirect:/searchedAccounts/" + searchedAccounts;
    }

    @GetMapping("/searchedAccounts/{searchedAccounts}")
    public String searchAccountsGet(Model model, @PathVariable String searchedAccounts) {
        List<Account> accs = accountService.findByNameContaining(searchedAccounts);
        model.addAttribute("accounts", accs);
        return "searchedAccounts";
    }
}
