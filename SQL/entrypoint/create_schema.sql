create table role (name varchar(255) not null, description varchar(255), primary key (name)) engine=InnoDB;
create table secret (secret_id bigint not null, is_initialized bit(1), password varchar(255), secret_key varbinary(2048), primary key (secret_id)) engine=InnoDB;
create table spring_security_5_db_sequence_generator (next_val bigint) engine=InnoDB;
insert into spring_security_5_db_sequence_generator values ( 1 );
create table user (username varchar(255) not null, account_not_expired bit not null, account_not_locked bit not null, credentials_not_expired bit not null, dob date, enabled bit not null, gender varchar(8), name varchar(255), profession varchar(255), password bigint not null, primary key (username)) engine=InnoDB;
create table user_roles (username varchar(255) not null, name varchar(255) not null, primary key (username, name)) engine=InnoDB;
alter table user add constraint FKmjbs7hnl0lfdc8a1726o3gk42 foreign key (password) references secret (secret_id);
alter table user_roles add constraint FKqjne5iia9813e1dce27m6377i foreign key (name) references role (name);
alter table user_roles add constraint FKo2j0svxgcf9yhw4j1iboj61yq foreign key (username) references user (username);
