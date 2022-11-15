create table wechat.t_user
(
    passwd varchar(16)  not null,
    name   varchar(128) not null,
    id     bigint auto_increment,
    constraint t_user_pk
        primary key (id)
);