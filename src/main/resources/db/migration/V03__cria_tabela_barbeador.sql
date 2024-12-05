CREATE TABLE public.barbeador
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    data_nascimento date,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);