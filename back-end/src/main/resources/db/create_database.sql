-- 1) Banco
CREATE DATABASE tinnova
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TEMPLATE = template1;

-- 2) Usuário
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'tinnova') THEN
      CREATE ROLE tinnova LOGIN PASSWORD 'tinnova';
   END IF;
END$$;

GRANT CONNECT ON DATABASE tinnova TO tinnova;

-- 3) Esquema público
GRANT USAGE ON SCHEMA public TO tinnova;

-- 4) Tabela veiculos
CREATE TABLE IF NOT EXISTS public.veiculos (
  id         BIGSERIAL PRIMARY KEY,
  veiculo    VARCHAR(255) NOT NULL,
  marca      VARCHAR(255) NOT NULL,
  ano        INTEGER NOT NULL,
  cor        VARCHAR(255) NOT NULL,
  descricao  TEXT,
  vendido    BOOLEAN NOT NULL DEFAULT FALSE,
  created    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 5) Permissões básicas ao usuário do app
GRANT SELECT, INSERT, UPDATE, DELETE ON public.veiculos TO tinnova;