This project is only used to demonstrate the following purpose:

1, how to send HTTPS request with client certificate with OkHttp or build-in HttpClient.
2, SSLSocketFactory must be recreated every time when a new request is executed.


To be more specific, if the HTTPS request is sharing the SSLSocketFactory instance, only the first 
request will succeed and the rest of the request will fail due to client certificate not attached.

However, everything works okay in java project.

So here comes the question: is it Android system bug?


