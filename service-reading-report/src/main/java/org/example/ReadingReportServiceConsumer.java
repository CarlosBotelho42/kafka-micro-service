package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ReadingReportServiceConsumer {

   private static final Path SOURCE = new File("/src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        var reportConsumer = new ReadingReportServiceConsumer();
        try (var service = new KafkaService<>(ReadingReportServiceConsumer.class.getSimpleName(),
                "USER_GENERATE_READING_REPORT",
                reportConsumer::parse,
                User.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Message<User>> record) throws IOException {

        System.out.println("---------------------------------------------");
        System.out.println("Processando relatório para " + record.value());

        var message = record.value();
        var user = message.getPayload();
        var target = new  File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());

        System.out.println("Arquivo criado: " + target.getAbsolutePath());

    }
}

