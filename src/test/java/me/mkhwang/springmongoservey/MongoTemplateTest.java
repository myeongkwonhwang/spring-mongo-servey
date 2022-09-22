package me.mkhwang.springmongoservey;

import com.mongodb.client.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootTest
@Slf4j
class MongoTemplateTest {

    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient mongoClient;

    @PostConstruct
    void onPostConstruct() {
        mongoTemplate = new MongoTemplate(mongoClient, "prod_match");
    }

    @Test
    void select() {
        final Query query = new Query();
        query.limit(10);
        List<Document> prod_tag = mongoTemplate.find(query, Document.class, "prod_tag");
        JSONArray jsonArray = new JSONArray();
        prod_tag.forEach(d -> {
            jsonArray.put(d.toJson());
        });
        log.info("jsonArray: {}", jsonArray);
    }
}
