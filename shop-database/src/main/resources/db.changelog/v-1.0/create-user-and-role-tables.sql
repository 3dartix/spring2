 create table roles (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;
GO

    create table users (
       id bigint not null auto_increment,
        username varchar(64),
        password varchar(128),
        firstname varchar(50),
        lastname varchar(50),
        phone varchar(20),
        email varchar(128),
        primary key (id)
    ) engine=InnoDB;
GO

    create table users_roles (
       user_id bigint not null,
        role_id bigint not null,
        primary key (user_id, role_id)
    ) engine=InnoDB;
GO

    alter table roles
       add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);
GO

    alter table users_roles
       add constraint FKj6m8fwv7oqv74fcehir1a9ffy
       foreign key (role_id)
       references roles (id);
GO

    alter table users_roles
       add constraint FK2o0jvgh89lemvvo17cbqvdxaa
       foreign key (user_id)
       references users (id);
GO