package project.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.Entities.Account;
import project.Repositories.AccountRepository;
import project.Services.AccountService;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/sign-up")
    public String signUpGet(@ModelAttribute("account") Account account) {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpPost(@RequestParam MultipartFile file, @Valid @ModelAttribute("account") Account account, BindingResult bindingResult) throws IOException {
        if (accountRepo.findByUsername(account.getUsername()) != null) {
            FieldError error = new FieldError("username", "username", "Username already exists");
            bindingResult.addError(error);
        }
        if (accountRepo.findByProfileString(account.getProfileString()) != null) {
            FieldError error = new FieldError("profileString", "profileString", "profileString already exists");
            bindingResult.addError(error);
        }

        if (!accountService.profileStringOk(account)) {
            FieldError error = new FieldError("profileString", "profileString", "profileString cant contain special characters or scandinavian characters, such as 'ä' or 'ö'");
            bindingResult.addError(error);
        }

        if (!accountService.passwordValid(account.getPassword())) {
            FieldError error = new FieldError("password", "password", "The password must be atleast 8 characters long. "
                    + "It must contain at least one Capital letter, one small letter, a number and at least one special character (eg. #,?,!).\n"
                    + "\n"
                    + "The password should NOT contain spaces or other whitespace characters, such as tabulator or linebreaks. "
                    + "The password should not contain easy to quess words or phrases, such as 'password' or '12345'. "
                    + "The password also must not contain repeating characters or numbers (more than 3 times)");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        accountService.addImageToUser(file, account);

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        accountRepo.save(account);
        return "redirect:sign-in";
    }

}
