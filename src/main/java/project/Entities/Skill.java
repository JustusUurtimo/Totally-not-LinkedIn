/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entities;

import java.util.ArrayList;
import project.Entities.Account;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class Skill extends AbstractPersistable<Long> {
    
    @NotNull
    private String name;
    
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
    
    @Min(1)
    @Max(10)
    @NotNull
    private Integer profiency;
    
    private String notes;
    
    private Integer endorsments;
    
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Account> endorsers;
}
