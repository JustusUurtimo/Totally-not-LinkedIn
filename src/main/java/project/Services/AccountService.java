/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.Entities.Account;
import project.Entities.Skill;
import project.Repositories.AccountRepository;

/**
 *
 * @author uurti
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;
    
        /*
     * 
     * ^ Assert position at the start of the line. (?=\P{Ll}*\p{Ll}) Ensure at least
     * one lowercase letter (in any script) exists. (?=\P{Lu}*\p{Lu}) Ensure at
     * least one uppercase letter (in any script) exists. (?=\P{N}*\p{N}) Ensure at
     * least one number character (in any script) exists.
     * (?=(!\\s)[\p{L}\p{N}]*[^(!\\s)\p{L}\p{N}]) Ensure at least one of any
     * character (in anyscript) that isn't a letter or digit exists, character that
     * is NOT whitespace. [\S]{8,} Matches any character 8 or more times, charactaer
     * must NOT be whitespace. $ Assert position at the end of the line.
     * 
     * Source :
     * https://stackoverflow.com/questions/48345922/reference-password-validation
     * 
     * (?!.*\\w(.)\\1{2,}) Ensures that no characters repeat more than 3 times in a
     * [\\S] Ensures that no whitespaces are allowed
     */
    private static final String PASSWORD_PATTERN = "^(?!.*\\w(.)\\1{2,})(?=\\P{Ll}*\\p{Ll})(?=\\P{Lu}*\\p{Lu})(?=\\P{N}*\\p{N})(?=[(!\\s)\\p{L}\\p{N}]*[^(!\\s)\\p{L}\\p{N}])[\\S]{8,}$";

    public Account findByUserName(String userName) {
        return accountRepo.findByUsername(userName);
    }

    public Account findByProfileString(String profileString) {
        return accountRepo.findByProfileString(profileString);
    }

    public Account findById(Long id) {
        return accountRepo.getOne(id);
    }

    public void save(Account acc) {
        accountRepo.save(acc);
    }

    @Cacheable("users")
    public List<Account> findByNameContaining(String word) {
        //return users in alpabetical order. Empty search return all users
        return accountRepo.findByNameContainingIgnoreCase(word, Sort.by(Sort.Order.asc("name")));
    }

    public boolean areNotConnected(Account asker, Account reciever) {

        //accounts are connected if the asker has reciever as connection or the asker has already asked the reciever
        return (!(asker.getConnections().contains(reciever))) && (!(reciever.getConnectionRequests().contains(asker)));
    }

    public void requestConnection(Account asker, Account reciever) {

        List<Account> acceptedUserConnectionRequests = reciever.getConnectionRequests();
        if (!acceptedUserConnectionRequests.contains(asker)) {

            reciever.getConnectionRequests().add(asker);
            accountRepo.save(reciever);

        }

    }

    public void endorseSkill(String skillName, Account endorser, Account reciever) {

        List<Skill> accSkills = reciever.getSkills();

        if (accSkills != null) {
            //I create dummy list to avoid ConcurrentModificationException
            List<Skill> updatedSkills = accSkills;
            for (Skill accskill : updatedSkills) {
                //only add endorment if the name mathes and the endorser has NOT endorsed the skill before
                List<Account> endorsers = accskill.getEndorsers();
                if (accskill.getName().equals(skillName) && (!endorsers.contains(endorser))) {
                    accskill.setEndorsments(accskill.getEndorsments() + 1);

                    endorsers.add(endorser);

                }
            }
            reciever.setSkills(updatedSkills);
            
        }
    }

    public void removeConnections(Account asker, Account reciever) {
        List<Account> loggedInConnections = asker.getConnections();
        List<Account> loggedInConnectionRequests = asker.getConnectionRequests();

        List<Account> acceptedUserConnections = reciever.getConnections();
        List<Account> acceptedUserConnectionRequests = reciever.getConnectionRequests();

        if (loggedInConnections.contains(reciever)) {
            loggedInConnections.remove(reciever);
        }
        if (loggedInConnectionRequests.contains(reciever)) {
            loggedInConnectionRequests.remove(reciever);
        }

        if (acceptedUserConnections.contains(asker)) {
            acceptedUserConnections.remove(asker);
        }

        if (acceptedUserConnectionRequests.contains(asker)) {
            reciever.getConnectionRequests().remove(asker);
        }

        accountRepo.save(asker);
        accountRepo.save(reciever);

    }

    public void addConnections(Account asker, Account reciever) {

        List<Account> loggedInConnectionRequests = asker.getConnectionRequests();

        if (loggedInConnectionRequests.contains(reciever)) {

            List<Account> loggedInConnections = asker.getConnections();
            List<Account> acceptedUserConnections = reciever.getConnections();
            List<Account> acceptedUserConnectionRequests = reciever.getConnectionRequests();

            loggedInConnectionRequests.remove(reciever);

            acceptedUserConnections.add(asker);
            loggedInConnections.add(reciever);

            if (acceptedUserConnectionRequests.contains(asker)) {
                reciever.getConnectionRequests().remove(asker);
            }
            accountRepo.save(asker);
            accountRepo.save(reciever);
        }

    }

    public boolean profileStringOk(Account account) {

        Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher m = p.matcher(account.getProfileString());

        return m.matches();
    }

    public void addImageToUser(MultipartFile file, Account acc) throws IOException {

        byte[] image = null;

        if (!file.isEmpty()) {
            image = file.getBytes();
        }
        /*else {
            // For local
            //File defaultImage = new ClassPathResource("public/Default-image.png").getFile();
            
            //For Heroku
            File defaultImage = new ClassPathResource("src/main/resources/public/Default-image.png").getFile();
            image = Files.readAllBytes(defaultImage.toPath());
        }*/
        acc.setProfilePicture(image);
    }

    public void removeImageFromUser(Account acc) {
        acc.setProfilePicture(null);
    }
    
    public boolean passwordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
}
