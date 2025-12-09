-- 1) Créer la base
CREATE DATABASE product_management_db;

-- 2) Créer l'utilisateur NON admin
CREATE USER product_manager_user WITH PASSWORD '123456';

-- 3) Donner accès à la base
GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;

-- Se connecter à la nouvelle base
\c product_management_db;

-- 4) Donner les droits sur le schéma public
GRANT USAGE ON SCHEMA public TO product_manager_user;
GRANT CREATE ON SCHEMA public TO product_manager_user;

-- 5) Donner droits CRUD sur toutes les tables (même futures)
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO product_manager_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_user;
