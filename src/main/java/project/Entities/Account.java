package project.Entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import org.hibernate.annotations.Type;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    @NotEmpty
    @Column(unique = true)
    private String username;
    
    @NotEmpty
    private String password;
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    @Column(unique = true)
    private String profileString;
    
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Skill> skills;
    
        
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Account> connections;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Account> connectionRequests;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "sender")
    private List<Message> messages;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    //@Type(type = "org.hibernate.type.BinaryType")
    private byte[] profilePicture;

}
