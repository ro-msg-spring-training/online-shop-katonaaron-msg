create table customer (
        id uuid not null,
        email_address varchar(255) UNIQUE,
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
        username varchar(255) UNIQUE,
        primary key (id)
);

create table location (
        id uuid not null,
        city varchar(255),
        country varchar(255),
        county varchar(255),
        name varchar(255),
        street_address varchar(255),
        primary key (id)
);

create table orders (
        created_at timestamp(6),
        customer_id uuid,
        id uuid not null,
        shipped_from_id uuid,
        city varchar(255),
        country varchar(255),
        county varchar(255),
        street_address varchar(255),
        primary key (id),
        constraint fk_customer
               foreign key (customer_id)
               references customer(id),
        constraint fk_location
               foreign key (shipped_from_id)
               references location(id)
);

create table product_category (
        id uuid not null,
        description varchar(255),
        name varchar(255),
        primary key (id)
);

create table supplier (
        id uuid not null,
        name varchar(255) UNIQUE,
        primary key (id)
);

create table product (
        price numeric(38,2),
        weight float(53),
        category_id uuid,
        id uuid not null,
        supplier_id uuid,
        description varchar(255),
        image_url varchar(255),
        name varchar(255),
        primary key (id),
        constraint fk_product_category
               foreign key (category_id)
               references product_category(id),
        constraint fk_supplier
               foreign key (supplier_id)
               references supplier(id)
);

create table order_detail (
        quantity integer,
        id uuid not null,
        order_id uuid,
        product_id uuid,
        primary key (id),
        unique(order_id, product_id),
        constraint fk_order
               foreign key (order_id)
               references orders(id),
        constraint fk_product
               foreign key (product_id)
               references product(id)
);

create table revenue (
        date date,
        sum numeric(38,2),
        id uuid not null,
        location_id uuid,
        primary key (id),
        constraint fk_location
               foreign key (location_id)
               references location(id)
);

create table stock (
        quantity integer,
        id uuid not null,
        location_id uuid,
        product_id uuid,
        primary key (id),
        unique(location_id, product_id),
        constraint fk_location
               foreign key (location_id)
               references location(id),
        constraint fk_product
               foreign key (product_id)
               references product(id)
);
