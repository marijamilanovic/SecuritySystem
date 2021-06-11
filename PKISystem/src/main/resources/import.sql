insert into role (id, name) values (1, "ROLE_USER");
insert into role (id, name) values (2, "ROLE_ADMIN");

insert into permission (id, name) values (1, 'GENERATE_CERT');
insert into permission (id, name) values (2, 'GENERATE_ROOT');
insert into permission (id, name) values (3, 'REVOKE_CERT');


insert into role_permission (role_id, permission_id) values (2, 1)
insert into role_permission (role_id, permission_id) values (2, 2)
insert into role_permission (role_id, permission_id) values (2, 3)