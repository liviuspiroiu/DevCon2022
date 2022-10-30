create table if not exists  categories
(
    id                 bigint       not null
        primary key,
    created_date       datetime     not null,
    last_modified_date datetime     null,
    description        varchar(255) not null,
    name               varchar(255) not null
);

create table if not exists  hibernate_sequence
(
    next_val bigint null
);

create table if not exists  products
(
    id                 bigint         not null
        primary key,
    created_date       datetime       not null,
    last_modified_date datetime       null,
    name               varchar(255)   not null,
    price              decimal(10, 2) not null,
    sales_counter      int            null,
    status             varchar(255)   not null,
    category_id        bigint         null,
    description        text           not null,
    constraint FKog2rp4qthbtt2lfyhfo32lsw9
        foreign key (category_id) references categories (id)
);

create table if not exists  sys_users
(
    user_id    bigint auto_increment
        primary key,
    address_1  varchar(255) null,
    address_2  varchar(255) null,
    city       varchar(255) null,
    country    varchar(2)   null,
    postcode   varchar(10)  null,
    enabled    bit          not null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    password   varchar(255) null,
    role       varchar(255) null,
    telephone  varchar(255) null,
    username   varchar(255) null
);

create table if not exists  orders
(
    id                 bigint         not null
        primary key,
    created_date       datetime       not null,
    last_modified_date datetime       null,
    address_1          varchar(255)   null,
    address_2          varchar(255)   null,
    city               varchar(255)   null,
    country            varchar(2)     null,
    postcode           varchar(10)    null,
    shipped            datetime       null,
    status             varchar(255)   not null,
    total_price        decimal(10, 2) not null,
    payment_id         bigint         null,
    user_user_id       bigint         null,
    constraint UK_haujdjk1ohmeixjhnhslchrp1
        unique (payment_id),
    constraint FKrvoq8pbatwvnfuwokuo2kgmo3
        foreign key (user_user_id) references sys_users (user_id)
);

create table if not exists  order_items
(
    id                 bigint   not null
        primary key,
    created_date       datetime not null,
    last_modified_date datetime null,
    quantity           bigint   not null,
    order_id           bigint   null,
    product_id         bigint   null,
    constraint FKbioxgbv59vetrxe0ejfubep1w
        foreign key (order_id) references orders (id),
    constraint FKocimc7dtr037rh4ls4l95nlfi
        foreign key (product_id) references products (id)
);

create table if not exists  payments
(
    id                 bigint       not null
        primary key,
    created_date       datetime     not null,
    last_modified_date datetime     null,
    paypal_payment_id  varchar(255) null,
    status             varchar(255) not null,
    order_id           bigint       null,
    constraint UK_8vo36cen604as7etdfwmyjsxt
        unique (order_id),
    constraint FK81gagumt0r8y3rmudcgpbk42l
        foreign key (order_id) references orders (id)
);

ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK8aol9f99s97mtyhij0tvfj41f;

alter table orders
    add constraint FK8aol9f99s97mtyhij0tvfj41f
        foreign key (payment_id) references payments (id);

