INSERT INTO shop.hibernate_sequence (next_val) VALUES (1);

INSERT INTO shop.stores (STORE_ID, STORE_NAME) VALUES (-1, 'root');
INSERT INTO shop.stores (STORE_ID, STORE_NAME) VALUES (-2, 'alcatraz');
INSERT INTO shop.stores (STORE_ID, STORE_NAME) VALUES (-3, 'midnight');

INSERT INTO shop.languages (LANGUAGE_ID, LANGUAGE, COUNTRY, LOCALE) VALUES (-1, 'uk', 'UA', 'uk_UA');
INSERT INTO shop.languages (LANGUAGE_ID, LANGUAGE, COUNTRY, LOCALE) VALUES (-2, 'en', 'US', 'en_US');
INSERT INTO shop.languages (LANGUAGE_ID, LANGUAGE, COUNTRY, LOCALE) VALUES (-3, 'ru', 'RU', 'ru_RU');

INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-1, -1);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-1, -2);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-1, -3);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-2, -1);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-2, -2);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-2, -3);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-3, -1);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-3, -2);
INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID) VALUES (-3, -3);

INSERT INTO shop.privileges (PRIVILEGE_ID, PRIVILEGE_NAME) VALUES (-1, 'STORE_READ');
INSERT INTO shop.privileges (PRIVILEGE_ID, PRIVILEGE_NAME) VALUES (-2, 'STORE_WRITE');
INSERT INTO shop.privileges (PRIVILEGE_ID, PRIVILEGE_NAME) VALUES (-3, 'APP_WRITE');

INSERT INTO shop.roles (ROLE_ID, ROLE_NAME) VALUES (-4, 'APP_ADMIN');
INSERT INTO shop.roles (ROLE_ID, ROLE_NAME) VALUES (-5, 'STORE_ADMIN');
INSERT INTO shop.roles (ROLE_ID, ROLE_NAME) VALUES (-6, 'USER');

INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-4, -1);
INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-4, -2);
INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-4, -3);
INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-5, -1);
INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-5, -2);
INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID) VALUES (-6, -1);

INSERT INTO shop.users (USER_ID, STEAM_ID, BALANCE, STEAM_NICKNAME, STEAM_AVATAR_URL, STORE_ID, IS_ACTIVE) VALUES (-1, '76561198107293144', 999999999.00, '1BarracudA', 'https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/34/34a3382e56a9ca233c8e9123ff04e2633d2dd751.jpg', -1, true);
INSERT INTO shop.users (USER_ID, STEAM_ID, BALANCE, STEAM_NICKNAME, STEAM_AVATAR_URL, STORE_ID, IS_ACTIVE) VALUES (-2, '76561198121789747', 999999999.00, '1 из Легенд', 'https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/34/09645ed527d97552b7c48f15361fb123339e0b0b.jpg', -1, true);

INSERT INTO shop.users_roles (USER_ID, ROLE_ID) VALUES (-1, -4);
INSERT INTO shop.users_roles (USER_ID, ROLE_ID) VALUES (-2, -4);

INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (1, 'Радиатор', 'CarRadiator', true, 'https://cdn.discordapp.com/attachments/820626215756234774/933383809511079936/radik.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (2, 'Свеча', 'SparkPlug', true, 'https://media.discordapp.net/attachments/934733103589654558/934733221357289472/svechya.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (3, 'Аккумулятор', 'CarBattery', true, 'https://media.discordapp.net/attachments/820626215756234774/933383809737588756/akum.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (4, 'Колючая проволока', 'BarbedWire', true, 'https://media.discordapp.net/attachments/952625703558475846/995779130882785471/wiretrap.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (5, 'Пачка гвоздей', 'NailBox', true, 'https://media.discordapp.net/attachments/952625703558475846/960491005604413440/nail.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (6, 'Оружейная стойка', 'Skyline_Kit_Gun_Rack', true, 'https://media.discordapp.net/attachments/934733103589654558/934890665970643044/gunrack.png', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (7, 'Автомобильная лампа', 'HeadlightH7', true, 'https://static.wikia.nocookie.net/dayz_gamepedia/images/0/08/HeadlampBulb.png/revision/latest?cb=20160829123308', true, -2, 'ITEM', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (9, 'Автомобильная лампа', 'HeadlightH7', false, 'https://static.wikia.nocookie.net/dayz_gamepedia/images/0/08/HeadlampBulb.png/revision/latest?cb=20160829123308', true, -2, 'ITEM', 1);

INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (8, 'Нива', 'OffroadHatchback', true, 'https://media.discordapp.net/attachments/820626215756234774/939629084764418109/admagazskrini_3.png', true, -2, 'VEHICLE', 1);

INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (11, 'Дверь на Ниву', 'HatchbackDoors_CoDriver', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (12, 'Дверь на Ниву', 'HatchbackDoors_Driver', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (13, 'Капот на Ниву', 'HatchbackHood', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (14, 'Ляда на Ниву', 'HatchbackTrunk', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (15, 'Колесо на Ниву', 'HatchbackWheel', true, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808986787850/niva.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (16, 'Колесо на Ниву', 'HatchbackWheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808986787850/niva.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (17, 'Колесо на Ниву', 'HatchbackWheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808986787850/niva.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (18, 'Колесо на Ниву', 'HatchbackWheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808986787850/niva.png', true, -2, 'VEHICLE', 1);

insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 1);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 2);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 3);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 7);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 9);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 11);

insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 12);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 13);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 14);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 15);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 16);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 17);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (8, 18);





INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (1, 100.00, 'RUB', 1, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (2, 100.00, 'RUB', 2, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (3, 100.00, 'RUB', 3, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (4, 100.00, 'RUB', 4, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (5, 100.00, 'RUB', 5, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (6, 100.00, 'RUB', 6, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (7, 100.00, 'RUB', 7, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (8, 250.00, 'RUB', 8, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (9, 100.00, 'RUB', 9, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (11, 100.00, 'RUB', 11, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (12, 100.00, 'RUB', 12, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (13, 100.00, 'RUB', 13, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (14, 100.00, 'RUB', 14, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (15, 100.00, 'RUB', 15, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (16, 100.00, 'RUB', 16, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (17, 100.00, 'RUB', 17, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (18, 100.00, 'RUB', 18, -2);






INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (19, 'Gunter', 'Hatchback_02', true, 'https://media.discordapp.net/attachments/820626215756234774/939629002073702440/admagazskrini_1.png', true, -2, 'VEHICLE', 1);

INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (20, 'Дверь на Гольф', 'Hatchback_02_Door_2_1', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (21, 'Дверь на Гольф', 'Hatchback_02_Door_2_2', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (22, 'Дверь на Гольф', 'Hatchback_02_Door_1_1', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (23, 'Дверь на Гольф', 'Hatchback_02_Door_1_2', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (24, 'Ляда на Гольф', 'Hatchback_02_Trunk', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (25, 'Капот на Гольф', 'Hatchback_02_Hood', false, '', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (26, 'Колесо на Гольф', 'Hatchback_02_Wheel', true, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808588349490/golf.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (27, 'Колесо на Гольф', 'Hatchback_02_Wheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808588349490/golf.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (28, 'Колесо на Гольф', 'Hatchback_02_Wheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808588349490/golf.png', true, -2, 'VEHICLE', 1);
INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, BUYABLE, IMAGE_URL, DELETABLE, STORE_ID, ITEM_TYPE, COUNT) VALUES (29, 'Колесо на Гольф', 'Hatchback_02_Wheel', false, 'https://cdn.discordapp.com/attachments/820626215756234774/933383808588349490/golf.png', true, -2, 'VEHICLE', 1);

INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (19, 250.00, 'RUB', 19, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (21, 100.00, 'RUB', 21, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (22, 100.00, 'RUB', 22, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (23, 100.00, 'RUB', 23, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (24, 100.00, 'RUB', 24, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (25, 100.00, 'RUB', 25, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (26, 100.00, 'RUB', 26, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (27, 100.00, 'RUB', 27, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (28, 100.00, 'RUB', 28, -2);
INSERT INTO shop.list_price (LISTPRICE, PRICE, CURRENCY, ITEM_ID, STORE_ID) VALUES (29, 100.00, 'RUB', 29, -2);


insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 2);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 3);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 7);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 9);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 11);


insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 20);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 21);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 22);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 23);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 24);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 25);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 26);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 27);
insert into shop.sub_items (MAIN_ITEM_ID, SUB_ITEM_ID) values (19, 28);


INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE) VALUES (-2, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json');
INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE) VALUES (-2, 'SSH_IP', '195.18.27.91');
INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE) VALUES (-2, 'SSH_PWD', 'bulkasmaslom227#');
INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE) VALUES (-2, 'SSH_USR', 'Constantine');
INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE) VALUES (-2, 'steam.api.key', 'DF21B88E007F7F971EE63BFA733BB812');