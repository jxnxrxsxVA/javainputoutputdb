//Janissa Hamilton
//11/24/2019
//CSC 202
//I used my own code which had the benefit of resources provided previously for other projects
//Also the Primefaces examples (primefaces.org/showcase.)
//Thank you so much for helping me re-organize and fix my code! (and extending the due date)
//I used this for WELD errors I had -- https://stackoverflow.com/questions/48410451/why-do-i-need-a-no-args-constructor-to-use-applicationscoped-beans-with-construc


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.ListIterator;

@ManagedBean(name = "hashRecord")
public class HashRecord {

    private ArrayList<String> hashRecords = new ArrayList<>();


    private String stringSSN;


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
                String sStart = "SSN=";
                stringSSN = entry.substring(entry.indexOf(sStart) + sStart.length(), entry.indexOf(sStart) + sStart.length() + 44);

            } catch (Exception e) {
                stringSSN = "N/A";
            }
            hashRecords.add(stringSSN);

        }
    }


    public void setHashRecords(ArrayList<String> hashRecords) {
        this.hashRecords = hashRecords;
    }

    public ArrayList<String> getHashRecords() {
        return hashRecords;
    }
}
