package mongoCourse.exam;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Solution7 {

    static MongoClient dbClient = new MongoClient(new ServerAddress("localhost", 27017));

    private static boolean imageExistsInAlbum(MongoCollection<Document> albumCollection, Double imgId) {
        Bson imgExistsFilter = Filters.and(Filters.in("images", imgId));
        List<Document> documents = albumCollection.find(imgExistsFilter).limit(1).into(new ArrayList<Document>());
        return documents.size() == 1;
    }

    private static void removeImgFrmCollection(MongoCollection<Document> imagesCollection, Double identifier) {
        Bson deleteByImdIdFilter = Filters.and(Filters.eq("_id", identifier));
        imagesCollection.deleteOne(deleteByImdIdFilter);
    }

    public static void main(String[] args) {
        try {
            final MongoDatabase studentDatabase = dbClient.getDatabase("test");
            final MongoCollection<Document> imgCollection = studentDatabase.getCollection("images");
            final MongoCollection<Document> albCollection = studentDatabase.getCollection("albums");

            Bson queryProjection = Projections.fields(Projections.exclude(Arrays.asList("tags", "height", "width")));
            Bson querySort = new Document("_id",1); //ascending order
            List<Document> imgDocs = imgCollection.find().projection(queryProjection).sort(querySort).into(new ArrayList<Document>());

            // Remove orphan images
            for (Document doc : imgDocs) {
                Double identifier = (Double) doc.get("_id");
                if (!imageExistsInAlbum(albCollection, identifier)) {
                    removeImgFrmCollection(imgCollection, identifier);
                    //System.out.println("Deleted image with identifier -> " + identifier);
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
