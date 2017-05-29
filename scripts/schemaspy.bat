:: !/bin/bash
:: Start script for schemaspy. requires exposed db.
java -jar ../lib/schemaSpy_5.0.0.jar -t hsqldb -db ShareIt-DB -host localhost -u DManstrator -noschema -dp ../lib/hsqldb-2.3.0.jar -o ../DB/ShareIt