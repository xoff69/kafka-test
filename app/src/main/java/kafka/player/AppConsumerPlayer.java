package kafka.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class AppConsumerPlayer {
  public static void main(String[] args) {

    Properties properties = new Properties();
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "xoff-consumer");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // create consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
    // subscribe consumer to our topic(s)
    consumer.subscribe(Arrays.asList("topic-player"));

    CommonPlayerDao commonPlayerDao=new CommonPlayerDao();

    // un consommateur sur le topic player

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<String, String> record : records) {
        System.out.println("Key: " + record.key() + ", Value: " + record.value());
        System.out.println("Partition: " + record.partition() + ", Offset:" + record.offset());
        // la record value est un player au format json
        // on poplate un insert avec
        ObjectMapper objectMapper = new ObjectMapper();
        try {
          CommonPlayer player = objectMapper.readValue(record.value(), CommonPlayer.class);
          System.out.println(player); // Affiche l'objet CommonPlayerEntity désérialisé
          commonPlayerDao.insertCommonPlayer(player);
        } catch (JsonMappingException e) {
          e.printStackTrace(); // Gérer les erreurs de mappage
        } catch (JsonProcessingException | SQLException e) {
          e.printStackTrace(); // Gérer les erreurs de traitement JSON
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }

      }
    }
  }
}
