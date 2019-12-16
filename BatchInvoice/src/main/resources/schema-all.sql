
CREATE SEQUENCE people_id_seq;
ALTER SEQUENCE people_id_seq
OWNED BY people.id;

DROP TABLE  IF EXISTS people;

CREATE TABLE people  (
    person_id integer NOT NULL DEFAULT nextval('people_id_seq') PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


psql -h localhost -p 5432 -U batchuser -d batch



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
	tax_code VARCHAR(8),
	lea NUMERIC (10, 4),
	ta NUMERIC (10, 4),
	pa NUMERIC (10, 4),
	status numeric default 0, 
	file_id numeric, 
	job_execution_id numeric, 
	invoice_type_code VARCHAR(8),
	doc_date TIMESTAMP WITH TIME ZONE ,
	last_update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    creationdate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE invoice ADD COLUMN tax_code VARCHAR(8);
ALTER TABLE invoice ADD COLUMN invoice_type_code VARCHAR(8);
ALTER TABLE invoice ADD COLUMN doc_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE invoice ADD COLUMN job_execution_id numeric;

select 
custom_serie,issue_date,issue_time,number_serie,ta,pa,lea,party_id

from(
select * from invoice where creationdate>=current_Date
) t;

EXTRA TABLES:

post_key_conf
Amnt_local_Type,doc_type_code,post_key
sub-total,01,30
igv,01,30
total,01,41
sub-total,07,50
igv,07,50
total,07,21

CREATE SEQUENCE post_key_conf_id_seq;

DROP TABLE  IF EXISTS post_key_conf;

CREATE TABLE post_key_conf  (
    ID integer NOT NULL DEFAULT nextval('post_key_conf_id_seq') PRIMARY KEY,
    Amnt_local_Type VARCHAR(16),
    doc_type_code VARCHAR(16),
    post_key VARCHAR(16),
    creationdate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unq_post_conf UNIQUE(Amnt_local_Type,doc_type_code,post_key)
);

truncate post_key_conf;
\COPY post_key_conf(Amnt_local_Type,doc_type_code,post_key) FROM '/home/jcmendozav/Java_windows/java_tools/BatchInvoice/config/post_key_conf.txt' DELIMITER ',' CSV HEADER;

vendor_map
party_id,vendor_id,vendor_name
10100017491,EEE-001,n1
20100017491,EEE-002,n2
30100017491,EEE-003,n3

CREATE SEQUENCE vendor_map_id_seq;

DROP TABLE  IF EXISTS vendor_map;

CREATE TABLE vendor_map  (
    ID integer NOT NULL DEFAULT nextval('vendor_map_id_seq') PRIMARY KEY,
    party_id VARCHAR(16),
    vendor_id VARCHAR(16),
    vendor_name varchar(128),
    creationdate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unq_vendor_map UNIQUE(party_id,vendor_id)
    

);


truncate vendor_map;
\COPY vendor_map(vendor_name,party_id,vendor_id)  FROM '/home/jcmendozav/Java_windows/java_tools/BatchInvoice/config/vendor_map.txt' DELIMITER ',' CSV HEADER ENCODING 'WIN1252';


account_mapping


CREATE SEQUENCE invoice_file_id_seq;

DROP TABLE  IF EXISTS invoice_file_job;

CREATE TABLE invoice_file_job  (
    ID integer NOT NULL DEFAULT nextval('invoice_file_id_seq')  ,
    file_name VARCHAR(128),
    file_path VARCHAR(512),
    uuid VARCHAR(128),
    job_id VARCHAR(16),
    lines numeric,
    size numeric,
    object_counter numeric DEFAULT 0,
    status numeric DEFAULT 0,
	status_desc VARCHAR(512),
	last_update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    creationdate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT unq_invoice_file_job PRIMARY KEY (ID )
    
    --,
    --CONSTRAINT unq_invoice_file_job UNIQUE(ID,uuid,job_id)
);







select vm.vendor_id
,pkc.post_key
,iv.Doc_Date 
,iv.Posting_Date 
,iv.Period 
,iv.ref_no 
,iv.doc_hdr_txt 
,iv.Account 
,iv.Amnt_Doc_Curr 
,iv.Amnt_local_Type 
,file_id
FROM (
 SELECT ROW_NUMBER() OVER (ORDER BY ID) AS group,  
unnest(array[ID, ID, ID]) AS ID,  
unnest(array[file_id, file_id, file_id]) AS file_id,  
unnest(array[issue_date, issue_date, issue_date]) AS Doc_Date,  
unnest(array[current_Date, current_Date, current_Date]) AS Posting_Date,  
unnest(array[date_part('month', doc_date), date_part('month', doc_date), date_part('month', doc_date)]) AS Period,  
unnest(array[number_serie, number_serie, number_serie]) AS ref_no,  
unnest(array[custom_serie, custom_serie, custom_serie]) AS doc_hdr_txt,  
unnest(array['TBD', 'TBD','TBD']) AS Account,  
unnest(array[issue_date, issue_date, issue_date]) AS Base_Date,  
--unnest(array['TBD', 'TBD','TBD']) AS CCntr,  
--unnest(array['TBD', 'TBD','TBD']) AS Assign_no,  
--unnest(array['TBD', 'TBD','TBD']) AS itm_txt,  
unnest(array[party_id, party_id,party_id]) AS party_id,  
unnest(array[invoice_type_code, invoice_type_code,invoice_type_code]) AS invoice_type_code,
unnest(array[ta, lea,pa ]) AS Amnt_Doc_Curr,
unnest(array['igv', 'sub-total','total']) AS Amnt_local_Type  
 from invoice
 where 1=1
 and creationdate>=current_Date-1
 ) as iv
 , vendor_map as vm
 , post_key_conf as pkc
where 1=1
and iv.party_id = vm.party_id
and iv.Amnt_local_Type=pkc.Amnt_local_Type
and iv.invoice_type_code=pkc.doc_type_code
ORDER BY 1, 2 ;  




