package com.personal.quasar.dao.listener;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import com.mongodb.client.model.changestream.OperationType;
import com.personal.quasar.kafka.UserInfoProducer;
import com.personal.quasar.model.entity.User;
import com.personal.quasar.model.mapper.UserDocumentMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * MongoDB change stream listener for user collection.
 * Streams inserts/updates/replaces/deletes to support CDC use cases.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserChangeListener {

    private final UserDocumentMapper userDocumentMapper;

    private static final List<String> OPERATIONS = List.of("insert", "update", "replace", "delete");

    private final MongoTemplate mongoTemplate;

    private final UserInfoProducer userInfoProducer;

    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        Executors.newSingleThreadExecutor().submit(() -> {
            var database = mongoTemplate.getDb();
            log.info(database.getName());
            var collection = database.getCollection("user");
            log.info(collection.getDocumentClass().toString());
            List<Bson> pipeline = List.of(Aggregates.match(Filters.in("operationType", OPERATIONS)));


            try (MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor =
                         collection.watch(pipeline)
                                 .fullDocument(FullDocument.UPDATE_LOOKUP) // ensures docs on updates
                                 .cursor()) {
                while (cursor.hasNext()) {
                    var event = cursor.next();
                    if(event.getOperationType() == OperationType.INSERT) {
                        Document document = event.getFullDocument();
                        var user = userDocumentMapper.toDto(document);
                        userInfoProducer.produce(user);
                    }
                    log.info("User change: op={}, doc={}", event.getOperationType(), event.getFullDocument());
                }
            } catch (Exception e) {
                log.error("User change stream stopped", e);
            }
        });
    }

}
