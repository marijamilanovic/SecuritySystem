insert into role (id, name) values (1, "ROLE_USER");
insert into role (id, name) values (2, "ROLE_ADMIN");
insert into role (id, name) values (3, "ROLE_CA");

insert into permission (id, name) values (10, 'GENERATE_CERT');
insert into permission (id, name) values (11, 'GENERATE_ROOT');
insert into permission (id, name) values (12, 'REVOKE_CERT');
insert into permission (id, name) values (13, 'ALL_REQUESTS');
insert into permission (id, name) values (14, 'USER_REQUESTS');
insert into permission (id, name) values (15, 'GET_REQUEST');
insert into permission (id, name) values (16, 'GET_USERS');


insert into role_permission (role_id, permission_id) values (2, 10);
insert into role_permission (role_id, permission_id) values (3, 10);
insert into role_permission (role_id, permission_id) values (2, 11);
insert into role_permission (role_id, permission_id) values (2, 12);
insert into role_permission (role_id, permission_id) values (3, 12);
insert into role_permission (role_id, permission_id) values (2, 13);
insert into role_permission (role_id, permission_id) values (1, 14);
insert into role_permission (role_id, permission_id) values (1, 15);
insert into role_permission (role_id, permission_id) values (2, 15);
insert into role_permission (role_id, permission_id) values (3, 15);


insert into users (id, username, email, password) values (100, 'gojko', 'gojko@mailinator.com', '$2a$10$fBKaq1pRKSeax3VDe31mve2eWVLo0GvhD1DzmuPosE.ucsvMZ/0XK');
-- role:user, pass: Gojko123*
insert into users (id, username, email, password) values (101, 'tasa', 'tasa@mailinator.com', '$2a$10$TsbpbZZpo/07zuO9pkA84.8hFzLIl4h9szLqc0kgQoOAPE0NHnVFe');
-- role:user, pass: Tasa123*
insert into users (id, username, email, password) values (102, 'masa', 'masa@mailinator.com', '$2a$10$7G1mRNuowW6qEkxFKfdueucEyjRFONhaMtm/4feuFx0kZ17UuF9R6');
-- role:user, pass: Masa123*
insert into users (id, username, email, password) values (103, 'maja', 'maja@mailinator.com', '$2a$10$nd1MEnerYUyV3OrYhFDggetDFkevekw3vjtqOwIpEo4abCSyIdzQG');
-- role:user, pass: Maja123*
insert into users (id, username, email, password) values (104, 'nikola', 'nikola@mailinator.com', '$2a$10$fVZOwUSOkARZrebCWgyu8uT9wDazYn2qkYWHLewOo3d5cgsH3Sf5u');
-- role:admin, pass: Gojko123*

insert into user_role(user_id, role_id) values (100, 1);
insert into user_role(user_id, role_id) values (101, 1);
insert into user_role(user_id, role_id) values (102, 1);
insert into user_role(user_id, role_id) values (103, 1);
insert into user_role(user_id, role_id) values (104, 2);


insert into certificate_request(id, common_name, surname, given_name, organisation, organisation_unit, country, email, valid_from, valid_to, isCA, key_usage, user_id) values (200, "Tasa", "Damjanac", "Nastasja", "Guglasi", "team 5", "Serbia", "tasa@mailinator.com", '2021-08-06', '2021-08-07', false, "Digital Certificate", 101);
insert into certificate_request(id, common_name, surname, given_name, organisation, organisation_unit, country, email, valid_from, valid_to, isCA, key_usage, user_id) values (201, "Masa", "Milanovic", "Marija", "Guglasi", "team 5", "Serbia", "masa@mailinator.com", '2021-07-01', '2021-08-01', true, "Certificate Authority", 102);