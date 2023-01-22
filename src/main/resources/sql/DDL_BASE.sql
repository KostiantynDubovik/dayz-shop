create table categories
(
	CATEGORY_ID   bigint       not null
		primary key,
	CATEGORY_NAME varchar(255) not null,
	DISPlAY_NAME  varchar(255) not null DEFAULT ''
);

create table category_relations
(
	PARENT_CATEGORY_ID bigint not null,
	CHILD_CATEGORY_ID  bigint not null,
	constraint UK_ukumnt4tcuacos1h9fvkj2uu
		unique (CHILD_CATEGORY_ID),
	constraint FKalr31nuy9d9x4qs006sca2w70
		foreign key (CHILD_CATEGORY_ID) references categories (CATEGORY_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint FKc50resr68q3cpwobaikm9lf9c
		foreign key (PARENT_CATEGORY_ID) references categories (CATEGORY_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table hibernate_sequence
(
	next_val bigint null
);

create table languages
(
	LANGUAGE_ID bigint       not null
		primary key,
	LANGUAGE    varchar(255) null,
	COUNTRY     varchar(255) null,
	LOCALE      varchar(255) null
);

create table privileges
(
	PRIVILEGE_ID   bigint       not null
		primary key,
	PRIVILEGE_NAME varchar(255) null
);

create table roles
(
	ROLE_ID   bigint      not null,
	ROLE_NAME varchar(32) null,
	constraint roles_ROLE_ID_uindex
		unique (ROLE_ID)
);

alter table roles
	add primary key (ROLE_ID);

create table roles_privileges
(
	ROLE_ID      bigint not null,
	PRIVILEGE_ID bigint not null,
	constraint FK8kxttvjnfb2dtfhjsw9nbwgnb
		foreign key (PRIVILEGE_ID) references privileges (PRIVILEGE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint roles_privileges_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table stores
(
	STORE_ID        bigint       not null
		primary key,
	STORE_NAME      varchar(255) not null,
	PARENT_STORE_ID bigint       null,
	constraint UK_b95rcr8yybvka6xv44j8f5avu
		unique (STORE_NAME)
);

create table items
(
	ITEM_ID    bigint       not null
		primary key,
	ITEM_NAME  varchar(255) not null,
	IN_GAME_ID varchar(255) not null,
	IMAGE_URL  varchar(255) null,
	STORE_ID   bigint       null,
	ITEM_TYPE  varchar(20)  not null,
	COUNT      bigint       null,
	COLOR      varchar(30)  null,
	SEQUENCE   bigint       not null default 0,
	constraint items_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table item_category
(
	ITEM_ID     bigint not null,
	CATEGORY_ID bigint not null,
	constraint item_category_item_category_CATEGORY_ID_fk
		foreign key (CATEGORY_ID) references categories (CATEGORY_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint item_category_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table item_description
(
	DESCRIPTION_ID bigint       not null
		primary key,
	DESCRIPTION    varchar(500) null,
	LANGUAGE_ID    bigint       default -3 null,
	STORE_ID       bigint       null,
	ITEM_ID        bigint       null,
	PUBLISHED      bit          null,
	constraint item_description_store_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint item_description_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint item_description_languages_LANGUAGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table list_price
(
	LISTPRICE_ID bigint         not null
		primary key,
	PRICE        decimal(19, 2) not null,
	CURRENCY     varchar(255)   not null,
	ITEM_ID      bigint         not null,
	STORE_ID     bigint         not null,
	constraint list_price_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint list_price_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table offer_price
(
	OFFER_ID   bigint         not null
		primary key,
	PRICE      decimal(19, 2) null,
	CURRENCY   varchar(255)   null,
	START_TIME datetime(6)    null,
	END_TIME   datetime(6)    null,
	PRIORITY   int            null,
	ITEM_ID    bigint         null,
	constraint UK_equ60oycdwy8nhqr0emt1gh1e
		unique (ITEM_ID),
	constraint FKfenl0org6dixeh79gce55vj05
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table servers
(
	SERVER_ID     bigint       null,
	STORE_ID      bigint       null,
	SERVER_NAME   varchar(255) null,
	INSTANCE_NAME varchar(100) not null,
	constraint servers_pk
		unique (SERVER_ID, STORE_ID, SERVER_NAME),
	constraint servers_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table store_config
(
	STORE_ID bigint       not null,
	`KEY`    varchar(255) not null,
	VALUE    varchar(255) not null,
	primary key (STORE_ID, `KEY`),
	constraint store_config_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table store_languages
(
	STORE_ID    bigint null,
	LANGUAGE_ID bigint null,
	constraint STORE_LANGUAGE_languages_LANGUAGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint STORE_LANGUAGE_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table sub_items
(
	ITEM_ID        bigint           not null,
	SUB_ITEM_ID         bigint           not null,
	QUANTITY            bigint default 1 not null,
	constraint sub_items_pk
		primary key (ITEM_ID, SUB_ITEM_ID),
	constraint main_item_FK
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint sub_item_FK
		foreign key (SUB_ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create index sub_items_items_ITEM_ID_fk_2
	on sub_items (SUB_ITEM_ID);



create table users
(
	USER_ID          bigint                      not null
		primary key,
	STEAM_ID         varchar(255)                not null,
	BALANCE          decimal(19, 2) default 0.00 null,
	STEAM_NICKNAME   varchar(255)                not null,
	STEAM_AVATAR_URL varchar(255)                not null,
	STORE_ID         bigint                      not null,
	IS_ACTIVE        bit                         not null,
	constraint UKrsl8blftmuw9y1u82pt7o4i9r
		unique (USER_ID, STORE_ID),
	constraint FKojefi57a28my3srup14jrs2f8
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table orders
(
	ORDER_ID    bigint         not null
		primary key,
	ORDER_TOTAL decimal(19, 2) null,
	STATUS      varchar(255)   null,
	USER_ID     bigint         null,
	STORE_ID    bigint         null,
	SERVER_ID   bigint         null,
	TIME_PLACED TIMESTAMP      null,
	constraint FKenwru67yr8f0ei6m1bc2xlj4w
		foreign key (USER_ID) references users (USER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint orders_servers_SERVER_ID_fk
		foreign key (SERVER_ID) references servers (SERVER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint orders_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table order_items
(
	ORDER_ITEM_ID bigint                      not null
		primary key,
	BOUGHT_TIME   datetime(6)                 null,
	RECEIVED      bit                         null,
	RECEIVE_TIME  datetime(6)                 null,
	PRICE         decimal(19, 2)              null,
	TOTAL_PRICE   decimal(19, 2) default 0.00 null,
	ITEM_ID       bigint                      null,
	USER_ID       bigint                      null,
	ORDER_ID      bigint                      null,
	SERVER_ID     bigint                      null,
	M_CODE        varchar(255)                 not null,
	STATUS        varchar(20)                 not null,
	COUNT         int            default 1    not null,
	constraint order_items_ORDER_ITEM_ID_uindex
		unique (ORDER_ITEM_ID),
	constraint order_items_servers_SERVER_ID_fk
		foreign key (SERVER_ID) references servers (SERVER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint FK6sjhssmsryq1o07mqnpky6cny
		foreign key (USER_ID) references users (USER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint FKnnrjyhgtcxoh0eo45qvl41ira
		foreign key (ORDER_ID) references orders (ORDER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint FKssyx5rw664bnq7bwtjerw3wwy
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table users_roles
(
	USER_ID bigint null,
	ROLE_ID bigint null,
	constraint USERS_ROLES_users_USER_ID_fk
		foreign key (USER_ID) references users (USER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint users_roles_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table item_attributes
(
	ITEM_ID         BIGINT       not null,
	STORE_ID        BIGINT       not null,
	ATTRIBUTE_NAME  VARCHAR(20)  not null,
	ATTRIBUTE_VALUE VARCHAR(255) null,
	constraint ITEM_ATTRIBUTES_pk
		primary key (ITEM_ID, STORE_ID, ATTRIBUTE_NAME),
	constraint ITEM_ATTRIBUTES_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint ITEM_ATTRIBUTES_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table payments
(
	PAYMENT_ID     bigint       not null
		primary key,
	AMOUNT         decimal      not null,
	CHARGE_TIME    timestamp    null,
	USER_ID        bigint       null,
	USER_FROM      bigint       null,
	STORE_ID       bigint       not null,
	PAYMENT_TYPE   varchar(255) not null,
	PAYMENT_STATUS varchar(20)  not null,
	CURRENCY       VARCHAR(3)   not null,
	BALANCE_BEFORE decimal      not null default 0,
	BALANCE_AFTER  decimal      not null default 0,
	constraint PAYMENTS_users_null_fk
		foreign key (USER_ID) references users (USER_ID)
			ON DELETE CASCADE ON UPDATE CASCADE,
	constraint PAYMENTS_users_from_fk
		foreign key (USER_FROM) references users (USER_ID)
			on update cascade on delete cascade,
	constraint payments_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table payment_properties
(
	PAYMENT_ID bigint       not null,
	NAME       varchar(255) not null,
	VALUE      varchar(255) null,
	constraint PAYMENT_PROPERTIES_payments_null_fk
		foreign key (PAYMENT_ID) references payments (PAYMENT_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists server_config
(
	SERVER_ID bigint       not null,
	`KEY`     varchar(255) not null,
	VALUE     varchar(255) not null,
	STORE_ID  bigint       not null,
	primary key (SERVER_ID, `KEY`),
	constraint server_config_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID),
	constraint server_config_servers_SERVER_ID_fk
		foreign key (SERVER_ID) references servers (SERVER_ID)
			on delete cascade on update cascade
);


create table if not exists user_services
(
	USER_ID   bigint      not null,
	ITEM_TYPE varchar(25) not null,
	END_DATE  timestamp   not null,
	SERVER_ID bigint      not null,
	ORDER_ID bigint not null,
	primary key (USER_ID, ITEM_TYPE),
	constraint user_services_users_USER_ID_fk
		foreign key (USER_ID) references users (USER_ID)
			on delete cascade on update cascade,
	constraint user_services_orders_null_fk
		foreign key (ORDER_ID) references orders (ORDER_ID)
			on update cascade on delete cascade,
	constraint user_services_servers_SERVER_ID_fk
		foreign key (SERVER_ID) references servers (SERVER_ID)
			on delete cascade on update cascade
);


create table item_server_buyable
(
	item_id   bigint not null,
	server_id bigint not null,
	constraint item_server_buyable_items_null_fk
		foreign key (item_id) references items (ITEM_ID)
			on update cascade on delete cascade,
	constraint item_server_buyable_servers_null_fk
		foreign key (server_id) references servers (server_id)
			on update cascade on delete cascade
);

create table order_properties
(
	ORDER_ID bigint       not null,
	NAME     VARCHAR(30)  not null,
	VALUE    varchar(255) null,
	constraint order_properties_orders_null_fk
		foreign key (ORDER_ID) references orders (ORDER_ID)
			on update cascade on delete cascade
);
