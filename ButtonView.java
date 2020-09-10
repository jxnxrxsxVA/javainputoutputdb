import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.primefaces.*;



@Named
@RequestScoped
@ManagedBean(name = "buttonView")
public class ButtonView {

    String hashedkey;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String ssn;

    public String getSsn() {
        return hashedkey;
    }

    public void setSsn(String ssn) throws NoSuchAlgorithmException {
        this.ssn = ssn;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(ssn.getBytes(StandardCharsets.UTF_8));
        hashedkey = Base64.getEncoder().encodeToString(hash);
    }

//Mongo objects

    MongoClient mongoClient = new MongoClient("localhost", 27017);
    MongoDatabase mongoDatabase = mongoClient.getDatabase("local");
    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("personalInfo");
    MongoCursor<Document> mongoCursor = mongoCollection.find().iterator();

      String fieldName = "SSN";

   //Buttons
    public void save() {
        //Check if the SSN is in the collection already, if it is change the name to match the new name.
                FindIterable fi = mongoCollection.find(Filters.eq(fieldName, getSsn()));
                MongoCursor cursor = fi.iterator();
                while(cursor.hasNext()) {
                    Document filter = new Document("SSN", getSsn());
                    Document name = new Document();
                    name.append("Name", getName());
                    Document update = new Document("$set", name);
                    mongoCollection.updateOne(filter, update);
                    addMessage("Updated");
                }
                Document save = new Document("SSN", "Name")
                .append("SSN", getSsn())
                        .append ("Name", getName());
                mongoCollection.insertOne(save);
                addMessage("New Record Created");
        }


    public void update() {
            Document filter = new Document("SSN", getSsn());
            Document name = new Document();
            name.append("Name", getName());
            Document update = new Document("$set", name);

            mongoCollection.updateOne(filter, update);
            addMessage("Data updated");

    }

    public void delete() {
            mongoCollection.deleteOne(Filters.eq("SSN", getSsn()));
            addMessage("Data deleted");
    }


    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

