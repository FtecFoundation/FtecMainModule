package com.ftec.repositories;

import com.ftec.entities.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentDAO extends CrudRepository<Comment, Long> {
}
