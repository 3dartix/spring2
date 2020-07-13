    create table orders (
       id bigint not null auto_increment,
        phone varchar(20) not null,
        address text not null,
        price decimal(19,2) not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;
GO
    
    create table orders_items (
       id bigint not null auto_increment,
        price decimal(19,2),
        quantity integer,
        order_id bigint,
        product_id bigint,
        primary key (id)
    ) engine=InnoDB;
GO
    
    alter table orders 
       add constraint FK32ql8ubntj5uh44ph9659tiih 
       foreign key (user_id) 
       references users (id);
GO
    
    alter table orders_items 
       add constraint FKij1wwgx6o198ubsx1oulpopem 
       foreign key (order_id) 
       references orders (id);
GO
    
    alter table orders_items 
       add constraint FKi7bwk7vkrcl9jeyqpso3fpnjk 
       foreign key (product_id) 
       references products (id);
GO