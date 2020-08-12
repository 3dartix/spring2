create table category (
       id bigint not null auto_increment,
        name varchar(150),
        primary key (id)
    ) engine=InnoDB;
GO

    create table products (
       id bigint not null auto_increment,
        description text,
        name varchar(150),
        price NUMERIC,
        primary key (id)
    ) engine=InnoDB;
GO

    create table product_category (
       product_id bigint not null,
        category_id bigint not null,
        primary key (product_id, category_id)
    ) engine=InnoDB;
GO

    alter table product_category
       add constraint FKkuq35ls1d40wpjb5htpp14q4e
       foreign key (category_id)
       references category (id);
GO

    alter table product_category
       add constraint FK5w21wp3eyugvi2lii94iao3fm
       foreign key (product_id)
       references products (id);
GO