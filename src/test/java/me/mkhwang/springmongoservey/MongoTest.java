package me.mkhwang.springmongoservey;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;

@Slf4j
public class MongoTest {

    private MongoClient client;

    @BeforeEach
    public void getMongoCredential() {
        final MongoCredential credential = MongoCredential.createCredential("userName", "database", "password".toCharArray());
        final MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings(s -> {
                            s.hosts(singletonList(new ServerAddress("127.0.0.1", 27017)));
                        })
                        .build());
        client = mongoClient;
    }

    @Test
    void test() {
        final MongoDatabase database = client.getDatabase("database");
        for (final String collectionName : database.listCollectionNames()) {
            final MongoCollection<Document> collection = database.getCollection(collectionName);
            final long count = collection.estimatedDocumentCount();
            log.debug("{}.{}.count: {}", database, collectionName, count);
        }
        final MongoCollection<Document> collection = database.getCollection("collection")
                .withCodecRegistry(MongoClientSettings.getDefaultCodecRegistry());
        final FindIterable<Document> limit = collection.find().limit(10);
        JSONArray jsonArray = new JSONArray();
        limit.forEach(d -> jsonArray.put(d.toJson()));
        log.debug("jsonArray: {}", jsonArray);
    }

}
