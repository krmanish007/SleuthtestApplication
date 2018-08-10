# SleuthtestApplication
This application is testing sleuth integration with the (non spring integration based) websocket client, which is not implemented in the out of box version of sleuth.

Integration test has created a mock version of Spring WebSocket, and Embedded Kafka.

This project is also trying to find out where traceId get repeated for each new message received from websocket
