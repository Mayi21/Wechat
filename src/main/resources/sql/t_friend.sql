create table wechat.t_friend
(
    id          bigint auto_increment
        primary key,
    wechat_id   varchar(12) not null,
    friend_id   varchar(12) not null,
    status      int         not null,
    create_time timestamp   not null,
    update_time timestamp   not null
);

create index t_friend_wechat_id_index
    on wechat.t_friend (wechat_id);