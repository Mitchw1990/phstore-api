-- docker run --name ph-db -d -p 49160:22 -p 49161:1521 christophesurmont/oracle-xe-11g
-- docker exec -ti ph-db /bin/bash
-- sqlplus system/oracle

conn sys/oracle as sysdba;
create user PH identified by PH;
grant unlimited tablespace to PH;
grant all privileges to PH;
CREATE TABLESPACE D01 DATAFILE '/u01/app/oracle/oradata/XE/D01.dbf' SIZE 5120M;
CREATE TABLESPACE I01 DATAFILE '/u01/app/oracle/oradata/XE/I01.dbf' SIZE 5120M;
CREATE TABLESPACE L01 DATAFILE '/u01/app/oracle/oradata/XE/L01.dbf' SIZE 5120M;
alter system set processes=300 scope=spfile;
alter system set sessions=300 scope=spfile;

shutdown immediate;
startup;
disc;


conn PH/PH;
create role PH_DELETE;
create role  PH_INSERT;
create role  PH_UPDATE;
create role  PH_SELECT;
create user PHUSR identified by PHUSR;
grant create session to PHUSR;
grant PH_DELETE to PHUSR;
grant PH_INSERT to PHUSR;
grant PH_UPDATE to PHUSR;
grant PH_SELECT to PHUSR;

disc;
