INSERT INTO supplier (supplier_id,name)
            VALUES ('SUP0000001','Heineken');
INSERT INTO supplier (supplier_id,name)
            VALUES ('SUP0000002','AMBEV');
INSERT INTO supplier (supplier_id,name)
            VALUES ('SUP0000003','CRS BRANDS');

INSERT INTO product (product_id,name,price,category,supplier_id)
            VALUES ('PRD0000001','Heineken',4.99,0,1);
INSERT INTO product (product_id,name,price,category,supplier_id)
            VALUES ('PRD0000002','Amstel',3.99,0,1);
INSERT INTO product (product_id,name,price,category,supplier_id)
            VALUES ('PRD0000003','Itaipolvora',0.99,0,2);
INSERT INTO product (product_id,name,price,category,supplier_id)
            VALUES ('PRD0000004','Dom Bosco',2.99,1,3);