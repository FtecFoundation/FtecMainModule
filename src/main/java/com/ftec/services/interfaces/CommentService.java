package com.ftec.services.interfaces;

import com.ftec.entities.Comment;

public interface CommentService {

    void save(Comment comment);

    void delete(Comment comment);
}
