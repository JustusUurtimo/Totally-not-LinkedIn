/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.Entities.Account;
import project.Entities.Message;
import project.Repositories.MessageRepository;

/**
 *
 * @author uurti
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    public List<Message> findConnectedMessages(Account acc) {

        List<Long> ids = acc.getConnections().stream().map(Account::getId).collect(Collectors.toList());
        
        //also add users id so that the logged in users can see their messages as well
        ids.add(acc.getId());
        
        //find 25 messages sent by people whit whom the logged in user is connected to and sorted by date
        //I set the sort to descending because it helped testing to see newest at top
        return messageRepo.findAllBySenderIdIn(ids, PageRequest.of(0, 25, Sort.by("messageDate").descending()));
    }

    public void save(Message message) {
        messageRepo.save(message);
    }
    
    public Message findById(Long id) {
        return messageRepo.getOne(id);
    }
}
