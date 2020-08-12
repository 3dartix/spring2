INSERT INTO users (`username`, `password`)
    VALUE ('admin', '$2y$12$O4zRlpMBTyAJ26SSrVkdgu8bHQaCPol6CI0xtIIyhLQEtdWeNglka'),
    ('guest', '$2y$12$zZWgOQ/.I985bigRaiZ3nOWffhGmSBbvNEAaC1oJVIaMQEMdt/Yjy'),
    ('anonymous', '$2y$12$7xXL3bEREnxJHRkbAk8IA.J8iD7y0YZSKv6QtnEYfc1kaDy0O1.XC');
GO

INSERT INTO roles (`name`)
    VALUE ('ROLE_ADMIN'), ('ROLE_GUEST'), ('ROLE_USER');
GO

INSERT INTO `users_roles` (`user_id`, `role_id`)
SELECT (SELECT id FROM `users` where `username` = 'admin'), (select id from `roles` where `name` = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM `users` where `username` = 'guest'), (select id from `roles` where `name` = 'ROLE_GUEST')
UNION ALL
SELECT (SELECT id FROM `users` where `username` = 'anonymous'), (select id from `roles` where `name` = 'ROLE_GUEST');
GO

