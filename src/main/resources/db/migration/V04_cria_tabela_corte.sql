CREATE TABLE public.corte
(
    codigo serial NOT NULL,
    cliente text,
    barbeador text,
    data_corte date,
    hora time,
    situacao boolean,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);