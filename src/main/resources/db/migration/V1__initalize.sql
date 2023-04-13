create sequence tags_seq start with 1 increment by 50;

create table addresses (
  id bigserial not null,
  address TEXT,
  user_id uuid not null,
  primary key (id)
);

create table tags (
  id bigint not null,
  name varchar(255),
  primary key (id)
);

create table user_identities (
  user_id uuid not null,
  dob date,
  nik varchar(16),
  first_name varchar(255),
  last_name varchar(255),
  primary key (user_id)
);

create table user_tags (
  user_id uuid not null,
  tag_id bigint not null,
  primary key (user_id, tag_id)
);

create table users (
  id uuid not null,
  created_on timestamp(6) not null,
  updated_on timestamp(6),
  authorities varchar(255) array,
  email varchar(255) not null,
  is_enabled boolean default false,
  password varchar(255) not null,
  username varchar(255) not null,
  primary key (id)
);
