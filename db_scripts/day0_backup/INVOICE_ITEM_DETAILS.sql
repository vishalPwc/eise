--------------------------------------------------------
--  File created - Thursday-May-07-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table INVOICE_ITEM_DETAILS
--------------------------------------------------------

   CREATE TABLE EISE_DEV.INVOICE_ITEM_DETAILS
   (	"ID" NUMBER GENERATED BY DEFAULT AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"INVOICE_ID" NUMBER, 
	"INVOICE_LINE_ID" VARCHAR2(50), 
	"ITEM_ID" VARCHAR2(50), 
	"ITEM_DESC" VARCHAR2(200), 
	"PLAN" VARCHAR2(50), 
	"QUANTITY" NUMBER, 
	"UNIT_PRICE" FLOAT(126), 
	"TOTAL" FLOAT(126), 
	"IIS_ID" NUMBER, 
	"INVOICE_DATE" TIMESTAMP (6)
   );
--------------------------------------------------------
--  DDL for Index INVOICE_ITEM_DETAILS_PK
--------------------------------------------------------


  ALTER TABLE EISE_DEV.INVOICE_ITEM_DETAILS ADD CONSTRAINT "INVOICE_ITEM_DETAILS_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INVOICE_ITEM_DETAILS
--------------------------------------------------------

  ALTER TABLE EISE_DEV.INVOICE_ITEM_DETAILS ADD CONSTRAINT "INVOICE_ITEM_DETAILS_FK1" FOREIGN KEY ("INVOICE_ID")
	  REFERENCES "ORDER_INVOICE_DETAILS" ("ID") ENABLE;
