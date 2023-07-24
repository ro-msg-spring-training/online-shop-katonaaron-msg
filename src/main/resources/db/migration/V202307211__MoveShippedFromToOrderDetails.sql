alter table order_detail
    add column shipped_from_id uuid;
alter table order_detail
    add constraint fk_shipped_from
    foreign key (shipped_from_id)
    references location(id);
alter table orders
    drop column shipped_from_id;