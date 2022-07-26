package net.yorksolutions.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        //create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //create a producer record


        for ( int i=0; i<10; i++) {

            String topic = "demo_java";
            String value = "hello world" +i;
            String key = "id_" + i;

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    //executes every time a record is successfully sent or an exception is thrown
                    if (e == null) {
                        //the record was successfully sent
                        log.info("\nReceived new metadata. \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Key: " + producerRecord.key() + "\n" +
                                "Partition :" + metadata.partition() + "\n" +
                                "Offset :" + metadata.offset() + "\n" +
                                "Timestamp :" + metadata.timestamp()
                        );
                    } else {
                        log.error("Error while producing", e);
                    }
                }
            });
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
            //send data - asynchronous


        //flush and close the Producer. flush data is synchronous
        producer.flush();

        //flush and close - calling .close() also calls flush, but we call flush above just for demonstration
        producer.close();

    }
}
