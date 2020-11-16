/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entities;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author uurti
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Account sender;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Comment> comments;

    private LocalDateTime messageDate;

    private Integer likes = 0;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Account> likers;

    @NotEmpty
    private String content;

}
