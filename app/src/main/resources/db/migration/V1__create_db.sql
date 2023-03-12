create table product
(
    id          integer generated by default as identity,
    product_id  varchar(255),
    name        varchar(255),
    price       numeric,
    category    integer,
    supplier_id integer,
    primary key (id)
);
create table supplier
(
    id          integer generated by default as identity,
    supplier_id varchar(255),
    name        varchar(255),
    primary key (id)
);

ALTER TABLE product
    ADD FOREIGN KEY (supplier_id)
        REFERENCES supplier (id);