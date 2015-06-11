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
            final MongoDatabase studentDatabase = dbClient.getDatabase("students");
            final MongoCollection<Document> collection = studentDatabase.getCollection("grades");

            Bson queryFilter = Filters.eq("type", "homework");
            Bson queryProjection = Projections.fields(Projections.include("student_id", "score", "type"), Projections.exclude("_id"));
            Bson querySort = new Document("student_id",1).append("score", 1); //ascending order

            List<Document> docs = collection.find(queryFilter).projection(queryProjection).sort(querySort).into(new ArrayList<Document>());

            double prevDeletedId = -1.0, currStudentId = -1, currStudentScore = -1;

            for (Document doc : docs) {
                currStudentId = (Double) doc.get("student_id");
                currStudentScore = (Double) doc.get("score");

                if (currStudentId != prevDeletedId) {
                    Bson delFilter = Filters.and(Filters.eq("student_id", currStudentId), Filters.eq("type", "homework"), Filters.eq("score", currStudentScore));
                    collection.deleteOne(delFilter);
                    System.out.println(" Removed row -> Student Id = " + currStudentId + ", score = " + currStudentScore + ", type= " + doc.get("type"));

                    prevDeletedId = currStudentId;
                }
                else {
                    System.out.println("Skip delete. Row -> Student Id = " + currStudentId + ", score = " + currStudentScore + ", type= " + doc.get("type"));
                }
            }
        } catch(Exception e) {
            System.out.println("Unexpected error => " + e.getMessage());
        }
        finally {
            dbClient.close();
        }
    }
}
