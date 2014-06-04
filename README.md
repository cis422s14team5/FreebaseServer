FreebaseServer
==============

Command line server for searching freebase film and tv show data. It is indented to be used with the WatchlistPro desktop app.

Installation and Use
--------------------

The server requires a Google API Key to connect to the Freebase database server. The API key only works with a defined list of IP addresses. To add a new API key, you must edit the source and change the API_KEY global variable in Freebase.java.

API Keys and more information about settin up a Google Developer Console can be found here:
https://console.developers.google.com/project/51439897032/apiui/credential

To run the server open your system command line program and type:

java -jar FreebaseServer.jar
