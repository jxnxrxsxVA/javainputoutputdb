import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

@ManagedBean(name ="record")
@RequestScoped
public class Record implements Serializable {


    private ArrayList<String> records = new ArrayList<>();

    private String stringName;

    public void setRecords(ArrayList<String> records) {
        this.records = records;
    }


    @PostConstruct
    public void init() {
        //Mongo

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("local");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("personalInfo");
        MongoCursor<Document> point1 = mongoCollection.find().iterator();

        //First creating the list of strings which contain the database records
        ArrayList<String> mongoData;
        mongoData = new ArrayList<String>();
        while (point1.hasNext()) {
            mongoData.add(point1.next().toString());
        }


        //now creating the strings that will go into the ArrayList


        ListIterator<String> listItr = mongoData.listIterator();
        while (listItr.hasNext()) {
            String entry = listItr.next();

            //try catch block because it is possible a record could have different field names or length, etc.
            try {
                //grab substrings from the data to separate the names and hashed ssns, put them into their lists
                String nStart = "Name=";
                String nEnd = "}";
                stringName = "      " + entry.substring(entry.indexOf(nStart) + nStart.length(), entry.indexOf(nEnd));

            } catch (Exception e) {
                stringName = "N/A";
            }
            records.add(stringName);

        }
    }


    public ArrayList<String> getRecords() {
        return records;
    }





}