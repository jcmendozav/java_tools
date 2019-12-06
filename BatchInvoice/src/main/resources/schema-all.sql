
CREATE SEQUENCE people_id_seq;
ALTER SEQUENCE people_id_seq
OWNED BY people.id;

DROP TABLE  IF EXISTS people;

CREATE TABLE people  (
    person_id integer NOT NULL DEFAULT nextval('people_id_seq') PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


CREATE SEQUENCE invoice_id_seq;


DROP TABLE  IF EXISTS invoice;

CREATE TABLE invoice  (
    ID integer NOT NULL DEFAULT nextval('invoice_id_seq') PRIMARY KEY,
    invoice_id VARCHAR(20),
    issue_date VARCHAR(20),
    issue_time VARCHAR(20)
);