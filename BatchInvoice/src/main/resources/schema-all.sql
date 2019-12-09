
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
    invoice_id VARCHAR(16),
    issue_date VARCHAR(16),
    issue_time VARCHAR(16),
    party_id VARCHAR(16),
	vendor_id VARCHAR(16),
	custom_serie VARCHAR(16),
	number_serie VARCHAR(16),
	issue_time_stamp VARCHAR(32),
	currency_code VARCHAR(8),
	lea NUMERIC (10, 4),
	ta NUMERIC (10, 4),
	pa NUMERIC (10, 4),
	status numeric default 0, 
    creationdate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);