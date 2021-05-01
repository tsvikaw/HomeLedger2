This is a micro-serviced application for home expense tracking
==============================================================

Storage MS
==========
port: 9999

Run on local db for debug:
private static final String DEF_DB_URL = "jdbc:mysql://localhost:3306/hl?useSSL=false";
home.ledger.parent/home.ledger.storage/src/main/java/com/tsvika/home/ledger/storage/configuration/DataBaseHelper.java

for prod should use env vars: DB_URL, DB_SCHEMA, DB_USER, DB_PWD
docker run  -d -p 9999:9999 -e DB_URL=jdbc:mysql://192.168.1.28:3306/hl -e DB_SCHEMA=hl -e DB_USER=admin -e DB_PWD=password hl_storage


Excel MS
========
port: 8091

Used for importing banking/credit card transactions into the storage, supports a wizard gui for that purpose.
 docker run -d -p 8091:8091 -e STORAGE_SERVICE_URL=http://192.168.1.27:9999/ -v /Users/twiegner/Downloads:/uploads  hl_excel

Communicates with storage
Allow CORS all
No auth


REPORTS MS
==========
port: 8092

Used for querying lines by parameters for displaying data tables and graphs.
docker run -d -p 8092:8092 -e STORAGE_SERVICE_URL=http://192.168.1.28:9999/  hl_reports

Communicates with storage
JWT auth using another project (below)


UI
===========
React app over yarn

to run on dev do: npm install, then, yarn dev
access on localhost:3000

docker run -d -p 3000:3000 hl_ui

user
password


Auth service (deprecated - now implemented inside npm server)
============
port: 8080
/Users/twiegner/git/experimental/springboot-auth-updated2/

Creates JWT for other apps to use
runs on gradle

Shared
======

All object shared are in the dto package