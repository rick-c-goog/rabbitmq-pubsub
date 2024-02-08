# Configure rabbitmq to PubSub


## RabbitMQ cluster setup in GKE 
Update rabbitmq-test.yaml , then run rabbit_cluster.sh, 

Get the login credentail( username, password, and external IP )

## GCP application integration setup:

### create an application integration

### Application Integration components:
Following this guide, 
https://cloud.google.com/application-integration/docs/configure-rabbitmq-trigger
1. RabbitMQ connector, 
2. RabbitMQ trigger
3. Pubsub connector 
4. DataMapping task, 
Need to create a variable first, with name match to the trigger output variable: 

5. Connector Task

### test and validations:

RabbitMQ console login:
Use the credentials and external IP retrived earlier, open the RabbitMQ console:
http://external_ip:15672

Create a durable type of queue and bind it to jms.durable.queues, 
publish a message, validate logs from application integration, also check the messages routed to pubsub topic

