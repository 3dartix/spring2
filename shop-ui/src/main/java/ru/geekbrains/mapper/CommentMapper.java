package ru.geekbrains.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.geekbrains.entity.Comment;
import ru.geekbrains.entity.Product;
import ru.geekbrains.entity.User;
import ru.geekbrains.representation.CommentRepr;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public static CommentMapper MAPPER;

    @Autowired
    public CommentMapper() {
        MAPPER = this;
    }

    public Comment toComment (User user, Product product, CommentRepr commentRepr) {
        Comment comment = new Comment();
        comment.setLocalDateTime(LocalDateTime.now());
        comment.setUser(user);
        comment.setProduct(product);
        comment.setComment(commentRepr.getComment().get());
        comment.setRating(commentRepr.getRating().get());
        return comment;
    }
}
