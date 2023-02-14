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

UPDATE order_items SET RECEIVED = true, RECEIVE_TIME = '0001-01-01 00:00:00.000000' WHERE RECEIVED = false AND COORDINATES = '0' AND STATUS = 'COMPLETE'


