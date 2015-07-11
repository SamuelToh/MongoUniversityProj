package course.homework;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.List;
import java.util.ArrayList;

/*
    Loops through the document of each student in the grades collection of students db.
*/
public class Homework2_3 {

    static MongoClient dbClient = new MongoClient(new ServerAddress("localhost", 27017));

    public static void main(String[] args) {
        try {
            MongoClient dbClient = new MongoClient();
            MongoDatabase db = dbClient.getDatabase("test");
            MongoCollection<Document> animals = db.getCollection("animals");

            Document animal = new Document("animal", "monkey");

            animals.insertOne(animal);
            animal.remove("animal");
            animal.append("animal", "cat");
            animals.insertOne(animal);
            animal.remove("animal");
            animal.append("animal", "lion");
            animals.insertOne(animal);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
