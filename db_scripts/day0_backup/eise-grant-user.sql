-- Connect to relavant PDB before creating the tablespace. Use: ALTER SESSION SET CONTAINER = <PDB_NAME> to do so.
-- If the connection is with CDB, prefix the username with C## before executing these statements.

grant create table to EISE_DEV;
grant create session, connect, create view, create procedure, create sequence to EISE_DEV;
commit;