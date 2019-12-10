
host=localhost
db=batch
user=batchuser
port=5432
sql_config_path=/home/jcmendozav/Java_windows/java_tools/BatchInvoice/sql
cd $sql_config_path
#psql -h $host -d $db -U $user -p $port -f $sql_config_path/import_post_key.sql 
#psql -h $host -d $db -U $user -p $port -f $sql_config_path/import_vendor_map.sql

psql -h $host -d $db -U $user -c \
	"copy vendor_map(party_id,vendor_id) from STDIN with delimiter as ','" \
   < vendor_map.txt

psql -h $host -d $db -U $user -c \
	        "copy post_key_conf(Amnt_local_Type,doc_type_code,post_key) from STDIN with delimiter as ','" \
		   < post_key_conf.txt

#psql -U $user -h $host $db << EOF
#SELECT * FROM xyz_table;
#SELECT * FROM abc_table;
#EOF
