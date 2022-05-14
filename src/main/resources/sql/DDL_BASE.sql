create table categories
(
	CATEGORY_ID bigint not null
		primary key,
	CATEGORY_NAME varchar(255) not null
);

create table category_relations
(
	PARENT_CATEGORY_ID bigint not null,
	CHILD_CATEGORY_ID bigint not null,
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
	LANGUAGE_ID bigint not null
		primary key,
	LANGUAGE varchar(255) null,
	COUNTRY varchar(255) null,
	LOCALE varchar(255) null
);

create table privilege
(
	PRIVILEGE_ID bigint not null
		primary key,
	PRIVILEGE_NAME varchar(255) null
);

create table roles
(
	ROLE_ID bigint not null,
	ROLE_NAME varchar(32) null,
	constraint roles_ROLE_ID_uindex
		unique (ROLE_ID)
);

alter table roles
	add primary key (ROLE_ID);

create table roles_privileges
(
	ROLE_ID bigint not null,
	PRIVILEGE_ID bigint not null,
	constraint FK8kxttvjnfb2dtfhjsw9nbwgnb
		foreign key (PRIVILEGE_ID) references privilege (PRIVILEGE_ID),
	constraint roles_privileges_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
);

create table stores
(
	STORE_ID bigint not null
		primary key,
	STORE_NAME varchar(255) not null,
	constraint UK_b95rcr8yybvka6xv44j8f5avu
		unique (STORE_NAME)
);

create table items
(
	ITEM_ID bigint not null
		primary key,
	ITEM_NAME varchar(255) not null,
	ITEM_DESCRIPTION varchar(255) null,
	BUYABLE bit not null,
	IMAGE_URL varchar(255) null,
	DELETABLE tinyint default 0 not null,
	PUBLISHED tinyint not null,
	STORE_ID bigint null,
	constraint FK2gokdsrsrxhit2vaq2o5ghjmq
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table item_category
(
	ITEM_ID bigint not null,
	CATEGORY_ID bigint not null,
	constraint FK2vrjlr1cx4796d638s9laawh6
		foreign key (CATEGORY_ID) references categories (CATEGORY_ID),
	constraint FK9x3u4c5ap2ontvk8gid06sob6
		foreign key (ITEM_ID) references items (ITEM_ID)
);

create table item_description
(
	DESCRIPTION_ID bigint not null
		primary key,
	DESCRIPTION varchar(255) null,
	LANGUAGE_ID bigint null,
	STORE_ID bigint null,
	ITEM_ID bigint null,
	PUBLISHED bit null,
	constraint FK3fk9uqhit7s4219epgqrokwmo
		foreign key (STORE_ID) references stores (STORE_ID),
	constraint FKnsigttavejwcrw33a02pfswl4
		foreign key (ITEM_ID) references items (ITEM_ID),
	constraint FKtibhh4wrty273ummjngilhp5i
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
);

create table list_price
(
	LISTPRICE bigint not null
		primary key,
	PRICE decimal(19,2) not null,
	CURRENCY varchar(255) not null,
	ITEM_ID bigint not null,
	constraint FKflw3cs3wwdxl7ivwt2sue2ljf
		foreign key (ITEM_ID) references items (ITEM_ID)
);

create table offer_price
(
	OFFER_ID bigint not null
		primary key,
	PRICE decimal(19,2) null,
	CURRENCY varchar(255) null,
	START_TIME datetime(6) null,
	END_TIME datetime(6) null,
	PRIORITY int null,
	ITEM_ID bigint null,
	constraint UK_equ60oycdwy8nhqr0emt1gh1e
		unique (ITEM_ID),
	constraint FKfenl0org6dixeh79gce55vj05
		foreign key (ITEM_ID) references items (ITEM_ID)
);

create table servers
(
	SERVER_ID bigint null,
	STORE_ID bigint null,
	STORE_NAME varchar(40) null,
	SERVER_NAME varchar(255) null,
	constraint UK_3jgue04c9ev4di3mrebxwegqn
		unique (STORE_ID),
	constraint FKh92krstf1jf72h0iqgs0mfnf
		foreign key (SERVER_ID) references stores (STORE_ID),
	constraint servers_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table store_languages
(
	STORE_ID bigint not null,
	LANGUAGE_ID bigint not null,
	constraint UK_ch11u1nsh4kh6aaqfklgmpoiu
		unique (LANGUAGE_ID),
	constraint FK1kdytqfho0odvnxgrb06yoty9
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID),
	constraint FKj02le5kpa0vpyhgrcknyr46mq
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table sub_items
(
	MAIN_ITEM_ID bigint not null,
	SUB_ITEM_ID bigint not null,
	constraint UK_2b96x3q13nt9u9bmb9ug0jn04
		unique (SUB_ITEM_ID),
	constraint FKk6pfbc7yumixkvqfwlw2t60sw
		foreign key (SUB_ITEM_ID) references items (ITEM_ID),
	constraint FKs26m23h1yrh7ku91jmw4n9grx
		foreign key (MAIN_ITEM_ID) references items (ITEM_ID)
);

create table users
(
	USER_ID bigint not null
		primary key,
	STEAM_ID varchar(255) null,
	BALANCE decimal(19,2) null,
	STEAM_NICKNAME varchar(255) null,
	STEAM_AVATAR_URL varchar(255) null,
	STORE_ID bigint null,
	constraint FKojefi57a28my3srup14jrs2f8
		foreign key (STORE_ID) references stores (STORE_ID)
);

create table orders
(
	ORDER_ID bigint not null
		primary key,
	ORDER_TOTAL decimal(19,2) null,
	STATUS varchar(255) null,
	USER_ID bigint null,
	constraint FKenwru67yr8f0ei6m1bc2xlj4w
		foreign key (USER_ID) references users (USER_ID)
);

create table order_items
(
	ORDER_ITEM_ID bigint not null
		primary key,
	BOUGHT_TIME datetime(6) null,
	RECEIVED bit null,
	RECEIVE_TIME datetime(6) null,
	PRICE decimal(19,2) null,
	ITEM_ID bigint null,
	USER_ID bigint null,
	QUANTITY int null,
	ORDER_ID bigint null,
	constraint UK_8mqc19ne0nb63tawmougm4e2
		unique (USER_ID),
	constraint FK6sjhssmsryq1o07mqnpky6cny
		foreign key (USER_ID) references users (USER_ID),
	constraint FK7kago8adpfrwq13q6ypcn06xr
		foreign key (ORDER_ITEM_ID) references users (USER_ID),
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
		foreign key (USER_ID) references users (USER_ID),
	constraint users_roles_roles_ROLE_ID_fk
		foreign key (ROLE_ID) references roles (ROLE_ID)
);

-- Cyclic dependencies found

create table store_config
(
	STORE_CONFIG_ID bigint auto_increment,
	STORE_ID bigint not null,
	`KEY` varchar(255) not null,
	VALUE varchar(255) not null,
	storeId bigint null,
	Store_STORE_ID bigint not null,
	constraint STORE_CONFIG_STORE_CONFIG_ID_uindex
		unique (STORE_CONFIG_ID),
	constraint FK5g86a44ye5aeyltemv5mx0c4
		foreign key (Store_STORE_ID) references stores (STORE_ID),
	constraint FKoadin1w6tig7b51n89xkfiubm
		foreign key (STORE_CONFIG_ID) references store_config (STORE_CONFIG_ID),
	constraint STORE_CONFIG_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			on delete cascade
);

alter table store_config
	add primary key (STORE_CONFIG_ID);

