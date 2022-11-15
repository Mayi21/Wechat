create table wechat.t_friend
(
    wechat_id   varchar(12) not null,
    friend_id   varchar(12) not null,
    status      int         not null,
    create_time timestamp   not null,
    update_time timestamp   not null,
    constraint t_friend_pk
        primary key (wechat_id)
);
