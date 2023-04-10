
CREATE SCHEMA IF NOT EXISTS stock;

CREATE TABLE IF NOT EXISTS stock.item (
    uuid varchar NOT NULL,
    name varchar NOT NULL,
    description varchar,
    price real
);

ALTER TABLE stock.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (uuid);
