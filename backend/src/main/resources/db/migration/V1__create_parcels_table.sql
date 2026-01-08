create table parcels (
  id uuid primary key,
  reference varchar(50) not null unique,
  status varchar(30) not null,
  created_at timestamp with time zone not null,
  updated_at timestamp with time zone not null
);

create index idx_parcels_status on parcels(status);
create index idx_parcels_created_at on parcels(created_at);