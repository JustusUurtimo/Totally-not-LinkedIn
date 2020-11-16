/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.h2.util.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.Entities.Account;
import project.Entities.Skill;
import project.Services.AccountService;
import project.Services.SkillService;

/**
 *
 * @author uurti
 */
@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SkillService skillService;

    @GetMapping("/profile")
    public String getLoggedInProfile(Principal principal) {
        Account loggedIn = accountService.findByUserName(principal.getName());
        return "redirect:/profile/" + loggedIn.getProfileString();
    }

    @GetMapping("/profile/{profileString}")
    public String showprofile(Model model, @PathVariable String profileString, Principal principal) throws IOException {
        Account loggedIn = accountService.findByUserName(principal.getName());
        Account acc = loggedIn;
        //we only search for the other acc if we are viewing other page than ours
        //Otherwise it can be same as loggedIn, also no reason to check connection if in own profile
        //I dont know why, but when registering user with 
        if (!profileString.equals(loggedIn.getProfileString()) && (accountService.findByProfileString(profileString) != null)) {
            acc = accountService.findByProfileString(profileString);
            model.addAttribute("usersNotConnected", accountService.areNotConnected(acc, loggedIn));
        }
        List<Skill> top3Skills = skillService.findTopSkills(acc);
        List<Skill> others = skillService.otherSkills(acc);

        model.addAttribute("accountName", acc.getName());
        model.addAttribute("skillsTop", top3Skills);
        model.addAttribute("skillsOthers", others);
        model.addAttribute("accounts", acc);
        model.addAttribute("accountUserName", acc.getUsername());
        model.addAttribute("accountID", acc.getId());
        model.addAttribute("connectionsSize", acc.getConnections().size());

        return "profile";
    }

    @PostMapping("/profile/{skillName}/{accountID}")
    public String endorseSkill(@PathVariable String skillName, @PathVariable Long accountID, Principal principal) {
        Account endorser = accountService.findByUserName(principal.getName());
        Account reciever = accountService.findById(accountID);
        
        accountService.endorseSkill(skillName, endorser, reciever);

        accountService.save(reciever);
        
        return "redirect:/profile/" + reciever.getProfileString();

    }

    @PostMapping(path = "/profile/{accountID}")
    public String connectionsHandler(@PathVariable Long accountID, Principal principal) {
        Account requested = accountService.findById(accountID);
        Account acc = accountService.findByUserName(principal.getName());

        if (accountService.areNotConnected(acc, requested)) {
            accountService.requestConnection(acc, requested);

        } else {
            accountService.removeConnections(acc, requested);
        }
        return "redirect:/profile/" + requested.getProfileString();
    }

    @PostMapping("/addProfilePic")
    public String addProfilePic(@RequestParam MultipartFile file, Principal principal) throws IOException {
         Account acc = accountService.findByUserName(principal.getName());
        if (!file.isEmpty()) {
            acc = accountService.findByUserName(principal.getName());
            accountService.addImageToUser(file, acc);
            accountService.save(acc);
        }
        return "redirect:/profile/" + acc.getProfileString();
    }

    @PostMapping("/removeProfilePic")
    public String removeProfilePic(Principal principal) throws IOException {
        Account acc = accountService.findByUserName(principal.getName());
        accountService.removeImageFromUser(acc);
        accountService.save(acc);
        return "redirect:/profile/" + acc.getProfileString();
    }


}
