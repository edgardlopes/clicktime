--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2015-10-30 11:15:35

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 188 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2043 (class 0 OID 0)
-- Dependencies: 188
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 100667)
-- Name: categoria_servico; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE categoria_servico (
    id bigint NOT NULL,
    nome character varying(100) NOT NULL
);


ALTER TABLE public.categoria_servico OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 100665)
-- Name: categoria_servico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE categoria_servico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categoria_servico_id_seq OWNER TO postgres;

--
-- TOC entry 2044 (class 0 OID 0)
-- Dependencies: 170
-- Name: categoria_servico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE categoria_servico_id_seq OWNED BY categoria_servico.id;


--
-- TOC entry 180 (class 1259 OID 100714)
-- Name: dia_atendimento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE dia_atendimento (
    data_at date NOT NULL,
    id bigint NOT NULL,
    profissional_fk bigint NOT NULL
);


ALTER TABLE public.dia_atendimento OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 100712)
-- Name: dia_atendimento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE dia_atendimento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dia_atendimento_id_seq OWNER TO postgres;

--
-- TOC entry 2045 (class 0 OID 0)
-- Dependencies: 179
-- Name: dia_atendimento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE dia_atendimento_id_seq OWNED BY dia_atendimento.id;


--
-- TOC entry 178 (class 1259 OID 100706)
-- Name: execucao_servico; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE execucao_servico (
    id bigint NOT NULL,
    profissional_fk bigint NOT NULL,
    servico_fk bigint NOT NULL,
    descricao character varying(150) NOT NULL,
    duracao time without time zone NOT NULL,
    valor double precision NOT NULL,
    ativo boolean
);


ALTER TABLE public.execucao_servico OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 100704)
-- Name: execucao_servico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE execucao_servico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.execucao_servico_id_seq OWNER TO postgres;

--
-- TOC entry 2046 (class 0 OID 0)
-- Dependencies: 177
-- Name: execucao_servico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE execucao_servico_id_seq OWNED BY execucao_servico.id;


--
-- TOC entry 182 (class 1259 OID 100722)
-- Name: horario_atendimento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE horario_atendimento (
    id bigint NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fim time without time zone NOT NULL,
    status character varying(1) DEFAULT 'B'::character varying,
    dia_atendimento_fk bigint NOT NULL
);


ALTER TABLE public.horario_atendimento OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 100720)
-- Name: horario_atendimento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE horario_atendimento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.horario_atendimento_id_seq OWNER TO postgres;

--
-- TOC entry 2047 (class 0 OID 0)
-- Dependencies: 181
-- Name: horario_atendimento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE horario_atendimento_id_seq OWNED BY horario_atendimento.id;


--
-- TOC entry 176 (class 1259 OID 100696)
-- Name: profissional; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE profissional (
    usuario_fk bigint NOT NULL,
    descricao character varying(250) NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fim time without time zone NOT NULL,
    unidade_tempo time without time zone DEFAULT '00:00:00'::time without time zone NOT NULL
);


ALTER TABLE public.profissional OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 100677)
-- Name: servico; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE servico (
    categoria_servico_fk bigint NOT NULL,
    id bigint NOT NULL,
    nome character varying(100) NOT NULL
);


ALTER TABLE public.servico OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 100675)
-- Name: servico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE servico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.servico_id_seq OWNER TO postgres;

--
-- TOC entry 2048 (class 0 OID 0)
-- Dependencies: 172
-- Name: servico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE servico_id_seq OWNED BY servico.id;


--
-- TOC entry 184 (class 1259 OID 100731)
-- Name: solicitacao; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitacao (
    id bigint NOT NULL,
    usuario_fk bigint NOT NULL,
    execucao_servico_fk bigint NOT NULL,
    status character varying(1) NOT NULL,
    descricao text
);


ALTER TABLE public.solicitacao OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 247494)
-- Name: solicitacao_avaliacao; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitacao_avaliacao (
    solicitacao_fk bigint NOT NULL,
    pontos double precision NOT NULL
);


ALTER TABLE public.solicitacao_avaliacao OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 165578)
-- Name: solicitacao_horario_atendimento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitacao_horario_atendimento (
    solicitacao_fk bigint NOT NULL,
    horario_atendimento_fk bigint NOT NULL
);


ALTER TABLE public.solicitacao_horario_atendimento OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 100729)
-- Name: solicitacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE solicitacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.solicitacao_id_seq OWNER TO postgres;

--
-- TOC entry 2049 (class 0 OID 0)
-- Dependencies: 183
-- Name: solicitacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE solicitacao_id_seq OWNED BY solicitacao.id;


--
-- TOC entry 175 (class 1259 OID 100685)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    nome character varying(100) NOT NULL,
    email character varying(255) NOT NULL,
    telefone character varying(20),
    senha character varying(100) NOT NULL,
    sobrenome character varying(100) NOT NULL,
    nome_usuario character varying(100) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 214726)
-- Name: usuario_avatar; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario_avatar (
    imagem bytea NOT NULL,
    usuario_fk bigint NOT NULL
);


ALTER TABLE public.usuario_avatar OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 100683)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 2050 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- TOC entry 1879 (class 2604 OID 100670)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_servico ALTER COLUMN id SET DEFAULT nextval('categoria_servico_id_seq'::regclass);


--
-- TOC entry 1884 (class 2604 OID 100717)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dia_atendimento ALTER COLUMN id SET DEFAULT nextval('dia_atendimento_id_seq'::regclass);


--
-- TOC entry 1883 (class 2604 OID 100709)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY execucao_servico ALTER COLUMN id SET DEFAULT nextval('execucao_servico_id_seq'::regclass);


--
-- TOC entry 1885 (class 2604 OID 100725)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY horario_atendimento ALTER COLUMN id SET DEFAULT nextval('horario_atendimento_id_seq'::regclass);


--
-- TOC entry 1880 (class 2604 OID 100680)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY servico ALTER COLUMN id SET DEFAULT nextval('servico_id_seq'::regclass);


--
-- TOC entry 1887 (class 2604 OID 100734)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitacao ALTER COLUMN id SET DEFAULT nextval('solicitacao_id_seq'::regclass);


--
-- TOC entry 1881 (class 2604 OID 100688)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- TOC entry 1889 (class 2606 OID 100674)
-- Name: categoria_servico_nome_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY categoria_servico
    ADD CONSTRAINT categoria_servico_nome_key UNIQUE (nome);


--
-- TOC entry 1891 (class 2606 OID 100672)
-- Name: categoria_servico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY categoria_servico
    ADD CONSTRAINT categoria_servico_pkey PRIMARY KEY (id);


--
-- TOC entry 1907 (class 2606 OID 100719)
-- Name: dia_atendimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dia_atendimento
    ADD CONSTRAINT dia_atendimento_pkey PRIMARY KEY (id);


--
-- TOC entry 1905 (class 2606 OID 100711)
-- Name: execucao_servico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY execucao_servico
    ADD CONSTRAINT execucao_servico_pkey PRIMARY KEY (id);


--
-- TOC entry 1909 (class 2606 OID 100728)
-- Name: horario_atendimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY horario_atendimento
    ADD CONSTRAINT horario_atendimento_pkey PRIMARY KEY (id);


--
-- TOC entry 1903 (class 2606 OID 100703)
-- Name: profissional_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY profissional
    ADD CONSTRAINT profissional_pkey PRIMARY KEY (usuario_fk);


--
-- TOC entry 1893 (class 2606 OID 100682)
-- Name: servico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY servico
    ADD CONSTRAINT servico_pkey PRIMARY KEY (id);


--
-- TOC entry 1917 (class 2606 OID 247498)
-- Name: solicitacao_avaliacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitacao_avaliacao
    ADD CONSTRAINT solicitacao_avaliacao_pkey PRIMARY KEY (solicitacao_fk);


--
-- TOC entry 1913 (class 2606 OID 165582)
-- Name: solicitacao_horario_atendimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitacao_horario_atendimento
    ADD CONSTRAINT solicitacao_horario_atendimento_pkey PRIMARY KEY (solicitacao_fk, horario_atendimento_fk);


--
-- TOC entry 1911 (class 2606 OID 100736)
-- Name: solicitacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitacao
    ADD CONSTRAINT solicitacao_pkey PRIMARY KEY (id);


--
-- TOC entry 1915 (class 2606 OID 214733)
-- Name: usuario_avatar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario_avatar
    ADD CONSTRAINT usuario_avatar_pkey PRIMARY KEY (usuario_fk);


--
-- TOC entry 1895 (class 2606 OID 100695)
-- Name: usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 1897 (class 2606 OID 190159)
-- Name: usuario_nome_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_nome_usuario_key UNIQUE (nome_usuario);


--
-- TOC entry 1899 (class 2606 OID 100693)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 1901 (class 2606 OID 190161)
-- Name: usuario_telefone_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_telefone_key UNIQUE (telefone);


--
-- TOC entry 1928 (class 2606 OID 222918)
-- Name: avatar_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_avatar
    ADD CONSTRAINT avatar_usuario_fk FOREIGN KEY (usuario_fk) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1922 (class 2606 OID 100757)
-- Name: dia_atendimento_profissional_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dia_atendimento
    ADD CONSTRAINT dia_atendimento_profissional_fk FOREIGN KEY (profissional_fk) REFERENCES profissional(usuario_fk) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1920 (class 2606 OID 100747)
-- Name: execuca_servico_profissional_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY execucao_servico
    ADD CONSTRAINT execuca_servico_profissional_fk FOREIGN KEY (profissional_fk) REFERENCES profissional(usuario_fk) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1921 (class 2606 OID 100752)
-- Name: execuca_servico_servico_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY execucao_servico
    ADD CONSTRAINT execuca_servico_servico_fk FOREIGN KEY (servico_fk) REFERENCES servico(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 1923 (class 2606 OID 173770)
-- Name: horario_atendimento_dia_atendimento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY horario_atendimento
    ADD CONSTRAINT horario_atendimento_dia_atendimento_fk FOREIGN KEY (dia_atendimento_fk) REFERENCES dia_atendimento(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1919 (class 2606 OID 100742)
-- Name: profissional_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profissional
    ADD CONSTRAINT profissional_usuario_fk FOREIGN KEY (usuario_fk) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1918 (class 2606 OID 100737)
-- Name: servico_categoria_servico_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY servico
    ADD CONSTRAINT servico_categoria_servico_fk FOREIGN KEY (categoria_servico_fk) REFERENCES categoria_servico(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 1925 (class 2606 OID 100777)
-- Name: solicitacao_execucao_servico_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitacao
    ADD CONSTRAINT solicitacao_execucao_servico_fk FOREIGN KEY (execucao_servico_fk) REFERENCES execucao_servico(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 1927 (class 2606 OID 165588)
-- Name: solicitacao_horario_atendimento_horario_atendimento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitacao_horario_atendimento
    ADD CONSTRAINT solicitacao_horario_atendimento_horario_atendimento_fk FOREIGN KEY (horario_atendimento_fk) REFERENCES horario_atendimento(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1926 (class 2606 OID 165583)
-- Name: solicitacao_horario_atendimento_solicitacao_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitacao_horario_atendimento
    ADD CONSTRAINT solicitacao_horario_atendimento_solicitacao_fk FOREIGN KEY (solicitacao_fk) REFERENCES solicitacao(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1924 (class 2606 OID 100772)
-- Name: solicitacao_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitacao
    ADD CONSTRAINT solicitacao_usuario_fk FOREIGN KEY (usuario_fk) REFERENCES usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2042 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-10-30 11:15:38

--
-- PostgreSQL database dump complete
--

