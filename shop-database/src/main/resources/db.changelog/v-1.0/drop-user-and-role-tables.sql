     alter table roles
       drop constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);
GO

    alter table users_roles
       drop constraint FKj6m8fwv7oqv74fcehir1a9ffy;
GO

    drop table roles;
GO

    drop table users;
GO

    drop table users_roles;
GO

