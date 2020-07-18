    create table pictures (
       id bigint not null auto_increment,
        content_type varchar(255) not null,
        name varchar(255) not null,
        local_path varchar(255),
        picture_data_id bigint,
        product_id bigint,
        primary key (id)
    ) engine=InnoDB;
GO

    create table pictures_data (
       id bigint not null auto_increment,
        data longblob not null,
        primary key (id)
    ) engine=InnoDB;
GO

    create table products_pictures (
       product_id bigint not null,
        picture_id bigint not null
    ) engine=InnoDB;
GO

    alter table pictures
       add constraint UK_ehsu2tyinopypjox1ijxt3g3c unique (picture_data_id);
GO


    alter table products_pictures
       add constraint UK_bxf9yjeusrtfkq7y6d3k2614a unique (picture_id);
GO

    alter table pictures
       add constraint FKe9cv52k04xoy6cj8xy308gnw3
       foreign key (picture_data_id)
       references pictures_data (id);
GO

    alter table pictures
       add constraint FK43hu51t487tsmo7tltxmdx9br
       foreign key (product_id)
       references products (id);
GO

    alter table products_pictures
       add constraint FKloucf8ggy74nmdej2jmvttvi4
       foreign key (picture_id)
       references pictures (id);
GO

    alter table products_pictures
       add constraint FKh3amnci4cl7xcl1al140xw79e
       foreign key (product_id)
       references products (id);
GO