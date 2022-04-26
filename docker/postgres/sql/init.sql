--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 14.0

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

SET default_table_access_method = heap;

--
-- Name: flight_call_signs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flight_call_signs (
    three_letter_id character varying(100),
    call_sign character varying(100),
    company character varying(100)
);


ALTER TABLE public.flight_call_signs OWNER TO postgres;

--
-- Data for Name: flight_call_signs; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('AAL', 'AMERICAN', 'AMERICAN AIRLINES INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('AAY', 'ALLEGIANT', 'ALLEGIANT AIR');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ACA', 'AIR CANADA', 'AIR CANADA');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('AIJ', 'ABC AEROLINEAS', 'ABC AEROLINEAS');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('AMX', 'AEROMEXICO', 'AEROVIAS DE MEXICO');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ANA', 'ALL NIPPON', 'ALL NIPPON AIRWAYS CO.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ASA', 'ALASKA', 'ALASKA AIRLINES INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ASH', 'AIR SHUTTLE', 'MESA AVIATION SERVICES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ATN', 'AIR TRANSPORT', 'AIR TRANSPORT INTERNATIONAL');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('AWI', 'WISCONSIN', 'AIR WISCONSIN AIRLINES CORPORATION ');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('BAW', 'SPEEDBIRD', 'BRITISH AIRWAYS');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('DAL', 'DELTA', 'DELTA AIR LINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('DLH', 'LUFTHANSA', 'DEUTSCHE LUFTHANSA');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('EDV', 'ENDEAVOR', 'ENDEAVOR AIR');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ENY', 'ENVOY', 'ENVOY AIR INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('FDX', 'FEDEX', 'FEDERAL EXPRESS CORPORATION');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('FFT', 'FRONTIER FLIGHT', 'FRONTIER AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('GEC', 'LUFTHANSA CARGO', 'LUFTHANSA CARGO AG');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('HAL', 'HAWAIIAN', 'HAWAIIAN AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('JBU', 'JETBLUE', 'JETBLUE AIRWAYS CORPORATION');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('JIA', 'BLUE STREAK', 'PSA AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('KLM', 'KLM', 'KLM ROYAL DUTCH AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('NKS', 'SPIRIT WINGS', 'SPIRIT AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('PDT', 'PIEDMONT', 'PIEDMONT AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('POE', 'PORTER AIR', 'PORTER AIRLINES INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('QTR', 'QATARI', 'QATAR AIRWAY S COMPANY');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('QXE', 'HORIZON AIR', 'HORIZON AIR INDUSTRIES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('ROU', 'ROUGE', 'AIR CANADA ROUGE');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('RPA', 'BRICKYARD', 'REPUBLIC AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('SCX', 'SUN COUNTRY', 'MN AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('SWA', 'SOUTHWEST', 'SOUTHWEST AIRLINES CO.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('SWG', 'SUNWING', 'SUNWING AIRLINES INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('TSC', 'AIR TRANSAT', 'AIR TRANSAT A.T. INC.');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('UAE', 'EMIRATES', 'EMIRATES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('UAL', 'UNITED', 'UNITED AIRLINES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('UCA', 'COMMUTAIR', 'COMMUTAIR');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('UPS', 'UPS', 'UNITED PARCEL SERVICE COMPANY');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('VIV', 'AEROENLACES', 'AEROENLACES NACIONALES');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('VOI', 'VOLARIS', 'CONCESIONARIA VUELA COMPANIA DE AVIACION');
INSERT INTO public.flight_call_signs (three_letter_id, call_sign, company) VALUES ('WJA', 'WESTJET', 'WESTJET AIRLINES LTD');


--
-- PostgreSQL database dump complete
--

