package ru.geekbrains.representation;

import lombok.Data;

import java.util.Optional;

@Data
public class CommentRepr {
    private Optional<Long> product_id;
    private Optional<String> comment;
    private Optional<Integer> rating;
}
