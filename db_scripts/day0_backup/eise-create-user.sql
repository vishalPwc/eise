-- Connect to relavant PDB before creating the tablespace. Use: ALTER SESSION SET CONTAINER = <PDB_NAME> to do so.
-- If the connection is with CDB, prefix the username with C## before executing these statements.

create user EISE_DEV identified by "Eise#Dev_123" default tablespace TBS_EISE_DEV quota unlimited on TBS_EISE_DEV;
commit;