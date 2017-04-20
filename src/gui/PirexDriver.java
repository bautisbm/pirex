package gui;
import javax.swing.JFrame;
import app.SourceProcessor;
import app.SearchEngine;
import app.Store;

/**
 * Pirex allows users to input arbitrary plain-text documents. It indexes and stores input texts 
 * permanently in the file system so they can be searched later. It allows users to enter queries
 * to retrieve paragraphs from stored texts matching the queries, and displays retrieved 
 * paragraphs to users. Pirex has a graphical user interface for all its functions. Pirex may 
 * include various auxiliary features to enhance search capabilities and results investigation.
 * 
 * @author David Wickizer
 * @author Alex Flores
 * @author Zach Berman
 * @author Juliana Doebler
 * @author Brandon Bautista 
 * @author Amanda Harner
 */
public class PirexDriver
{
	/**
	 * Entry point for Pirex. Initializes the GUI and runs Pirex.
	 * 
	 * @param args Command line arguments
	 */
    public static void main(String[] args)
    {
    	Store store = new Store(); 
    	JFrame frame = new PirexGUI(new SourceProcessor(store), new SearchEngine(store));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(1000,600);  
    
    	frame.setVisible(true);
    }
}
