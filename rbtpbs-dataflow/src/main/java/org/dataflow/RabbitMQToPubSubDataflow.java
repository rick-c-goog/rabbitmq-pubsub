
package org.dataflow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.rabbitmq.RabbitMqIO;
import org.apache.beam.sdk.io.rabbitmq.RabbitMqMessage;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.KV;

public class RabbitMQToPubSubDataflow  {

    // Custom options for connecting to RabbitMQ and Pub/Sub
    public interface RabbitMQToPubSubOptions extends PipelineOptions {
        @Description("Username command line argument.")
        @Default.String("user_")
        String getUserName();
        void setUserName(String userName);
       
        @Description("Password command line argument.")
        @Default.String("password_")
        String getPassword();
        void setPassword(String password);

        @Description("Host command line argument.")
        @Default.String("host_")
        String getHost();
        void setHost(String host);

        @Description("Pubsub Topic command line argument.")
        @Default.String("topic_")
        String getTopic();
        void setTopic(String topic);

        @Description("RabbitMQ queue command line argument.")
        @Default.String("queue")
        String getQueue();
        void setQueue(String queue);
        // Add getters and setters for necessary RabbitMQ and Pub/Sub options
    }
    //PipelineOptionsFactory.register(RabbitMQToPubSubOptions.class);
    static class FormatForPubSub extends DoFn<String, String> {
        @ProcessElement
        public void processElement(ProcessContext context) {
            //  Reformat the message from RabbitMQ if needed before publishing to PubSub
            String input = context.element();
            String formattedMessage = input; // Potentially adjust formatting
            context.output(formattedMessage);
        }
    }

    public static void main(String[] args) {
        RabbitMQToPubSubOptions options = PipelineOptionsFactory
                                          .fromArgs(args)
                                          .withValidation()
                                          .as(RabbitMQToPubSubOptions.class);

        Pipeline pipeline = Pipeline.create(options);
        String user=options.getUserName();
        String password=options.getPassword();
        String host=options.getHost();
        String queue=options.getQueue();
        String pubsubTopic=options.getQueue();
        /*
        RabbitMqMessage message = new RabbitMqMessage("my_message");
        
        pipeline.apply(
               RabbitMqIO.write()
                 .withUri("amqp://"+user+ ":"+ password +"@" +host+":5672") 
                 .withQueue("QUEUE")
            );
         */
        String uri="amqp://"+user+ ":"+ password +"@" +host+":5672";
        System.out.println(uri);
        PCollection<RabbitMqMessage> messages = pipeline.apply(
                RabbitMqIO.read()
                        .withUri(uri) // Your RabbitMQ URI
                        .withQueue(queue) // Your RabbitMQ queue name
        );


        messages
        .apply(
            MapElements.via(
                new SimpleFunction<RabbitMqMessage, String>() {
                  public String apply(RabbitMqMessage input) {
                    return new String(input.getBody());
                  }
                }))
        .apply("WriteToPubSubTopic", PubsubIO.writeStrings().to(pubsubTopic));  //default "projects/mqtopubsubtest/topics/rabbit2ps"
        pipeline.run().waitUntilFinish();
    }
}