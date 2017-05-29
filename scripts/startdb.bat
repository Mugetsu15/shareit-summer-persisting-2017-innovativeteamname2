:: !/bin/bash
:: Startskript to expose database such that it can by used by schemaspy
:: start hsqldb
java -cp ../lib/hsqldb-2.3.0.jar org.hsqldb.server.Server --database.0 file:../DB/ShareIt-DB --dbname.0 ShareIt-DB