/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestParam;
import project.Entities.Account;
import project.Entities.Comment;
import project.Entities.Message;
import project.Repositories.AccountRepository;
import project.Repositories.CommentRepository;
import project.Repositories.MessageRepository;
import project.Services.AccountService;
import project.Services.MessageService;

/**
 *
 * @author uurti
 */
@Controller
public class FeedController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentRepository commentRepo;

    @GetMapping("/feed")
    public String getFeed(Model model, Principal principal, @ModelAttribute("message") Message message, @ModelAttribute("comment") Comment comment) {
        Account acc = accountService.findByUserName(principal.getName());
        
        model.addAttribute("accountID", acc.getId());
        model.addAttribute("posts", messageService.findConnectedMessages(acc));
        
        return "feed";
    }

    @PostMapping("/feed")
    public String postFeed(Principal principal, @Valid @ModelAttribute("message") Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //errors are now basicly useless but otherwise all massages dissapeared from screen
            return "redirect:/feed";
        }
        Account acc = accountService.findByUserName(principal.getName());

        message.setMessageDate(LocalDateTime.now());
        message.setSender(acc);
        message.setLikes(0);
        message.setLikers(new ArrayList<>());
        message.setComments(new ArrayList<>());

        acc.getMessages().add(message);
        messageService.save(message);
        return "redirect:/feed";
    }

    @PostMapping("/likeMessage/{postId}/{accountID}")
    public String likePost(@PathVariable Long postId, @PathVariable Long accountID) {

        Message message = messageService.findById(postId);
        Account liker = accountService.findById(accountID);

        if (!message.getLikers().contains(liker)) {
            message.getLikers().add(liker);
            message.setLikes(message.getLikes() + 1);
            messageService.save(message);
        }
        return "redirect:/feed";
    }

    @PostMapping("/likeComment/{commentId}/{accountID}")
    public String likeComment(@PathVariable Long commentId, @PathVariable Long accountID) {

        Comment comment = commentRepo.getOne(commentId);
        Account liker = accountService.findById(accountID);

        if (!comment.getLikers().contains(liker)) {
            comment.getLikers().add(liker);
            comment.setLikes(comment.getLikes() + 1);
            commentRepo.save(comment);
        }
        return "redirect:/feed";
    }

    @PostMapping("/feed/{postId}")
    public String commentPost(@PathVariable Long postId, Principal principal, @Valid @ModelAttribute("message") Comment comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/feed";
        }
        Message message = messageService.findById(postId);

        comment.setMessageDate(LocalDateTime.now());
        comment.setSender(accountService.findByUserName(principal.getName()));
        comment.setLikes(0);
        comment.setLikers(new ArrayList<>());
        
        //this adds new comments as the first in the list(I just liked the idea of new messages at top)
        message.getComments().add(0, comment);
        messageService.save(message);
        return "redirect:/feed";
    }

}
