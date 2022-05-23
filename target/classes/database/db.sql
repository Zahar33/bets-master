USE bets;
CREATE TABLE cash_accounts
(
    id      INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    balance DOUBLE
);

CREATE TABLE avatars
(
    id      INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    picture MEDIUMBLOB,
    date    DATETIME
);

CREATE TABLE conditions
(
    id             INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    conditionsName VARCHAR(255)        NOT NULL,
    coefficient    DOUBLE              NOT NULL,
    result         TINYINT(1)
);

CREATE TABLE matches
(
    id              INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sportsName      VARCHAR(255)        NOT NULL,
    leaguesName     VARCHAR(255)        NOT NULL,
    eventsDate      DATETIME            NOT NULL,
    firstSidesName  VARCHAR(255)        NOT NULL,
    secondSidesName VARCHAR(255)        NOT NULL,
    active          TINYINT(1)
);

CREATE TABLE bookmakers
(
    id        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255)        NOT NULL,
    lastName  VARCHAR(255)        NOT NULL,
    password  VARCHAR(255)        NOT NULL,
    email     VARCHAR(255)        NOT NULL,
    purse_id  INT(11)
);


CREATE TABLE customers
(
    id        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255)        NOT NULL,
    lastName  VARCHAR(255)        NOT NULL,
    password  VARCHAR(255)        NOT NULL,
    email     VARCHAR(255)        NOT NULL,
    active    TINYINT(1),
    purse_id  INT(11),
    avatar_id INT(11),
    CONSTRAINT customers_avatar_fk FOREIGN KEY (avatar_id) REFERENCES avatars (id),
    CONSTRAINT fk_purseId_cashAccountId FOREIGN KEY (purse_id) REFERENCES cash_accounts (id)
);
CREATE INDEX customers_avatar_fk ON customers (avatar_id);
CREATE INDEX fk_purseId_cashAccountId ON customers (purse_id);


CREATE TABLE matches_conditions
(
    match_id     INT(11),
    condition_id INT(11),
    CONSTRAINT matches_conditions_conditions_id_fk FOREIGN KEY (condition_id) REFERENCES conditions (id),
    CONSTRAINT matches_conditions_matches_id_fk FOREIGN KEY (match_id) REFERENCES matches (id)
);
CREATE INDEX matches_conditions_conditions_id_fk ON matches_conditions (condition_id);
CREATE INDEX matches_conditions_matches_id_fk ON matches_conditions (match_id);

CREATE TABLE bets
(
    id               INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    value            DOUBLE              NOT NULL,
    customer_id      INT(11)             NOT NULL,
    possibleGain     DOUBLE,
    finalCoefficient DOUBLE,
    finalResult      TINYINT(1),
    betsDate         DATETIME,
    CONSTRAINT bets_customers_id_fk FOREIGN KEY (customer_id) REFERENCES customers (id)
);
CREATE INDEX bets_customers_id_fk ON bets (customer_id);

CREATE TABLE customers_bets
(
    customer_id INT(11) NOT NULL,
    bets_id     INT(11) NOT NULL,
    CONSTRAINT customers_bets_bets_id_fk FOREIGN KEY (bets_id) REFERENCES bets (id),
    CONSTRAINT customers_bets_customers_id_fk FOREIGN KEY (customer_id) REFERENCES customers (id)
);
CREATE INDEX customers_bets_bets_id_fk ON customers_bets (bets_id);
CREATE INDEX customers_bets_customers_id_fk ON customers_bets (customer_id);

CREATE TABLE bets_conditions
(
    bets_id       INT(11) NOT NULL,
    conditions_id INT(11) NOT NULL,
    CONSTRAINT bets_conditions_bets_id_fk FOREIGN KEY (bets_id) REFERENCES bets (id),
    CONSTRAINT bets_conditions_conditions_id_fk FOREIGN KEY (conditions_id) REFERENCES conditions (id)
);
CREATE INDEX bets_conditions_bets_id_fk ON bets_conditions (bets_id);
CREATE INDEX bets_conditions_conditions_id_fk ON bets_conditions (conditions_id);

CREATE TABLE transfers_to_customers
(
    id            INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sender_id     INT(11),
    amount        DOUBLE              NOT NULL,
    recipient_id  INT(11)             NOT NULL,
    transfersTime DATETIME,
    CONSTRAINT transfers_to_customers_customers_id_fk FOREIGN KEY (recipient_id) REFERENCES customers (id)
);
CREATE INDEX transfers_to_customers_customers_id_fk ON transfers_to_customers (recipient_id);

CREATE TABLE transfers_to_bookmaker
(
    id            INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sender_id     INT(11),
    amount        DOUBLE              NOT NULL,
    recipient_id  INT(11)             NOT NULL,
    transfersTime DATETIME,
    CONSTRAINT transfers_to_bookmaker_bookmakers_id_fk FOREIGN KEY (recipient_id) REFERENCES bookmakers (id)
);
CREATE INDEX transfers_to_bookmaker_bookmakers_id_fk ON transfers_to_bookmaker (recipient_id);

INSERT INTO bets.cash_accounts VALUES (1, 300000);
INSERT INTO bets.bookmakers VALUES (1, 'Алексей', 'Пак', '1234567', 'qwe@mail.ru', 1);

INSERT INTO bets.matches VALUES (1, 'Football', 'Euro', '2022-04-28 19:00:00', 'England', 'Poland', 1);
INSERT INTO bets.conditions VALUES (1, 'England win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (2, 'Poland win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (1, 1);
INSERT INTO bets.matches_conditions VALUE (1, 2);
INSERT INTO bets.matches VALUES (2, 'Football', 'Euro', '2022-04-28 19:00:00', 'Albania', 'Austria', 1);
INSERT INTO bets.conditions VALUES (3, 'Albania win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (4, 'Austria win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (2, 3);
INSERT INTO bets.matches_conditions VALUE (2, 4);
INSERT INTO bets.matches VALUES (3, 'Football', 'Euro', '2022-04-28 19:00:00', 'Belgium', 'Croatia', 1);
INSERT INTO bets.conditions VALUES (5, 'Belgium win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (6, 'Croatia win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (3, 5);
INSERT INTO bets.matches_conditions VALUE (3, 6);
INSERT INTO bets.matches VALUES (4, 'Football', 'Euro', '2022-04-28 19:00:00', 'Czech', 'France', 1);
INSERT INTO bets.conditions VALUES (7, 'Czech win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (8, 'France win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (4, 7);
INSERT INTO bets.matches_conditions VALUE (4, 8);
INSERT INTO bets.matches VALUES (5, 'Football', 'Euro', '2022-04-28 19:00:00', 'Germany', 'Hungary', 1);
INSERT INTO bets.conditions VALUES (9, 'Germany win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (10, 'Hungary win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (5, 9);
INSERT INTO bets.matches_conditions VALUE (5, 10);
INSERT INTO bets.matches VALUES (6, 'Football', 'Euro', '2022-04-28 19:00:00', 'Iceland', 'Italy', 1);
INSERT INTO bets.conditions VALUES (11, 'Iceland win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (12, 'Italy win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (6, 11);
INSERT INTO bets.matches_conditions VALUE (6, 12);
INSERT INTO bets.matches VALUES (7, 'Football', 'Euro', '2022-04-28 19:00:00', 'Northern Ireland', 'Portugal', 1);
INSERT INTO bets.conditions VALUES (13, 'Northern Ireland win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (14, 'Portugal win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (7, 13);
INSERT INTO bets.matches_conditions VALUE (7, 14);
INSERT INTO bets.matches VALUES (8, 'Football', 'Euro', '2022-04-28 19:00:00', 'Ireland', 'Romania', 1);
INSERT INTO bets.conditions VALUES (15, 'Ireland win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (16, 'Romania win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (8, 15);
INSERT INTO bets.matches_conditions VALUE (8, 16);
INSERT INTO bets.matches VALUES (9, 'Football', 'Euro', '2022-04-28 19:00:00', 'Russia', 'Slovakia', 1);
INSERT INTO bets.conditions VALUES (17, 'Russia win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (18, 'Slovakia win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (9, 17);
INSERT INTO bets.matches_conditions VALUE (9, 18);
INSERT INTO bets.matches VALUES (10, 'Football', 'Euro', '2022-04-28 19:00:00', 'Spain', 'Sweden', 1);
INSERT INTO bets.conditions VALUES (19, 'Spain win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (20, 'Sweden win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (10, 19);
INSERT INTO bets.matches_conditions VALUE (10, 20);
INSERT INTO bets.matches VALUES (11, 'Football', 'Euro', '2022-04-28 19:00:00', 'Switzerland', 'Turkey', 1);
INSERT INTO bets.conditions VALUES (21, 'Switzerland win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (22, 'Turkey win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (11, 21);
INSERT INTO bets.matches_conditions VALUE (11, 22);
INSERT INTO bets.matches VALUES (12, 'Football', 'Euro', '2022-04-28 19:00:00', 'Ukraine', 'Wales', 1);
INSERT INTO bets.conditions VALUES (23, 'Ukraine win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (24, 'Wales win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (12, 23);
INSERT INTO bets.matches_conditions VALUE (12, 24);
INSERT INTO bets.matches VALUES (13, 'Football', 'Euro', '2022-04-28 19:00:00', 'Slovakia', 'Poland', 1);
INSERT INTO bets.conditions VALUES (25, 'Slovakia win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (26, 'Poland win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (13, 25);
INSERT INTO bets.matches_conditions VALUE (13, 26);
INSERT INTO bets.matches VALUES (14, 'Football', 'Euro', '2022-04-28 19:00:00', 'France', 'Ireland', 1);
INSERT INTO bets.conditions VALUES (27, 'France win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (28, 'Ireland win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (14, 27);
INSERT INTO bets.matches_conditions VALUE (14, 28);
INSERT INTO bets.matches VALUES (15, 'Football', 'Euro', '2022-04-28 19:00:00', 'England', 'Spain', 1);
INSERT INTO bets.conditions VALUES (29, 'England win', 1.3, NULL);
INSERT INTO bets.conditions VALUES (30, 'Spain win', 1.9, NULL);
INSERT INTO bets.matches_conditions VALUE (15, 29);
INSERT INTO bets.matches_conditions VALUE (15, 30);


INSERT INTO bets.cash_accounts VALUE (2, 200);
INSERT INTO bets.cash_accounts VALUE (3, 300);
INSERT INTO bets.cash_accounts VALUE (4, 400);
INSERT INTO bets.cash_accounts VALUE (5, 500);
INSERT INTO bets.cash_accounts VALUE (6, 600);
INSERT INTO bets.cash_accounts VALUE (7, 700);
INSERT INTO bets.cash_accounts VALUE (8, 800);
INSERT INTO bets.cash_accounts VALUE (9, 900);
INSERT INTO bets.cash_accounts VALUE (10, 1000);
INSERT INTO bets.cash_accounts VALUE (11, 1100);
INSERT INTO bets.cash_accounts VALUE (12, 1200);
INSERT INTO bets.cash_accounts VALUE (13, 1300);
INSERT INTO bets.cash_accounts VALUE (14, 1400);
INSERT INTO bets.cash_accounts VALUE (15, 1500);
INSERT INTO bets.cash_accounts VALUE (16, 1600);
INSERT INTO bets.cash_accounts VALUE (17, 1700);
INSERT INTO bets.cash_accounts VALUE (18, 1800);
INSERT INTO bets.cash_accounts VALUE (19, 1900);
INSERT INTO bets.cash_accounts VALUE (20, 2000);
INSERT INTO bets.cash_accounts VALUE (21, 10000);


INSERT INTO bets.customers VALUE (id, 'Родион', 'Ермолин', 'qweasd', 'erm@mail.ru', 1, 2, NULL);
INSERT INTO bets.customers VALUE (id, 'Алихан', 'Есмагамбетов', 'qweasd', 'esm@mail.ru', 1, 3, NULL);
INSERT INTO bets.customers VALUE (id, 'Илья', 'Бондаренко', 'qweasd', 'iiillliiiaaa@mail.ru', 1, 4, NULL);
INSERT INTO bets.customers VALUE (id, 'Владимир', 'Бондаренко', 'qweasd', 'bon@mail.ru', 1, 5, NULL);
INSERT INTO bets.customers VALUE (id, 'Сергей', 'Гарбар', 'qweasd', 'gar@mail.ru', 1, 6, NULL);
INSERT INTO bets.customers VALUE (id, 'Демьян', 'Колба', 'qweasd', 'kol@mail.ru', 1, 7, NULL);
INSERT INTO bets.customers VALUE (id, 'Даниил', 'Спирин', 'qweasd', 'spir@mail.ru', 1, 8, NULL);
INSERT INTO bets.customers VALUE (id, 'Дарья', 'Богинская', 'qweasd', 'bog@mail.ru', 1, 9, NULL);
INSERT INTO bets.customers VALUE (id, 'Виктория', 'Боровинская', 'qweasd', 'bor@mail.ru', 1, 10, NULL);
INSERT INTO bets.customers VALUE (id, 'Анастасия', 'Силантьева', 'qweasd', 'sil@mail.ru', 1, 11, NULL);
INSERT INTO bets.customers VALUE (id, 'Анастасия', 'Борисенкова', 'qweasd', 'boris@mail.ru', 1, 12, NULL);
INSERT INTO bets.customers VALUE (id, 'Александер', 'Васильев', 'qweasd', 'vas@mail.ru', 1, 13, NULL);
INSERT INTO bets.customers VALUE (id, 'Александра', 'Сухачёва', 'qweasd', 'suh@mail.ru', 1, 14, NULL);
INSERT INTO bets.customers VALUE (id, 'Елена', 'Воробей', 'qweasd', 'vor@mail.ru', 1, 15, NULL);
INSERT INTO bets.customers VALUE (id, 'Марина', 'Бабаева', 'qweasd', 'bab@mail.ru', 1, 16, NULL);
INSERT INTO bets.customers VALUE (id, 'Любовь', 'Колба', 'qweasd', 'lyub@mail.ru', 1, 17, NULL);
INSERT INTO bets.customers VALUE (id, 'Андрей', 'Аршавин', 'qweasd', 'arsh@mail.ru', 1, 18, NULL);
INSERT INTO bets.customers VALUE (id, 'Пётр', 'Первый', 'qweasd', 'perv@mail.ru', 1, 19, NULL);
INSERT INTO bets.customers VALUE (id, 'Игорь', 'Лапин', 'qweasd', 'lap@mail.ru', 1, 20, NULL);
INSERT INTO bets.customers VALUE (id, 'Иван', 'Данилов', 'qweasd', 'dan@mail.ru', 1, 21, NULL);
