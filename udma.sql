
    drop table if exists Item;

    create table Item (
        id bigint not null,
        class varchar(255) not null,
        itemCode varchar(255),
        itemBytes tinyblob,
        primary key (id)
    );
