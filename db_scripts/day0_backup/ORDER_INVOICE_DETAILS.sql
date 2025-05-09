--------------------------------------------------------
--  File created - Thursday-May-07-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table ORDER_INVOICE_DETAILS
--------------------------------------------------------

 CREATE TABLE EISE_DEV.ORDER_INVOICE_DETAILS
   (	"ID" NUMBER GENERATED BY DEFAULT AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"ORDER_ID" VARCHAR2(50), 
	"INVOICE_ID" VARCHAR2(50), 
	"AMOUNT" FLOAT(126), 
	"ORDER_DATE" TIMESTAMP (6), 
	"INVOICE_TYPE" VARCHAR2(50)
   );

  ALTER TABLE EISE_DEV.ORDER_INVOICE_DETAILS ADD CONSTRAINT "ORDER_INVOICE_DETAILS_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
