CREATE TABLE event
(
    event_id    int IDENTITY (1, 1) NOT NULL,
    name        varchar(255),
    location    varchar(255),
    date        datetime,
    category    varchar(255),
    description varchar(255),
    image_url   varchar(255),
    CONSTRAINT pk_event PRIMARY KEY (event_id)
)
    GO