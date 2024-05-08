CREATE TABLE device (
    device_name VARCHAR(255) NOT NULL,
    device_password VARCHAR(255) NOT NULL,
    PRIMARY KEY (device_name, device_password)
);