CREATE TABLE public.corte
(
    codigo serial NOT NULL,
    barbeador text,
    corte text,
    data_corte date,
    hora time,
    situacao boolean,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);