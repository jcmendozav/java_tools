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

--
-- Data for Name: post_key_conf; Type: TABLE DATA; Schema: public; Owner: batchuser
--

INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (8, 'sub-total', '01', '30', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (9, 'igv', '01', '30', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (10, 'total', '01', '41', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (11, 'sub-total', '08', '30', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (12, 'igv', '08', '30', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (13, 'total', '08', '41', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (14, 'sub-total', '07', '50', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (15, 'igv', '07', '50', '2019-12-16 17:11:13.405677-05');
INSERT INTO public.post_key_conf (id, amnt_local_type, doc_type_code, post_key, creationdate) VALUES (16, 'total', '07', '21', '2019-12-16 17:11:13.405677-05');


--
-- Data for Name: vendor_map; Type: TABLE DATA; Schema: public; Owner: batchuser
--

INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (118, '20467534026', '2000073993', 'AMERICA MOVIL PERU SAC', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (119, '20100017491', '2000074027', 'TELEFONICA DEL PERU SAA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (120, '20106897914', '2000082955', 'ENTEL PERU S.A', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (121, '20428698569', '2000082950', 'AMERICATEL PERU SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (122, '20390625007', '2000129161', 'ACE SEGUROS S.A.', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (123, '20100103738', '2000082021', 'K L M CIA REAL HOLANDESA DE', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (124, '20127765279', '2000079279', 'COESTI SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (125, '20307246741', '2000073939', 'AEROVIAS DE MEXICO SA DE CV', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (126, '20100142041', '2000148162', 'SOCIETE AIR FRANCE', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (127, '13-3133497', '2000049847', 'AMERICAN EXPRESS', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (128, '20100989710', '2000073965', 'LINEAS AEREAS COSTARRICENSES SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (129, '20512453857', '2000153213', 'AIR EUROPA LINEAS AEREAS', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (130, '20342868844', '2000073978', 'STAR UP SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (131, '20269985900', '2000168163', 'ENEL DISTRIBUCIÓN PERÚ S.A.A.', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (132, '20100035392', '2000074006', 'EL PACIFICO PERUANO SUIZA COMPANIA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (133, '20332970411', '2000074007', 'PACIFICO COMPAÑIA DE SEGUROS Y REASEGUROS SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (134, '20511038821', '2000127798', 'TM GESTION INMOBILIARIA S A C', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (135, '20100041953', '2000084524', 'RIMAC SEGUROS Y REASEGUROS', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (136, '20512293639', '2000129307', 'CORPORACION MG SAC', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (137, '20100103657', '2000073962', 'LATAM AIRLINES GROUP S.A.', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (138, '20389395481', '2000172327', 'DHL GLOBAL FORWARDING', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (139, '20307328471', '2000074932', 'DHL GLOBAL FORWARDING', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (140, '20341841357', '2000073963', 'LAN PERU SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (141, '20131312955', '2000073980', 'SUPERINTENDENCIA NACIONAL DE', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (142, '20348858182', '2000073987', 'TRANS AMERICAN AIR LINES SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (143, '20297995635', '2000082150', 'CONTINENTAL AIRLINES INC SUCURSAL', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (144, '20101128777', '2000073952', 'DHL EXPRESS PERU SAC', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (145, '20431115825', '2000074025', 'PACIFICO SA ENTIDAD', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (146, '20100095379', '2000073970', 'PANALPINA TRANSPORTES MUNDIALES SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (147, 'B85284594', '2000116368', 'TELEFONICA COMPRAS ELECTRONICAS SL', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (148, '20100973392', '2000073938', 'AEROLINEAS ARGENTINAS SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (149, '20100129290', '2000073940', 'AEROVIAS DEL CONTINENTE AMERICANO', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (150, '20107751913', '2000073946', 'CARLSON WAGONLIT PERU SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (151, '20100041449', '2000073959', 'IBERIA LINEAS AEREAS DE ESPANA SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (152, '20331898008', '2000074020', 'LUZ DEL SUR SAA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (153, '20101070671', '2000081954', 'AMERICAN AIR LINES INC', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (154, '20100116121', '2000082017', 'LUFTHANSA LINEAS AEREAS ALEMANAS', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (155, '20258937784', '2000082154', 'COMPANIA PANAMENA DE AVIACION SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (156, '20518480554', '2000103158', 'TAM LINEAS AEREAS S.A.', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (157, '20426107041', '2000106748', 'PALACIOS & ASOCIADOS AGENTES', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (158, '20518042280', '2000114786', 'PERUVIAN AIR LINE SA', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (159, '20514606774', '2000135417', 'C & M SERVICENTROS SAC', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (160, '20502556381', '2000136280', 'DOCUMET BARDALES DE', '2019-12-16 17:10:35.091201-05');
INSERT INTO public.vendor_map (id, party_id, vendor_id, vendor_name, creationdate) VALUES (161, '20469748163', '2000148175', 'EMPRESA PUBLICA TAME LINEA AEREA', '2019-12-16 17:10:35.091201-05');


--
-- PostgreSQL database dump complete
--

