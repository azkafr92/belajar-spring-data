alter table
  if exists users
add
  constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table
  if exists users
add
  constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

alter table
  if exists addresses
add
  constraint FK1fa36y2oqhao3wgg2rw1pi459 foreign key (user_id) references users;

alter table
  if exists user_identities
add
  constraint FKl8i188j5rgpteq6erbt6x1h0m foreign key (user_id) references users;

alter table
  if exists user_tags
add
  constraint FKioatd2q4dvvsb5k6al6ge8au4 foreign key (tag_id) references tags;

alter table
  if exists user_tags
add
  constraint FKdylhtw3qjb2nj40xp50b0p495 foreign key (user_id) references users;
