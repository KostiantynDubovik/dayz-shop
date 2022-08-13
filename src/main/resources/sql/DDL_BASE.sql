create table categories
(
	CATEGORY_ID   bigint       not null
		primary key,
	CATEGORY_NAME varchar(255) not null
);

create table category_relations
(
	PARENT_CATEGORY_ID bigint not null,
	CHILD_CATEGORY_ID  bigint not null,
	constraint UK_ukumnt4tcuacos1h9fvkj2uu
		unique (CHILD_CATEGORY_ID),
	constraint FKalr31nuy9d9x4qs006sca2w70
		foreign key (CHILD_CATEGORY_ID) references categories (CATEGORY_ID),
	constraint FKc50resr68q3cpwobaikm9lf9c
		foreign key (PARENT_CATEGORY_ID) references categories (CATEGORY_ID)
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
		foreign key (PRIVILEGE_ID) references privileges (PRIVILEGE_ID),
	constraint roles_privileges_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
);

create table stores
(
	STORE_ID   bigint       not null
		primary key,
	STORE_NAME varchar(255) not null,
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
	DELETABLE  bit          not null,
	STORE_ID   bigint       null,
	BUYABLE    bit          not null,
	constraint items_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table item_category
(
	ITEM_ID     bigint not null,
	CATEGORY_ID bigint not null,
	constraint item_category_item_category_CATEGORY_ID_fk
		foreign key (CATEGORY_ID) references categories (CATEGORY_ID),
	constraint item_category_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID)
);

create table item_description
(
	DESCRIPTION_ID bigint       not null
		primary key,
	DESCRIPTION    varchar(255) null,
	LANGUAGE_ID    bigint       null,
	STORE_ID       bigint       null,
	ITEM_ID        bigint       null,
	PUBLISHED      bit          null,
	constraint item_description_store_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID),
	constraint item_description_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID),
	constraint item_description_languages_LANGUAGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
);

create table list_price
(
	LISTPRICE bigint         not null
		primary key,
	PRICE     decimal(19, 2) not null,
	CURRENCY  varchar(255)   not null,
	ITEM_ID   bigint         not null,
	STORE_ID  bigint         not null,
	constraint list_price_items_ITEM_ID_fk
		foreign key (ITEM_ID) references items (ITEM_ID),
	constraint list_price_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
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
);

create table servers
(
	SERVER_ID   bigint       null,
	STORE_ID    bigint       null,
	STORE_NAME  varchar(40)  null,
	SERVER_NAME varchar(255) null,
	constraint UK_3jgue04c9ev4di3mrebxwegqn
		unique (STORE_ID),
	constraint FKh92krstf1jf72h0iqgs0mfnf
		foreign key (SERVER_ID) references stores (STORE_ID),
	constraint servers_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table store_config
(
	STORE_ID bigint       not null,
	`KEY`    varchar(255) not null,
	VALUE    varchar(255) not null,
	primary key (STORE_ID, `KEY`),
	constraint store_config_KEY_uindex
		unique (`KEY`),
	constraint store_config_STORE_ID_uindex
		unique (STORE_ID),
	constraint store_config_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table store_languages
(
	STORE_ID    bigint null,
	LANGUAGE_ID bigint null,
	constraint STORE_LANGUAGE_languages_LANGUAGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID),
	constraint STORE_LANGUAGE_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table sub_items
(
	MAIN_ITEM_ID bigint not null,
	SUB_ITEM_ID  bigint not null,
	constraint UK_2b96x3q13nt9u9bmb9ug0jn04
		unique (SUB_ITEM_ID),
	constraint FKk6pfbc7yumixkvqfwlw2t60sw
		foreign key (SUB_ITEM_ID) references items (ITEM_ID),
	constraint FKs26m23h1yrh7ku91jmw4n9grx
		foreign key (MAIN_ITEM_ID) references items (ITEM_ID)
);

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
);

create table orders
(
	ORDER_ID    bigint         not null
		primary key,
	ORDER_TOTAL decimal(19, 2) null,
	STATUS      varchar(255)   null,
	USER_ID     bigint         null,
	STORE_ID    bigint         null,
	constraint FKenwru67yr8f0ei6m1bc2xlj4w
		foreign key (USER_ID) references users (USER_ID),
	constraint orders_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table order_items
(
	ORDER_ITEM_ID bigint         not null
		primary key,
	BOUGHT_TIME   datetime(6)    null,
	RECEIVED      bit            null,
	RECEIVE_TIME  datetime(6)    null,
	PRICE         decimal(19, 2) null,
	ITEM_ID       bigint         null,
	USER_ID       bigint         null,
	ORDER_ID      bigint         null,
	constraint UK_8mqc19ne0nb63tawmougm4e2
		unique (USER_ID),
	constraint FK6sjhssmsryq1o07mqnpky6cny
		foreign key (USER_ID) references users (USER_ID),
	constraint FKnnrjyhgtcxoh0eo45qvl41ira
		foreign key (ORDER_ID) references orders (ORDER_ID),
	constraint FKssyx5rw664bnq7bwtjerw3wwy
		foreign key (ITEM_ID) references items (ITEM_ID)
);

create table users_roles
(
	USER_ID bigint null,
	ROLE_ID bigint null,
	constraint USERS_ROLES_users_USER_ID_fk
		foreign key (USER_ID) references users (USER_ID)
			on delete cascade,
	constraint users_roles_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
);

