package app;

import java.util.*;
import java.io.*;

/**
 * 
 * @author Brandon Bautista
 * 
 * Utility class for the Store
 *
 */
class StoreUtil
{
    /**
     * Generic serialization method. Serializes an object to a file.
     * 
     * @param obj The object to be serialized
     * @param filename The file path that the object will be written to.
     * 
     */
    public static final <T extends Serializable> void serializeObj(T obj, String filename)
    {
        FileOutputStream out;
        ObjectOutputStream serializer;
        File file = new File(filename);
        file.getParentFile().mkdirs();

        try 
        {
            out = new FileOutputStream(file);
            serializer = new ObjectOutputStream(out);
     
            serializer.writeObject(obj);
            serializer.flush();
            out.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * Generic deserialization method. Deserializes a file into an Object in memory.
     * 
     * @param filename The file to deserialize
     * @return Returns the Object being deserialized
     */
    public static final Object deserializeObj(String filename)
    {
      FileInputStream in;
      ObjectInputStream  deserializer;
          File file = new File(filename);
          file.getParentFile().mkdirs();
      
      try 
      {	
        in = new FileInputStream(file);
        deserializer = new ObjectInputStream(in);
         
        Object obj = deserializer.readObject();
        in.close();
              return obj;
      } 
      catch (Exception ex) 
      {
              return null;
      }
    }

}

/**
 * Class which handles functionality in the "Data Store" layer.
 * 
 * @author Alex Flores
 *
 */
public class Store 
{
	  private String PIREX_STORE = System.getProperty("user.dir") + "/pirexData"; 

    public ArrayList<Opus> documentStore;
    public Indexer indexer;

    /**
     * Constructor for Store objects.
     */
    public Store()
    {
        init();
    }
    
    /**
     * Constructor for testing purposes 
     */
    public Store(int testing)
    {
        this.documentStore = new ArrayList<>();
        this.indexer = new Indexer();
    }

    /**
     * Initializes the store, automatically loading any documents and inverted index that are saved at
     * the current PIREX_STORE location.
     */
    @SuppressWarnings("unchecked")
    private void init()
    {       
        Object loadedObj;
        
        loadedObj = StoreUtil.deserializeObj(PIREX_STORE + "/DocumentStore.dat");
        this.documentStore = (loadedObj != null) ? (ArrayList<Opus>)loadedObj : new ArrayList<Opus>();

        loadedObj = StoreUtil.deserializeObj(PIREX_STORE + "/InvertedIndex.dat");
        this.indexer = (loadedObj != null) ? (Indexer)loadedObj : new Indexer();
    }

    /**
     * Saves the current state of the document store and the inverted index to file.
     */
    public void saveToFile()
    {
        StoreUtil.serializeObj(this.documentStore, PIREX_STORE + "/DocumentStore.dat");
        StoreUtil.serializeObj(this.indexer, PIREX_STORE + "/InvertedIndex.dat");
    }

	/**
	 * Sets a new location for the Data Store.
	 * 
	 * @param path The new path to the Data Store
	 */
	public void setDataStoreLocation(String path) 
	{
		this.PIREX_STORE = path;
        init();
	}	
}