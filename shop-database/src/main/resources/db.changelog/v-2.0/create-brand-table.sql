    create table brands (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;
GO

    alter table products
       add column brand_id bigint not null;
GO

    alter table brands
       add constraint UK_oce3937d2f4mpfqrycbr0l93m unique (name);
GO
    alter table products
       add constraint FKa3a4mpsfdf4d2y6r8ra3sc8mv
       foreign key (brand_id)
       references brands (id);
GO