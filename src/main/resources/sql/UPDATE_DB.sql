ALTER TABLE item_description add column
	ITEM_NAME      VARCHAR(255) null;

UPDATE item_description id
	INNER JOIN items i ON id.ITEM_ID = i.ITEM_ID
SET id.ITEM_NAME = i.ITEM_NAME WHERE I.ITEM_ID = ID.ITEM_ID;

ALTER TABLE offer_price add column
STORE_ID BIGINT not null;

alter table offer_price
	add constraint sub_items_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE;

alter table offer_price
	add OFFER_PRICE_TYPE VARCHAR(30) default 'ABSOLUTE' not null;

alter table order_items
	add COORDINATES VARCHAR(50) default '0' null;

UPDATE order_items SET RECEIVED = true, RECEIVE_TIME = '0001-01-01 00:00:00.000000' WHERE RECEIVED = false AND COORDINATES = '0' AND STATUS = 'COMPLETE';




INSERT INTO shop.store_config (STORE_ID,`KEY`,VALUE) VALUES
	                                                     (-2,'freekassa.wallet.api.key','96A2E56A31F0ABB61D9BDDB4C69A8218'),
	                                                     (-2,'freekassa.purse','F112359797'),
	                                                     (-2,'freekassa.wallet.id','F112229662'),
	                                                     (-1,'comisson.enabled','true'),
	                                                     (-2,'comisson.enabled','false'),
	                                                     (-1,'freekassa.comission','0.63'),
	                                                     (-2,'freekassa.comission','1');



create table funds_transfers
(
	fund_transfer_id BIGINT         not null,
	store_from       BIGINT         not null,
	wallet_to        varchar(50)    not null,
	initial_amount   decimal(19, 2) not null,
	percentage       decimal(19, 2) not null,
	amount           decimal(19, 2) not null,
	currency         varchar(3)     not null,
	transfer_status  varchar(20)    not null,
	transfer_time    timestamp      not null,
	constraint funds_transfers_pk
		primary key (fund_transfer_id),
	constraint funds_transfers_stores_from_fk
		foreign key (store_from) references stores (STORE_ID)
			ON DELETE CASCADE ON UPDATE CASCADE
);

create table funds_transfer_properties
(
	fund_transfer_id bigint       not null,
	NAME       varchar(255) not null,
	VALUE      varchar(255) null,
	constraint funds_transfer_properties_funds_transfers_fund_transfer_id_fk
		foreign key (fund_transfer_id) references funds_transfers (fund_transfer_id)
			ON DELETE CASCADE ON UPDATE CASCADE
);



INSERT INTO shop.store_config (STORE_ID,`KEY`,VALUE) VALUES
	(-2,'freekassa.withdraw.api.url','https://api.freekassa.ru/v1/withdrawals/create'),
	(-2,'freekassa.wallet.api.url','https://fkwallet.com/api_v1.php');


alter table funds_transfers
	add payment_id BIGINT not null;

alter table funds_transfers
	add constraint funds_transfers_payments_null_fk
		foreign key (payment_id) references payments (PAYMENT_ID);




-- ---------------------------------------------------------------------------------------------------------------------



alter table items
	add DESCRIPTION_ID BIGINT null;

alter table items
	add constraint items_description_DESCIPTION_ID_fk
		foreign key (DESCRIPTION_ID) references description (DESCRIPTION_ID)
			on update cascade;


rename table item_description to description;

UPDATE items i join description d on d.ITEM_ID = i.ITEM_ID
set i.DESCRIPTION_ID = d.DESCRIPTION_ID;

alter table description
	drop foreign key item_description_items_ITEM_ID_fk;

alter table description
	drop ITEM_ID;

alter table description
	add constraint description_languages_LANGUAGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
			on delete cascade;

alter table description
	change ITEM_NAME ENTITY_NAME varchar(255) null after DESCRIPTION_ID;

alter table categories
	add VISIBLE bit default 0 not null;

alter table categories
	add STORE_ID bigint default -2 not null;

alter table categories
	add constraint categories_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID)
			on update cascade;


alter table categories
	add DESCRIPTION_ID BIGINT null;


alter table categories
	add constraint categories_description_DESCRIPTIOM_ID_fk
		foreign key (DESCRIPTION_ID) references description (DESCRIPTION_ID)
			on update cascade;

update categories set CATEGORY_ID = 100 where CATEGORY_ID = 1;

insert into description (DESCRIPTION_ID, ENTITY_NAME, DESCRIPTION, LANGUAGE_ID, STORE_ID, PUBLISHED)
values (100, 'Все', '', -3, -2, 1),
       (107, 'Услуги', '', -3, -2, 1),
       (108, 'Экипировка', '', -3, -2, 1),
       (109, 'Транспортные средства', '', -3, -2, 1),
       (110, 'Стройматериалы', '', -3, -2, 1),
       (111, 'Контейнеры', '', -3, -2, 1),
       (200, 'Одежда', '', -3, -2, 1),
       (201, 'Головной убор', '', -3, -2, 1),
       (202, 'Очки', '', -3, -2, 1),
       (203, 'Маска', '', -3, -2, 1),
       (204, 'Торс', '', -3, -2, 1),
       (205, 'Перчатки', '', -3, -2, 1),
       (206, 'Рюкзак', '', -3, -2, 1),
       (207, 'Пояс', '', -3, -2, 1),
       (208, 'Ноги', '', -3, -2, 1),
       (209, 'Обувь', '', -3, -2, 1),
       (210, 'Жилет', '', -3, -2, 1);

insert into categories(CATEGORY_ID, category_name, DESCRIPTION_ID, visible, store_id)
values (200, 'cloth', 200, 0, -2),
       (201, 'hats', 201, 0, -2),
       (202, 'goggles', 202, 0, -2),
       (203, 'mask', 203, 0, -2),
       (204, 'body', 204, 0, -2),
       (205, 'gloves', 205, 0, -2),
       (206, 'backpack', 206, 0, -2),
       (207, 'belt', 207, 0, -2),
       (208, 'legs', 208, 0, -2),
       (209, 'shoes', 209, 0, -2),
       (210, 'vest', 210, 0, -2);

update categories c join description d on c.DESCRIPTION_ID = d.DESCRIPTION_ID set c.DESCRIPTION_ID = d.DESCRIPTION_ID where c.CATEGORY_ID between 99 and 211;

insert into category_relations
values (200, 201),
       (200, 202),
       (200, 203),
       (200, 204),
       (200, 205),
       (200, 206),
       (200, 207),
       (200, 208),
       (200, 209),
       (200, 210);



-- ---------------------------------------------------------------------------------------------------------------------



alter table users
	add LANGUAGE_ID BIGINT default -2 not null;

alter table users
	add constraint users_languages_LANGUANGE_ID_fk
		foreign key (LANGUAGE_ID) references languages (LANGUAGE_ID)
			on update cascade;




alter table users
	add USER_AGREEMENT bit(1) default 0 null;



insert into store_config
values (-1, 'language.default', -2),
       (-2, 'language.default', -3);


update shop.store_config set STORE_ID = -1 where shop.store_config.`KEY` = 'freekassa.ips';




-- ---------------------------------------------------------------------------------------------------------------------




alter table user_services
	modify SERVER_ID bigint null;

insert into server_config values
	                              (1, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (2, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (3, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (4, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (5, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (6, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2),
	                              (7, 'PATH_TO_CUSTOM_SET', '../../Omega/servers/%s/profiles/DayZShop/DataBase/CustomSets/%s', -2);


insert into description values ((select next_val + 1 from hibernate_sequence), 'Кастомный сет', 'Собери сет на свое усмотрение', -2, -2, 1);

insert into items values ((select next_val + 2 from hibernate_sequence), 'custom_set', 'https://cdn.discordapp.com/attachments/934733103589654558/1086383901150294049/1unknown345.png', -2, 'CUSTOM_SET', 1, null, 0, (select next_val + 1 from hibernate_sequence));

insert into item_server_buyable values ((select next_val + 2 from hibernate_sequence), 1),
                                       ((select next_val + 2 from hibernate_sequence), 2),
                                       ((select next_val + 2 from hibernate_sequence), 3),
                                       ((select next_val + 2 from hibernate_sequence), 4),
                                       ((select next_val + 2 from hibernate_sequence), 5),
                                       ((select next_val + 2 from hibernate_sequence), 6),
                                       ((select next_val + 2 from hibernate_sequence), 7);

insert into item_category values ((select next_val + 2 from hibernate_sequence), 1), ((select next_val + 2 from hibernate_sequence), 108);

insert into list_price values ((select next_val + 3 from hibernate_sequence), 1000, 'RUB', (select next_val + 2 from hibernate_sequence), -2);

update hibernate_sequence set next_val = next_val + 4;