/**
 * Created by stoh on 13/06/2015.
 */
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/*
 * This homework aims to teach students on how to delete (pull) a nested document from an array of documents.
 * The schema of this work looks something like this.
 *
 * > db.students.find( { _id : 137 } ).pretty( )
{
        "_id" : 137,
        "name" : "Tamika Schildgen",
        "scores" : [
                {
                        "type" : "exam",
                        "score" : 4.433956226109692
                },
                {
                        "type" : "quiz",
                        "score" : 65.50313785402548
                },
                {
                        "type" : "homework",
                        "score" : 54.75994689226145
                },
                {
                        "type" : "homework",
                        "score" : 89.5950384993947
                }
        ]
}
 *
 */
public class myAssignment {

    private static MongoClient databaseClient = new MongoClient(new ServerAddress("localhost", 27017));
    private static final String ASSIGNMENT3_1_DB_NAME = "school";
    private static final String ASSIGNMENT3_1_COLLECTION_NAME = "students";

    public static void main(String[] args) {
        System.out.println("Executing 3_1 assignment.");
        try {
            final MongoDatabase database = databaseClient.getDatabase(ASSIGNMENT3_1_DB_NAME);
            MongoCollection<Document> studentCollection = database.getCollection(ASSIGNMENT3_1_COLLECTION_NAME);

            Bson sortByStuIdent = new Document("_id", 1);
            List<Document> documents = studentCollection.find().sort(sortByStuIdent).into(new ArrayList<Document>());

            for (Document document : documents) {
                Double studentId = (Double) document.get("_id");
                String studentName = (String) document.get("name");
                String workType = "";
                Double homeWorkScore = 0.0;
                Double lowestHwScore = Double.MAX_VALUE;
                List<Document> scoresItem = document.get("scores", List.class);

                for (Document item : scoresItem) {
                    workType = (String) item.get("type");
                    homeWorkScore = (Double) item.get("score");

                    if (workType.equals("homework") && homeWorkScore < lowestHwScore) {
                        lowestHwScore = homeWorkScore;
                    }
                }
                studentCollection.updateOne(
                        new Document("_id", studentId),
                          new Document("$pull",
                                  /*filter=*/new Document("scores", new Document("type", "homework").append("score", lowestHwScore))));

                System.out.println(
                        String.format("Pulled nested score doc => { student id = %s studentName = %s, workType = %s, score = %s ",
                                studentId.toString(), studentName, workType, homeWorkScore.toString()));
            }
        } catch (Exception e) {
            System.out.println("Unexpected error => " + e.getMessage());
        }
        finally {
            databaseClient.close();
        }
    }
}
