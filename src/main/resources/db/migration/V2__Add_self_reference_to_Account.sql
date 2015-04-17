ALTER TABLE "PUBLIC"."ACCOUNTS"
ADD COLUMN "PARENT_ID" BIGINT NULL
;
ALTER TABLE "PUBLIC"."ACCOUNTS"
ADD CONSTRAINT FK_ACCOUNTS
FOREIGN KEY (PARENT_ID)
REFERENCES "PUBLIC"."ACCOUNTS"(ACCOUNT_ID)
;
CREATE INDEX SYS_IDX_10119 ON "PUBLIC"."ACCOUNTS"(PARENT_ID)
;