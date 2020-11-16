/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.Entities.Comment;

/**
 *
 * @author uurti
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    
}

