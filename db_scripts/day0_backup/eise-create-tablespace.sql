-- Connect to relavant PDB before creating the tablespace. Use: ALTER SESSION SET CONTAINER = <PDB_NAME> to do so.
-- If the connection is with CDB, prefix the username with C## before executing these statements.

CREATE TABLESPACE TBS_EISE_DEV DATAFILE 'TBS_EISE_DEV_PDB.dat' SIZE 100M REUSE AUTOEXTEND ON NEXT 100M;