
CREATE SEQUENCE  invoice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: invoice; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE  invoice (
    id integer DEFAULT invoice_id_seq.nextval NOT NULL,
    invoice_id character varying(16),
    issue_date character varying(16),
    issue_time character varying(16),
    party_id character varying(16),
    vendor_id character varying(16),
    custom_serie character varying(16),
    number_serie character varying(16),
    issue_time_stamp character varying(32),
    currency_code character varying(8),
    tax_code character varying(8),
    lea numeric(10,4),
    ta numeric(10,4),
    pa numeric(10,4),
    status numeric DEFAULT 0,
    file_id numeric,
    job_execution_id numeric,
    invoice_type_code character varying(8),
    doc_date timestamp with time zone,
    last_update_date timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    proc_status numeric DEFAULT 0,
    proc_desc character varying(256)
);



--
-- Name: invoice_file_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE  invoice_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: invoice_file_job; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE  invoice_file_job (
    id integer DEFAULT invoice_file_id_seq.nextval NOT NULL,
    file_name character varying(128),
    file_path character varying(512),
    uuid character varying(128),
    job_id character varying(16),
    lines numeric,
    size numeric,
    object_counter numeric DEFAULT 0,
    status numeric DEFAULT 0,
    status_desc character varying(512),
    last_update_date timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);



--
-- Name: people_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

--
-- Name: post_key_conf_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE  post_key_conf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: post_key_conf; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE  post_key_conf (
    id integer DEFAULT post_key_conf_id_seq.nextval NOT NULL,
    amnt_local_type character varying(16),
    doc_type_code character varying(16),
    post_key character varying(16),
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);



--
-- Name: vendor_map_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE  vendor_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: vendor_map; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE  vendor_map (
    id integer DEFAULT vendor_map_id_seq.nextval NOT NULL,
    party_id character varying(16),
    vendor_id character varying(16),
    vendor_name character varying(128),
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
