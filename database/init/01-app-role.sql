CREATE ROLE agservpro
    LOGIN
    PASSWORD 'agservpro'
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE;

GRANT ALL PRIVILEGES ON DATABASE agservpro TO agservpro;

\connect agservpro

GRANT USAGE, CREATE ON SCHEMA public TO agservpro;
