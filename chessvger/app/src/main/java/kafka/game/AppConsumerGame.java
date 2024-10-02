package kafka.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import kafka.player.CommonPlayer;
import kafka.player.CommonPlayerDao;
import kafka.util.CommonKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class AppConsumerGame {
  public static void main(String[] args) {

  KafkaConsumer consumer= CommonKafka.getConsumer("topic-game","xoff-game");

    CommonGameDao commonGameDao=new CommonGameDao();
    ObjectMapper objectMapper = new ObjectMapper();

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<String, String> record : records) {


        try {
          CommonGame game = objectMapper.readValue(record.value(), CommonGame.class);
          commonGameDao.insertCommonGame(game);

        } catch (JsonProcessingException | ClassNotFoundException | SQLException e) {
          throw new RuntimeException(e);
        }

      }
    }
  }
}
