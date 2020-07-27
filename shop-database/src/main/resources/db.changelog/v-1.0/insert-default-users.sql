INSERT INTO users (`name`, `password`)
VALUE ('admin', '$2y$12$Rj.sbFuU5uMEhjS/MIRbc.a8EgJaqwLPaLleHox3QhLm4LAd4StNC'),
    ('guest', '$2y$12$Rj.sbFuU5uMEhjS/MIRbc.a8EgJaqwLPaLleHox3QhLm4LAd4StNC');
GO

INSERT INTO roles (`name`)
VALUE ('ROLE_ADMIN'), ('ROLE_GUEST');
GO

INSERT INTO `users_roles` (`user_id`, `role_id`)
SELECT (SELECT id FROM `users` where `name` = 'admin'), (select id from `roles` where `name` = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM `users` where `name` = 'guest'), (select id from `roles` where `name` = 'ROLE_GUEST');
GO
