# RabbitMQ to Pubsub dataflow

## download this code
## A few commands to tests
```
mvn clean install
```

setup a few variables before test the apache bean pipleline locally
```
export USER_ID=XXXX # Username for RabbitMQ credential
export PASSWORD=XXX  # Password for RabbitMQ credential
export HOST=XXXX #RabbitMQ host name/IP
export TOPIC=XXX  # Full path GCP pubsub topic (projects/rick-devops-01/topics/rabbit2ps)
export QUEUE=XXX  # RabbitMQ queue
```
Run the following command to start local pipeline
```
mvn  exec:java  -Dexec.mainClass=org.dataflow.RabbitMQToPubSubDataflow
```
Push a message to RabbitMQ through console or program. 
Check the message published to GCP pubsub, pull the messages under pubsub topic



mvn -Pdataflow-runner compile exec:java \
    -Dexec.mainClass=org.dataflow.RabbitMQToPubSubDataflow \
    -Dexec.args="--project=PROJECT_ID \
    --gcpTempLocation=gs://BUCKET_NAME/temp/ \
    --runner=DataflowRunner \
    --region=REGION" \
    --userName=XXX" \
    --password=XXX" \
    --host=XXX" \
    --topic=XXX" \
    --queue=xxx" 