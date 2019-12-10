
truncate table post_key_conf;
COPY post_key_conf(Amnt_local_Type,doc_type_code,post_key) 
FROM '/home/jcmendozav/Java_windows/java_tools/BatchInvoice/config/post_key_conf.txt' DELIMITER ',' CSV HEADER;
