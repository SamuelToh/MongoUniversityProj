package course;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // XXX HW 3.2,  Work Here
        Bson linkFilter = Filters.eq("permalink", permalink);
        return postsCollection.find(linkFilter).first();
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        List<Document> posts;
        Bson sortByDescDate = new Document("date", -1);
        posts = postsCollection.find().sort(sortByDescDate).limit(limit).into(new ArrayList<Document>());

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // XXX HW 3.2, Work Here
        /**********************************
         * My schema design
         * Posts
        {
            "_id" : xxxxxxxxxxxxxxxxxx,
            "author" : "Tamika Schildgen",
            "title: : "love java"
            "body" : "hello world!"
            "permaLink" : "www.blah.com"
            "dates" : 23-01-2011
            "tags" : [ "xxx", "yyyy", "zzzz" ]
            "comments" : [ { A: B }, { C: D } ]
        }
        **************************************/

        // Build the post object and insert it
        Document post = new Document();
        post.append("author", username)
                .append("title", title)
                .append("body", body)
                .append("permalink", permalink)
                .append("date", new Date())
                .append("tags", tags)
                .append("comments", Collections.emptyList());

        postsCollection.insertOne(post);

        return permalink;
    }
    

    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        Document comment = new Document();
        comment.append("author", name);
        comment.append("body", body);
        if (email != null && !email.equals("")) comment.append("email", email);

        Document pushCommentOp = new Document("$push", /*nested document=*/new Document("comments", comment));
        Bson filterByPermLink = Filters.eq("permalink", permalink);
        postsCollection.updateOne(filterByPermLink, pushCommentOp);
    }
}
