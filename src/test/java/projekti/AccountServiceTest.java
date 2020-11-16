/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;


import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import project.Entities.Account;
import project.Repositories.AccountRepository;
import project.Services.AccountService;
import project.projektiMainApplication;

/**
 *
 * @author uurti
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = projektiMainApplication.class)
public class AccountServiceTest {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Test
    public void profileStringIsOk() {
        
        Account acc = new Account();
        acc.setProfileString("pekka");
        assertTrue(accountService.profileStringOk(acc));
        
        acc.setProfileString("pekkä");
        assertFalse(accountService.profileStringOk(acc));
        
        acc.setProfileString("jaa//%");
        assertFalse(accountService.profileStringOk(acc));
        
    }
    
    

}
