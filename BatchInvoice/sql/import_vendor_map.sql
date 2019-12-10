truncate table vendor_map;
COPY vendor_map(party_id,vendor_id) 
FROM '/home/jcmendozav/Java_windows/java_tools/BatchInvoice/config/vendor_map.txt' DELIMITER ',' CSV HEADER;
