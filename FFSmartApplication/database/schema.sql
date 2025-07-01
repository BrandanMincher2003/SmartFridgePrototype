Create database if not exists FFSmartDB;

Create table if not exists Users (
    user_id int auto_increment primary key,
    username varchar(50) unique not null,
    password varchar(255) not null, -- Encrypted
    role enum('Admin', 'Head Chef', 'Chef', 'Delivery Person') not null,
	authority_level int not null check (authority_level between 1 and 4)
);

Create table if not exists Consumables (
    consumable_id int auto_increment primary key,
    name varchar(100) not null,
    quantity int not null,
    expiry_date date not null
);

Create table if not exists Supplier (
    supplier_id int auto_increment primary key,
    name varchar(100) not null,
    contact_info varchar(100) not null,
    address text not null,
    registration_date date not null,
    status varchar(50) not null
);

create table if not exists Settings (
    settings_id int auto_increment primary key,
    settings_file varchar(100) not null,
    language varchar(50) not null,
    power_mode boolean default true,
    light_settings varchar(100),
    auto_putaway boolean default false,
    voice_login_enabled boolean default false
);


Create table if not exists Fridge (
    fridge_id int auto_increment primary key,
    location varchar(100) not null,
    temperature double default 5.0,
    is_door_open boolean default false,
    settings_id int,
    foreign key (settings_id) references Settings(settings_id) on delete set null
);

Create table if not exists User_Fridges (
    user_id int,
    fridge_id int,
    primary key (user_id, fridge_id),
    foreign key (user_id) references Users(user_id) on delete cascade,
    foreign key (fridge_id) references Fridge(fridge_id) on delete cascade
);

Create table if not exists notifications (
	notification_id int auto_increment primary key,
    consumable_id int not null,
    reason enum('expiry_warning', 'low_stock_warning') not null,
    message text not null,
    timestamp timestamp default current_timestamp,
    foreign key (consumable_id) references consumables(consumable_id) on delete cascade
);

Create table if not exists orders (
	order_number int auto_increment primary key,
    item_name varchar(100) not null,
    quantity int not null,
    supplier varchar(100) not null
);
