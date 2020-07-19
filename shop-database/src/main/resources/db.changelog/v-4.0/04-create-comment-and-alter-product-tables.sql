    create table comments (
       id bigint not null auto_increment,
        comment TEXT not null,
        local_date_time datetime(6) not null,
        rating integer,
        product_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;
GO

    create table products_comments (
       product_id bigint not null,
        comment_id bigint not null
    ) engine=InnoDB;
GO

--     alter table products_comments
--        drop index UK_e32mu2i9s8k2uhiq8fq4xoruf;
GO

    alter table products_comments 
       add constraint UK_e32mu2i9s8k2uhiq8fq4xoruf unique (comment_id);
GO

    alter table comments 
       add constraint FK6uv0qku8gsu6x1r2jkrtqwjtn 
       foreign key (product_id) 
       references products (id);
GO

    alter table comments 
       add constraint FK8omq0tc18jd43bu5tjh6jvraq 
       foreign key (user_id) 
       references users (id);
GO

    alter table products_comments 
       add constraint FKs1lnami2h59r7m5dyx3v4qp1k 
       foreign key (comment_id) 
       references comments (id)
GO

    alter table products_comments 
       add constraint FK5dewacdfyx9em2ofathrg1vyf 
       foreign key (product_id) 
       references products (id);
GO