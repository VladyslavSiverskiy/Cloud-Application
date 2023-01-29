# Cloud-Application

The project consist of two parts (Server and client)

Clone this repo to config server. There is no need to copy client_resources folder

Download client app: https://github.com/vsiver/Cloud-Application/blob/main/client_resources/Cloud.exe
To open client, you should run server in your local network.

To run server, you have to create a connection to postgres. So, run postgeSql on your computer, create new db 
and execute next script to create table: https://github.com/vsiver/Cloud-Application/blob/main/src/main/java/com/vsivercloud/Sql_script

Then, create file DBConfig.java in main/src/main/java/com/vsivercloud/ folder. Create three variables (DB_URL, DB_USER, DB_PASSWORD) and fill them (add data of db). 

