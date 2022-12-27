INSERT INTO shop.hibernate_sequence (next_val)
VALUES (1);

INSERT INTO shop.stores (STORE_ID, STORE_NAME)
VALUES (-1, 'root');
INSERT INTO shop.stores (STORE_ID, STORE_NAME)
VALUES (-2, 'alcatraz');

INSERT INTO shop.languages (LANGUAGE_ID, LANGUAGE, COUNTRY, LOCALE)
VALUES (-1, 'uk', 'UA', 'uk_UA'),
       (-2, 'en', 'US', 'en_US'),
       (-3, 'ru', 'RU', 'ru_RU');

INSERT INTO shop.store_languages (STORE_ID, LANGUAGE_ID)
VALUES (-1, -1),
       (-1, -2),
       (-1, -3),
       (-2, -1),
       (-2, -2),
       (-2, -3);

INSERT INTO shop.privileges (PRIVILEGE_ID, PRIVILEGE_NAME)
VALUES (-1, 'STORE_READ'),
       (-2, 'STORE_WRITE'),
       (-3, 'APP_WRITE');

INSERT INTO shop.roles (ROLE_ID, ROLE_NAME)
VALUES (-4, 'APP_ADMIN'),
       (-5, 'STORE_ADMIN'),
       (-6, 'USER');

INSERT INTO shop.roles_privileges (ROLE_ID, PRIVILEGE_ID)
VALUES (-4, -1),
       (-4, -2),
       (-4, -3),
       (-5, -1),
       (-5, -2),
       (-6, -1);

INSERT INTO shop.users (USER_ID, STEAM_ID, BALANCE, STEAM_NICKNAME, STEAM_AVATAR_URL, STORE_ID, IS_ACTIVE)
VALUES (-1, '76561198107293144', 999999999.00, 'Constantine',
        'https://avatars.akamai.steamstatic.com/db91f340f01a90029a89b8fdc41e8976a2888f3f.jpg', -1, true),
       (-2, '76561198121789747', 999999999.00, 'ФУкаБляка',
        'https://avatars.akamai.steamstatic.com/09645ed527d97552b7c48f15361fb123339e0b0b.jpg', -1, true),
       (174, '76561199105717168', 0.00, 'Тузик',
        'https://avatars.akamai.steamstatic.com/c2072eed0d2a06b1eb2c39874ae432ba59dbe1b2.jpg', -2, true),
       (189, '76561199231957857', 0.00, 'kostiantyn.dubovik',
        'https://avatars.akamai.steamstatic.com/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb.jpg', -2, true),
       (192, '76561199075276596', 0.00, 'Conqueeftador',
        'https://avatars.akamai.steamstatic.com/e1dde7fe207a397634580d4755f7890ccf24ddd7.jpg', -2, true);

INSERT INTO shop.users_roles (USER_ID, ROLE_ID)
VALUES (-1, -4),
       (-2, -4),
       (174, -6),
       (189, -6),
       (192, -6);

INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, IMAGE_URL, STORE_ID, ITEM_TYPE, COUNT, COLOR, SEQUENCE)
VALUES (1, 'Радиатор', 'CarRadiator',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037180388038148209/unknown.png', -2, 'ITEM', 1,
        null, 100),
       (2, 'Свеча', 'SparkPlug',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037170548783988756/unknown.png', -2, 'ITEM', 1,
        null, 200),
       (3, 'Легковой аккумулятор', 'CarBattery',
        'https://cdn.discordapp.com/attachments/952625703558475846/1038479094679081000/543534.png', -2, 'ITEM', 1, null,
        300),
       (4, 'Колючая проволока', 'BarbedWire',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037176324181209098/unknown.png', -2, 'ITEM', 1,
        null, 400),
       (5, 'Пачка гвоздей', 'NailBox',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037178953154166824/unknown.png', -2, 'ITEM', 1,
        null, 500),
       (6, 'Оружейная стойка', 'Skyline_Kit_Gun_Rack',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037180829333454890/unknown.png', -2, 'ITEM', 1,
        null, 600),
       (7, 'Автомобильная лампа', 'HeadlightH7',
        'https://static.wikia.nocookie.net/dayz_gamepedia/images/0/08/HeadlampBulb.png/revision/latest?cb=20160829123308',
        -2, 'ITEM', 1, null, 700),
       (9, 'Грузовой аккумулятор', 'TruckBattery',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037179833274339378/unknown.png', -2, 'ITEM', 1,
        null, 800),
       (8, 'Зелёная Нива', 'OffroadHatchback',
        'https://cdn.discordapp.com/attachments/952625703558475846/1038095378903470100/unknown.png', -2, 'VEHICLE', 1,
        null, 900),
       (11, 'Дверь на Ниву', 'HatchbackDoors_CoDriver', '', -2, 'VEHICLE', 1, 'green', 1000),
       (12, 'Дверь на Ниву', 'HatchbackDoors_Driver', '', -2, 'VEHICLE', 1, 'green', 1100),
       (13, 'Капот на Ниву', 'HatchbackHood', '', -2, 'VEHICLE', 1, 'green', 1200),
       (14, 'Ляда на Ниву', 'HatchbackTrunk', '', -2, 'VEHICLE', 1, 'green', 1300),
       (15, 'Колесо на Ниву', 'HatchbackWheel',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037183066579402836/unknown.png', -2, 'VEHICLE', 1,
        null, 1400),
       (19, 'Красный Golf', 'Hatchback_02',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037154422641397770/5555555555555555555.png', -2,
        'VEHICLE', 1, 'red', 1500),
       (20, 'Дверь на Golf', 'Hatchback_02_Door_2_1', '', -2, 'VEHICLE', 1, 'red', 1600),
       (21, 'Дверь на Golf', 'Hatchback_02_Door_2_2', '', -2, 'VEHICLE', 1, 'red', 1700),
       (22, 'Дверь на Golf', 'Hatchback_02_Door_1_1', '', -2, 'VEHICLE', 1, 'red', 1800),
       (23, 'Дверь на Golf', 'Hatchback_02_Door_1_2', '', -2, 'VEHICLE', 1, 'red', 1900),
       (24, 'Ляда на Golf', 'Hatchback_02_Trunk', '', -2, 'VEHICLE', 1, null, 2000),
       (25, 'Капот на Golf', 'Hatchback_02_Hood', '', -2, 'VEHICLE', 1, 'red', 2100),
       (26, 'Колесо на Golf', 'Hatchback_02_Wheel',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037182573127938128/unknown.png', -2, 'VEHICLE', 1,
        null, 2200),
       (27, 'Белая нива', 'OffroadHatchback_White',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037170785187528724/unknown.png', -2, 'VEHICLE', 1,
        'white', 2300),
       (28, 'Дверь на Ниву', 'HatchbackDoors_CoDriver_White', '', -2, 'VEHICLE', 1, 'white', 2400),
       (29, 'Дверь на Ниву', 'HatchbackDoors_Driver_White', '', -2, 'VEHICLE', 1, 'white', 2500),
       (30, 'Капот на Ниву', 'HatchbackHood_White', '', -2, 'VEHICLE', 1, 'white', 2600),
       (31, 'Ляда на Ниву', 'HatchbackTrunk_White', '', -2, 'VEHICLE', 1, 'white', 2700),
       (32, 'Жёлтая Skoda', 'Sedan_02_Yellow',
        'https://cdn.discordapp.com/attachments/952625703558475846/1038085410338902068/unknown.png', -2, 'VEHICLE', 1,
        'white', 2800),
       (33, 'Дверь на Skoda', 'Sedan_02_Door_2_1', '', -2, 'VEHICLE', 1, 'yellow', 2900),
       (34, 'Дверь на Skoda', 'Sedan_02_Door_2_2', '', -2, 'VEHICLE', 1, 'yellow', 3000),
       (35, 'Дверь на Skoda', 'Sedan_02_Door_1_1', '', -2, 'VEHICLE', 1, 'yellow', 3100),
       (36, 'Дверь на Skoda', 'Sedan_02_Door_1_2', '', -2, 'VEHICLE', 1, 'yellow', 3200),
       (37, 'Капот на Skoda', 'Sedan_02_Hood', '', -2, 'VEHICLE', 1, 'yellow', 3300),
       (38, 'Ляда на Skoda', 'Sedan_02_Trunk', '', -2, 'VEHICLE', 1, 'yellow', 3400),
       (39, 'Колесо на Skoda', 'Sedan_02_Wheel',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037181923518328922/unknown.png', -2, 'VEHICLE', 1,
        null, 3500),
       (40, 'Белая Волга', 'CivilianSedan',
        'https://cdn.discordapp.com/attachments/952625703558475846/1038104667525218404/unknown.png', -2, 'VEHICLE', 1,
        'white', 3600),
       (41, 'Дверь на Волгу', 'CivSedanDoors_Driver', '', -2, 'VEHICLE', 1, 'white', 3700),
       (42, 'Дверь на Волгу', 'CivSedanDoors_CoDriver', '', -2, 'VEHICLE', 1, 'white', 3800),
       (43, 'Дверь на Волгу', 'CivSedanDoors_BackRight', '', -2, 'VEHICLE', 1, 'white', 3900),
       (44, 'Дверь на Волгу', 'CivSedanDoors_BackLeft', '', -2, 'VEHICLE', 1, 'white', 4000),
       (45, 'Капот на Волгу', 'CivSedanHood', '', -2, 'VEHICLE', 1, 'white', 4100),
       (46, 'Ляда на Волгу', 'CivSedanTrunk', '', -2, 'VEHICLE', 1, 'white', 4200),
       (47, 'Колесо на Волгу', 'CivSedanWheel',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037181447364157550/unknown.png', -2, 'VEHICLE', 1,
        null, 4300),
       (48, 'Хаки V3S', 'Truck_01_Covered',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037164091011244092/unknown.png', -2, 'VEHICLE', 1,
        'khaki', 4400),
       (49, 'Дверь на V3S', 'Truck_01_Door_1_1', '', -2, 'VEHICLE', 1, 'khaki', 4500),
       (50, 'Дверь на V3S', 'Truck_01_Door_2_1', '', -2, 'VEHICLE', 1, 'khaki', 4600),
       (51, 'Капот на V3S', 'Truck_01_Hood', '', -2, 'VEHICLE', 1, 'khaki', 4700),
       (52, 'Колесо на V3S', 'Truck_01_Wheel', '', -2, 'VEHICLE', 1, null, 4800),
       (53, 'Колесо на V3S', 'Truck_01_WheelDouble', '', -2, 'VEHICLE', 1, null, 4900),
       (54, 'Приоритет на 30 дней', '',
        'https://cdn.discordapp.com/attachments/934733103589654558/1037151804904980500/unknown.png', -2, 'VIP', 1, '30',
        5000),
       (55, 'Приоритет на 60 дней', '',
        'https://cdn.discordapp.com/attachments/934733103589654558/1037151804904980500/unknown.png', -2, 'VIP', 1, '60',
        5100),
       (56, 'Set 101', '101',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5200),
       (57, 'Set 102', '102',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5300),
       (58, 'Set 103', '103',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5400),
       (59, 'Set 104', '104',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5500),
       (60, 'Set 105', '105',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5600),
       (61, 'Set 106', '106',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5700),
       (62, 'Set 107', '107',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5800),
       (63, 'Set 108', '108',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        5900),
       (64, 'Set 109', '109',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        6000),
       (65, 'Set 110', '110',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        6100),
       (66, 'Set 111', '111',
        'https://cdn.discordapp.com/attachments/952625703558475846/1037475695334523050/unknown.png', -2, 'SET', 1, '30',
        6200);



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (1, 50.00, 'RUB', 1, -2),
       (2, 50.00, 'RUB', 2, -2),
       (3, 50.00, 'RUB', 3, -2),
       (4, 80.00, 'RUB', 4, -2),
       (5, 80.00, 'RUB', 5, -2),
       (6, 150.00, 'RUB', 6, -2),
       (7, 10.00, 'RUB', 7, -2),
       (8, 250.00, 'RUB', 8, -2),
       (9, 50.00, 'RUB', 9, -2),
       (11, 10.00, 'RUB', 11, -2),
       (12, 10.00, 'RUB', 12, -2),
       (13, 10.00, 'RUB', 13, -2),
       (14, 10.00, 'RUB', 14, -2),
       (15, 25.00, 'RUB', 15, -2);

INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (19, 150.00, 'RUB', 19, -2),
       (21, 10.00, 'RUB', 21, -2),
       (22, 10.00, 'RUB', 22, -2),
       (23, 10.00, 'RUB', 23, -2),
       (24, 10.00, 'RUB', 24, -2),
       (25, 10.00, 'RUB', 25, -2),
       (26, 25.00, 'RUB', 26, -2);

INSERT INTO shop.servers (SERVER_ID, STORE_ID, SERVER_NAME, INSTANCE_NAME)
VALUES (1, -2, '1PP', '1PP'),
       (2, -2, 'LITE', 'LITE'),
       (3, -2, '3PP', 'CHERNO_3PP'),
       (4, -2, 'CASUAL', 'CASUAL'),
       (5, -2, 'CASUAL 24/7', 'CASUAL2'),
       (6, -2, 'WINTER', 'WINTER'),
       (7, -2, 'LIVONIA', 'LIVONIA');

INSERT INTO shop.store_config (STORE_ID, `KEY`, VALUE)
values (-2, 'checkRealCharges', true),
       (-2, 'realChargesThreshold', 1),
       (-2, 'steam.api.key', 'DF21B88E007F7F971EE63BFA733BB812'),
       (-2, 'yoomoney.client_id', '283B42B550A5600978948FF5B6F48F1980E8C374C8B1F9F42FD867F80D0BE356'),
       (-2, 'yoomoney.client_secret',
        '5CD09F538D6F2D0C43A0B389A252B835C67109174C778C709ED7AC306C3B824B2F5945880B3B6381C4D51B0A3967D49639D45C4A0D0D42CD7ED54145EA8245D9'),
       (-2, 'yoomoney.app_name', 'Alcatraz Donation'),
       (-2, 'default.image',
        'https://media.discordapp.net/attachments/925072921125351454/1056336180188299274/missing.png?width=676&height=676'),
       (-2, 'freekassa.api_key', 'c7d8f2117410ec8d40a00b077d3b9bd9'),
       (-2, 'freekassa.secret', 'TC*bn0XfesGM8cC'),
       (-2, 'freekassa.secret2', 'toi(/(Ni7LxsCfm'),
       (-2, 'freekassa.merchantId', '25874'),
       (-2, 'freekassa.baseUrl', 'https://pay.freekassa.ru/'),
       (-2, 'freekassa.ips', '168.119.157.136,168.119.60.227,138.201.88.124,178.154.197.79'),
       (-2, 'known_hosts', '~/.ssh/known_hosts');

INSERT INTO shop.server_config (SERVER_ID, `KEY`, VALUE, STORE_ID)
VALUES (1, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (1, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (1, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (1, 'SSH_IP', '195.18.27.92', -2),
       (1, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (1, 'SSH_USR', 'Constantine', -2),
       (2, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (2, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (2, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (2, 'SSH_IP', '195.18.27.92', -2),
       (2, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (2, 'SSH_USR', 'Constantine', -2),
       (3, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (3, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (3, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (3, 'SSH_IP', '195.18.27.91', -2),
       (3, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (3, 'SSH_USR', 'Constantine', -2),
       (4, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (4, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (4, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (4, 'SSH_IP', '195.18.27.91', -2),
       (4, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (4, 'SSH_USR', 'Constantine', -2),
       (5, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (5, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (5, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (5, 'SSH_IP', '185.189.255.245', -2),
       (5, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (5, 'SSH_USR', 'Constantine', -2),
       (6, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (6, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (6, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (6, 'SSH_IP', '185.189.255.245', -2),
       (6, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (6, 'SSH_USR', 'Constantine', -2),
       (7, 'PATH_TO_JSON', 'servers/%s/profiles/HotlineTrade/%s.json', -2),
       (7, 'PATH_TO_SET', 'servers/%s/profiles/%s', -2),
       (7, 'PATH_TO_VIP', 'servers/%s/%s', -2),
       (7, 'SSH_IP', '195.18.27.91', -2),
       (7, 'SSH_PWD', 'Alcatrazshop1221', -2),
       (7, 'SSH_USR', 'Constantine', -2);



INSERT INTO shop.categories (CATEGORY_ID, CATEGORY_NAME, DISPLAY_NAME)
VALUES (1, 'all', 'Все'),
       (107, 'vip', 'Услуги'),
       (108, 'clothes', 'Экипировка'),
       (109, 'vehicles', 'Транспортные средства'),
       (110, 'building', 'Стройматериалы'),
       (111, 'containers', 'Контейнеры')
;



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (1, -2, 'buyable', 'true'),
       (2, -2, 'buyable', 'true'),
       (3, -2, 'buyable', 'true'),
       (4, -2, 'buyable', 'true'),
       (5, -2, 'buyable', 'true'),
       (6, -2, 'buyable', 'true'),
       (8, -2, 'buyable', 'true'),
       (9, -2, 'buyable', 'true'),
       (19, -2, 'buyable', 'true'),
       (26, -2, 'buyable', 'true');



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (27, 150.00, 'RUB', 27, -2),
       (28, 10.00, 'RUB', 28, -2),
       (29, 10.00, 'RUB', 29, -2),
       (30, 10.00, 'RUB', 30, -2),
       (31, 10.00, 'RUB', 31, -2);



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (27, -2, 'buyable', 'true');



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (32, 140.00, 'RUB', 32, -2),
       (33, 10.00, 'RUB', 33, -2),
       (34, 10.00, 'RUB', 34, -2),
       (35, 10.00, 'RUB', 35, -2),
       (36, 10.00, 'RUB', 36, -2),
       (37, 10.00, 'RUB', 37, -2),
       (38, 10.00, 'RUB', 38, -2),
       (39, 25.00, 'RUB', 39, -2);



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (32, -2, 'buyable', 'true'),
       (39, -2, 'buyable', 'true');



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (40, 120.00, 'RUB', 40, -2),
       (41, 10.00, 'RUB', 41, -2),
       (42, 10.00, 'RUB', 42, -2),
       (43, 10.00, 'RUB', 43, -2),
       (44, 10.00, 'RUB', 44, -2),
       (45, 10.00, 'RUB', 45, -2),
       (46, 10.00, 'RUB', 46, -2),
       (47, 25.00, 'RUB', 47, -2);



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (40, -2, 'buyable', 'true'),
       (47, -2, 'buyable', 'true');



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (48, 200.00, 'RUB', 48, -2),
       (49, 10.00, 'RUB', 49, -2),
       (50, 10.00, 'RUB', 50, -2),
       (51, 10.00, 'RUB', 51, -2),
       (52, 10.00, 'RUB', 52, -2),
       (53, 10.00, 'RUB', 53, -2);



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (48, -2, 'buyable', 'true');



INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (54, -2, 'buyable', 'true'),
       (55, -2, 'buyable', 'true'),
       (56, -2, 'buyable', 'true'),
       (57, -2, 'buyable', 'true'),
       (58, -2, 'buyable', 'true'),
       (59, -2, 'buyable', 'true'),
       (60, -2, 'buyable', 'true'),
       (61, -2, 'buyable', 'true'),
       (62, -2, 'buyable', 'true'),
       (63, -2, 'buyable', 'true'),
       (64, -2, 'buyable', 'true'),
       (65, -2, 'buyable', 'true'),
       (66, -2, 'buyable', 'true')
;



INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (54, 300.00, 'RUB', 54, -2),
       (55, 600.00, 'RUB', 55, -2),
       (56, 250.00, 'RUB', 56, -2),
       (57, 250.00, 'RUB', 57, -2),
       (58, 250.00, 'RUB', 58, -2),
       (59, 300.00, 'RUB', 59, -2),
       (60, 300.00, 'RUB', 60, -2),
       (61, 500.00, 'RUB', 61, -2),
       (62, 500.00, 'RUB', 62, -2),
       (63, 500.00, 'RUB', 63, -2),
       (64, 600.00, 'RUB', 64, -2),
       (65, 600.00, 'RUB', 65, -2),
       (66, 600.00, 'RUB', 66, -2)
;

INSERT INTO shop.item_category (ITEM_ID, CATEGORY_ID)
VALUES (1, 109),
       (2, 109),
       (3, 109),
       (4, 110),
       (5, 110),
       (6, 111),
       (7, 109),
       (8, 109),
       (9, 109),
       (11, 109),
       (12, 109),
       (13, 109),
       (14, 109),
       (15, 109),
       (19, 109),
       (20, 109),
       (21, 109),
       (22, 109),
       (23, 109),
       (24, 109),
       (25, 109),
       (26, 109),
       (27, 109),
       (28, 109),
       (29, 109),
       (30, 109),
       (31, 109),
       (32, 109),
       (33, 109),
       (34, 109),
       (35, 109),
       (36, 109),
       (37, 109),
       (38, 109),
       (39, 109),
       (40, 109),
       (41, 109),
       (42, 109),
       (43, 109),
       (44, 109),
       (45, 109),
       (46, 109),
       (47, 109),
       (48, 109),
       (49, 109),
       (50, 109),
       (51, 109),
       (52, 109),
       (53, 109),
       (54, 107),
       (55, 107),
       (56, 108),
       (57, 108),
       (58, 108),
       (59, 108),
       (60, 108),
       (61, 108),
       (62, 108),
       (63, 108),
       (64, 108),
       (65, 108),
       (66, 108),
       (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (19, 1),
       (20, 1),
       (21, 1),
       (22, 1),
       (23, 1),
       (24, 1),
       (25, 1),
       (26, 1),
       (27, 1),
       (28, 1),
       (29, 1),
       (30, 1),
       (31, 1),
       (32, 1),
       (33, 1),
       (34, 1),
       (35, 1),
       (36, 1),
       (37, 1),
       (38, 1),
       (39, 1),
       (40, 1),
       (41, 1),
       (42, 1),
       (43, 1),
       (44, 1),
       (45, 1),
       (46, 1),
       (47, 1),
       (48, 1),
       (49, 1),
       (50, 1),
       (51, 1),
       (52, 1),
       (53, 1),
       (54, 1),
       (55, 1),
       (56, 1),
       (57, 1),
       (58, 1),
       (59, 1),
       (60, 1),
       (61, 1),
       (62, 1),
       (63, 1),
       (64, 1),
       (65, 1),
       (66, 1);



INSERT INTO shop.items (ITEM_ID, ITEM_NAME, IN_GAME_ID, IMAGE_URL, STORE_ID, ITEM_TYPE, COUNT, COLOR)
VALUES (67, 'Стройка', '', 'https://cdn.discordapp.com/attachments/952625703558475846/1037178953154166824/unknown.png',
        -2, 'ITEM', 1, null);

INSERT INTO item_attributes (ITEM_ID, STORE_ID, ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
VALUES (67, -2, 'buyable', 'true');

INSERT INTO shop.item_category (ITEM_ID, CATEGORY_ID)
VALUES (67, 110),
       (67, 1);

INSERT INTO shop.list_price (LISTPRICE_ID, PRICE, CURRENCY, ITEM_ID, STORE_ID)
VALUES (67, 200.00, 'RUB', 67, -2);

insert into shop.sub_items (ITEM_ID, SUB_ITEM_ID, QUANTITY)
values (8, 1, 1),
       (8, 2, 1),
       (8, 3, 1),
       (8, 7, 2),
       (8, 11, 1),
       (8, 12, 1),
       (8, 13, 1),
       (8, 14, 1),
       (8, 15, 5),
       (19, 1, 1),
       (19, 2, 1),
       (19, 3, 1),
       (19, 7, 2),
       (19, 20, 1),
       (19, 21, 1),
       (19, 22, 1),
       (19, 23, 1),
       (19, 24, 1),
       (19, 25, 1),
       (19, 26, 5),
       (27, 1, 1),
       (27, 2, 1),
       (27, 3, 1),
       (27, 7, 2),
       (27, 15, 5),
       (27, 29, 1),
       (27, 30, 1),
       (27, 31, 1),
       (32, 1, 1),
       (32, 2, 1),
       (32, 3, 1),
       (32, 7, 2),
       (32, 33, 1),
       (32, 34, 1),
       (32, 35, 1),
       (32, 36, 1),
       (32, 37, 1),
       (32, 38, 1),
       (32, 39, 5),
       (40, 1, 1),
       (40, 2, 1),
       (40, 3, 1),
       (40, 7, 2),
       (40, 41, 1),
       (40, 42, 1),
       (40, 43, 1),
       (40, 44, 1),
       (40, 45, 1),
       (40, 46, 1),
       (40, 47, 5),
       (48, 7, 2),
       (48, 9, 1),
       (48, 49, 1),
       (48, 50, 1),
       (48, 51, 1),
       (48, 52, 4),
       (48, 53, 4);

insert into item_server_buyable(item_id, server_id)
select item_id, server_id
from item_attributes,
     servers
where ATTRIBUTE_NAME = 'buyable'
  and ATTRIBUTE_VALUE = 'true';

delete
from item_attributes
where ATTRIBUTE_NAME = 'buyable';