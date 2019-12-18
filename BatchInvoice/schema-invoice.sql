--
-- PostgreSQL database dump
--

-- Dumped from database version 11.5 (Ubuntu 11.5-1.pgdg18.04+1)
-- Dumped by pg_dump version 12.1 (Ubuntu 12.1-1.pgdg18.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

--
-- Name: batch_job_execution; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_job_execution (
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone,
    job_configuration_location character varying(2500)
);


ALTER TABLE public.batch_job_execution OWNER TO batchuser;

--
-- Name: batch_job_execution_context; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_job_execution_context (
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE public.batch_job_execution_context OWNER TO batchuser;

--
-- Name: batch_job_execution_params; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_job_execution_params (
    job_execution_id bigint NOT NULL,
    type_cd character varying(6) NOT NULL,
    key_name character varying(100) NOT NULL,
    string_val character varying(250),
    date_val timestamp without time zone,
    long_val bigint,
    double_val double precision,
    identifying character(1) NOT NULL
);


ALTER TABLE public.batch_job_execution_params OWNER TO batchuser;

--
-- Name: batch_job_execution_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.batch_job_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_job_execution_seq OWNER TO batchuser;

--
-- Name: batch_job_instance; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_job_instance (
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL
);


ALTER TABLE public.batch_job_instance OWNER TO batchuser;

--
-- Name: batch_job_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.batch_job_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_job_seq OWNER TO batchuser;

--
-- Name: batch_step_execution; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_step_execution (
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


ALTER TABLE public.batch_step_execution OWNER TO batchuser;

--
-- Name: batch_step_execution_context; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.batch_step_execution_context (
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE public.batch_step_execution_context OWNER TO batchuser;

--
-- Name: batch_step_execution_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.batch_step_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_step_execution_seq OWNER TO batchuser;

--
-- Name: invoice_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.invoice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoice_id_seq OWNER TO batchuser;

--
-- Name: invoice; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.invoice (
    id integer DEFAULT nextval('public.invoice_id_seq'::regclass) NOT NULL,
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


ALTER TABLE public.invoice OWNER TO batchuser;

--
-- Name: invoice_file_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.invoice_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoice_file_id_seq OWNER TO batchuser;

--
-- Name: invoice_file_job; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.invoice_file_job (
    id integer DEFAULT nextval('public.invoice_file_id_seq'::regclass) NOT NULL,
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


ALTER TABLE public.invoice_file_job OWNER TO batchuser;

--
-- Name: people_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.people_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.people_id_seq OWNER TO batchuser;

--
-- Name: people; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.people (
    person_id integer DEFAULT nextval('public.people_id_seq'::regclass) NOT NULL,
    first_name character varying(20),
    last_name character varying(20)
);


ALTER TABLE public.people OWNER TO batchuser;

--
-- Name: post_key_conf_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.post_key_conf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.post_key_conf_id_seq OWNER TO batchuser;

--
-- Name: post_key_conf; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.post_key_conf (
    id integer DEFAULT nextval('public.post_key_conf_id_seq'::regclass) NOT NULL,
    amnt_local_type character varying(16),
    doc_type_code character varying(16),
    post_key character varying(16),
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.post_key_conf OWNER TO batchuser;

--
-- Name: vendor_map_id_seq; Type: SEQUENCE; Schema: public; Owner: batchuser
--

CREATE SEQUENCE public.vendor_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vendor_map_id_seq OWNER TO batchuser;

--
-- Name: vendor_map; Type: TABLE; Schema: public; Owner: batchuser
--

CREATE TABLE public.vendor_map (
    id integer DEFAULT nextval('public.vendor_map_id_seq'::regclass) NOT NULL,
    party_id character varying(16),
    vendor_id character varying(16),
    vendor_name character varying(128),
    creationdate timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.vendor_map OWNER TO batchuser;

--
-- Name: batch_job_execution_context batch_job_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_execution_context
    ADD CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_execution batch_job_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_execution
    ADD CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_instance batch_job_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_instance
    ADD CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id);


--
-- Name: batch_step_execution_context batch_step_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_step_execution_context
    ADD CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id);


--
-- Name: batch_step_execution batch_step_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_step_execution
    ADD CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id);


--
-- Name: invoice invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- Name: batch_job_instance job_inst_un; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);


--
-- Name: people people_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.people
    ADD CONSTRAINT people_pkey PRIMARY KEY (person_id);


--
-- Name: post_key_conf post_key_conf_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.post_key_conf
    ADD CONSTRAINT post_key_conf_pkey PRIMARY KEY (id);


--
-- Name: invoice_file_job unq_invoice_file_job; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.invoice_file_job
    ADD CONSTRAINT unq_invoice_file_job PRIMARY KEY (id);


--
-- Name: post_key_conf unq_post_conf; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.post_key_conf
    ADD CONSTRAINT unq_post_conf UNIQUE (amnt_local_type, doc_type_code, post_key);


--
-- Name: vendor_map unq_vendor_map; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.vendor_map
    ADD CONSTRAINT unq_vendor_map UNIQUE (party_id, vendor_id);


--
-- Name: vendor_map vendor_map_pkey; Type: CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.vendor_map
    ADD CONSTRAINT vendor_map_pkey PRIMARY KEY (id);


--
-- Name: batch_job_execution_context job_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- Name: batch_job_execution_params job_exec_params_fk; Type: FK CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- Name: batch_step_execution job_exec_step_fk; Type: FK CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- Name: batch_job_execution job_inst_exec_fk; Type: FK CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES public.batch_job_instance(job_instance_id);


--
-- Name: batch_step_execution_context step_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: batchuser
--

ALTER TABLE ONLY public.batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES public.batch_step_execution(step_execution_id);


--
-- PostgreSQL database dump complete
--

