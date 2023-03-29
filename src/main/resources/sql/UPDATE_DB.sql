ALTER TABLE item_description add column
	ITEM_NAME      VARCHAR(255) null;

UPDATE item_description id
	INNER JOIN items i ON id.ITEM_ID = i.ITEM_ID
SET id.ITEM_NAME = i.ITEM_NAME WHERE I.ITEM_ID = ID.ITEM_ID;

ALTER TABLE offer_price add column
STORE_ID BIGINT not null;

alter table offer_price
	add constraint sub_items_stores_STORE_ID_fk
		foreign key (STORE_ID) references stores (STORE_ID);

alter table offer_price
	add OFFER_PRICE_TYPE VARCHAR(30) default 'ABSOLUTE' not null;

alter table order_items
	add COORDINATES VARCHAR(50) default '0' null;

UPDATE order_items SET RECEIVED = true, RECEIVE_TIME = '0001-01-01 00:00:00.000000' WHERE RECEIVED = false AND COORDINATES = '0' AND STATUS = 'COMPLETE';




INSERT INTO shop.store_config (STORE_ID,`KEY`,VALUE) VALUES
	                                                     (-2,'freekassa.wallet.api.key','96A2E56A31F0ABB61D9BDDB4C69A8218'),
	                                                     (-1,'freekassa.wallet.id','F112359797'),
	                                                     (-2,'freekassa.wallet.id','F112229662'),
	                                                     (-2,'freekassa.wallet.own.id','F112229662'),
	                                                     (-1,'comisson.enabled','true'),
	                                                     (-2,'comisson.enabled','false'),
	                                                     (-1,'freekassa.comission','0.63'),
	                                                     (-2,'freekassa.comission','1');



create table funds_transfers
(
	fund_transfer_id BIGINT      not null,
	store_from       BIGINT      not null,
	wallet_to        varchar(50) not null,
	initial_amount   decimal     not null,
	percentage       decimal     not null,
	amount           decimal     not null,
	currency         varchar(3)  not null,
	transfer_status  varchar(20) not null,
	transfer_time    timestamp   not null,
	constraint funds_transfers_pk
		primary key (fund_transfer_id),
	constraint funds_transfers_stores_from_fk
		foreign key (store_from) references stores (STORE_ID)
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

