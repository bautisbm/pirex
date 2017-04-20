package gui;

import app.SourceProcessor;
import app.SearchEngine;
import app.Pair;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 * This class represents the GUI for Pirex. Pirex contains three main panels: search, load,
 * and summarize panels.
 * 
 * @author David Wickizer
 * @author Zach Berman
 *
 */
public class PirexGUI extends JFrame implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	private Container contentPane;
	private JPanel contentPanel, searchPanel, loadPanel, summarizePanel, removePanel;
    private JTabbedPane tabbedPane;
    private JList<String> shortFormList;
    private DefaultListModel<String> model;
    private JLabel numberOfRetrievedDoc;
    private JTextArea longFormTextArea, processTextArea, summaryTextArea;
    private JFormattedTextField loadFileField, titleField, authorField, removeField;
    private JButton processButton, browseButton, removeButton, clearButton, clearLong;
    private JComboBox<String> fileTypeComboBox;
    private JMenuBar menuBar;
    private JMenu optionsMenu, fileMenu, helpMenu;
    private JMenuItem exitItem, saveItem, loadItem, exportItem, documentsItem, sourcesItem; 
    private JMenuItem indexItem, aboutItem, helpItem;
    private JFileChooser sourcesFileChooser;
    private SourceProcessor sourceProcessor;
    private SearchEngine searchEngine;
    private TreeSet<Pair> pairs;
    private JComboBox<String> queryField;
    private String PIREX_STORE = System.getProperty("user.dir") + "/pirexData";
    private String PIREX_SOURCES = PIREX_STORE + "/Sources";
    private String PIREX_HELP = System.getProperty("user.dir") + "/pirexHelp";
  
    /**
     * Default constructor for GUI. Sets up the GUI when invoked .
     */
    public PirexGUI(SourceProcessor sourceProcessor, SearchEngine searchEngine)
    {   
        super();
    
	    this.sourceProcessor = sourceProcessor;
	    this.searchEngine = searchEngine;
	    sourcesFileChooser = new JFileChooser();
	    
	    setTitleBarIcon();
	    setTitle(System.getProperty("Pirex"));
	    addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                sourceProcessor.store.saveToFile();
                System.exit(JFrame.EXIT_ON_CLOSE);
            }
        });
	 
	    performLayout();
	    summaryTextArea.setText(sourceProcessor.opusSummary());
    }
  
    /**
     * Sets the title bar icon to the appropriate image.
     */
    public void setTitleBarIcon() 
    {
    	ArrayList<Image> images = new ArrayList<>();
        URL url1 = this.getClass().getResource("/resources/Pirex_Icon_16x16.png");
        ImageIcon icon1 = new ImageIcon(url1);
	    
        URL url2 = this.getClass().getResource("/resources/Pirex_Icon_32x32.png");
        ImageIcon icon2 = new ImageIcon(url2);
	    
        images.add(icon1.getImage());
        images.add(icon2.getImage());
	    
        this.setIconImages(images);	  
    }
  
    /**
     * Helper method that sets up the layout of the GUI.
     */
    private void performLayout()
    {
        contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
	    
		// Placing tab panel inside to create padding
		contentPanel = new JPanel();
	    
		// Tabbed Pane to hold all panels
		tabbedPane = new JTabbedPane();

		// Three main panels
		searchPanel = new JPanel();
		loadPanel = new JPanel();
		summarizePanel = new JPanel();
		contentPane.add(contentPanel, BorderLayout.CENTER);
    
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPanel.setLayout(new BorderLayout());
    
		createSearchPanel();
		createLoadPanel();
		createSummarizePanel();
		createMenuBar();
    
		// Add tabs to tab pane
		tabbedPane.addTab("Search for Documents", searchPanel);
		tabbedPane.addTab("Load Documents",  loadPanel);
		tabbedPane.addTab("Summarize Documents",  summarizePanel);
    
		// Add tab pane to content panel
		contentPanel.add(tabbedPane, BorderLayout.CENTER);  
    }
  
    /**
     * Copyright © 2008-2016 Mkyong.com, all rights reserved.
     * Code modified from Mkyong.com.
     * Creates a pirexHelp directory with all of the Help materials.
     * 
     * @param src Directory to copy from
     * @param dest Directory to copy to
     */
    private void createPirexHelp(File src, File dest)
    {
    	if(src.isDirectory())
    	{
    		// If directory not exists, create it
    		if(!dest.exists())
    		{
    			dest.mkdir();
    			System.out.println("Directory copied from " + src + "  to " + dest);
    		}

    		// List all the directory contents
    		String files[] = src.list();

    		for (String file : files) 
    		{
    			// Construct the src and dest file structure
    			File srcFile = new File(src, file);
    			File destFile = new File(dest, file);
    			// Recursive copy
    			createPirexHelp(srcFile,destFile);
    		}

    	}
    	else
    	{
    		try 
    		{
	    		// If file, then copy it
	    		// Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    		OutputStream out = new FileOutputStream(dest);
	
	    		byte[] buffer = new byte[1024];
	
	    		int length;
	    		
	    		// Copy the file content in bytes
	    		while ((length = in.read(buffer)) > 0)
	    			out.write(buffer, 0, length);
	 
	
	    		in.close();
	    		out.close();
	    		System.out.println("File copied from " + src + " to " + dest);
    		}
    		catch (IOException e) 
    		{
    			System.out.println(e.getMessage());
    		}
    	}
    		   	
    }
    /**
     * Helper method for creating search panel.
     */
    private void createSearchPanel()
    {
    	searchPanel.setLayout(new BorderLayout());
    	searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    	JPanel searchBar = new JPanel();
	    JPanel results = new JPanel();
	    results.setLayout(new GridLayout(2 , 1));
    
	    // Search bar
	    searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    searchBar.setLayout(new BorderLayout());
    
	    queryField = new JComboBox<>(new String[0]);
	    queryField.addActionListener(this);
	    queryField.setEditable(true);
    
	    clearButton = new JButton("Clear");
	    clearButton.addActionListener(this);
	    
	    searchBar.add(new JLabel("Query: "), BorderLayout.WEST);
	    searchBar.add(queryField, BorderLayout.CENTER);
	    searchBar.add(clearButton, BorderLayout.EAST);
	    
	    searchPanel.add(searchBar, BorderLayout.NORTH);
    
	    // Short form results panel
	    JPanel shortFormResults = new JPanel();
	    
	    model = new DefaultListModel<>();
	 
	    shortFormList = new JList<>(model);
	    shortFormList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    shortFormList.addListSelectionListener(this);
	    shortFormList.setBorder(BorderFactory.createLineBorder(Color.black));
	    JScrollPane shortFormScroll = new JScrollPane(shortFormList);
	    shortFormScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
	    JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    
	    numberOfRetrievedDoc = new JLabel("");
	    numberOfRetrievedDoc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    clearLong = new JButton("Clear Document");
	    clearLong.setVisible(false);
	    clearLong.addActionListener(this);
	    
	    middle.add(numberOfRetrievedDoc);
	    middle.add(clearLong);
    
	    shortFormResults.setLayout(new BorderLayout());
	    shortFormResults.add(shortFormScroll, BorderLayout.CENTER);
	    shortFormResults.add(middle, BorderLayout.SOUTH);
	    
	    results.add(shortFormResults);
	    
	    // Long form results panel
	    JPanel longFormResults = new JPanel();
	    
	    longFormTextArea = new JTextArea();
	    longFormTextArea.setEditable(false);
	    
	    JScrollPane longFormScrollPane = new JScrollPane(longFormTextArea);
	    longFormScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    longFormResults.setLayout(new BorderLayout());
	    longFormResults.add(longFormScrollPane);
	    
	    results.add(longFormResults);
	    
	    searchPanel.add(results, BorderLayout.CENTER);
    }
  
    /**
     * Helper method for creating load panel.
     */
    private void createLoadPanel()
    {
    	loadPanel.setLayout(new BorderLayout());
    	loadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
	    JPanel topLoadPanel = new JPanel();
	    topLoadPanel.setLayout(new GridLayout(4, 1));
	    topLoadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    // Text file loader
	    JPanel textFileSelection = new JPanel();
	    textFileSelection.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    textFileSelection.setLayout(new BorderLayout());
	    
	    JLabel textFileLabel = new JLabel("Text File: ");
	    
	    loadFileField = new JFormattedTextField();
	    loadFileField.setBorder(BorderFactory.createLoweredBevelBorder());
	    loadFileField.setEditable(false);
    
	    browseButton = new JButton("Browse");
	    browseButton.addActionListener(this);
	    
	    textFileSelection.add(textFileLabel, BorderLayout.WEST);
	    textFileSelection.add(loadFileField, BorderLayout.CENTER);
	    textFileSelection.add(browseButton, BorderLayout.EAST);
	    
	    topLoadPanel.add(textFileSelection);
	    
	    // Text file type
	    JPanel textFileType = new JPanel();
	    textFileType.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    textFileType.setLayout(new BorderLayout());
	    
	    JLabel textFileTypeLabel = new JLabel("Text File Type: ");
	    
//	    String[] fileTypes = new String[3];
//	    fileTypes[0] = "Project Gutenberg File";
//	    fileTypes[1] = "HTML File";
//	    fileTypes[2] = "Rich Text Format File";
	    
	    String[] fileTypes = new String[1];
	    fileTypes[0] = "Project Gutenberg File";
	    
	    fileTypeComboBox = new JComboBox<String>(fileTypes);
	    fileTypeComboBox.setEditable(false);
	    
	    textFileType.add(textFileTypeLabel, BorderLayout.WEST);
	    textFileType.add(fileTypeComboBox, BorderLayout.CENTER);
	    
	    topLoadPanel.add(textFileType);
	    
	    // Title and author
	    JPanel titleAuthorPanel = new JPanel();
	    titleAuthorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    titleAuthorPanel.setLayout(new GridLayout(1, 2));
	    
	    JPanel titlePanel = new JPanel();
	    titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
	    titlePanel.setLayout(new BorderLayout());
	    
	    JLabel titleLabel = new JLabel("Title: ");
	    titleField = new JFormattedTextField();
	    titleField.setBorder(BorderFactory.createLoweredBevelBorder());
	    
	    titlePanel.add(titleLabel, BorderLayout.WEST);
	    titlePanel.add(titleField, BorderLayout.CENTER);
	    
	    JPanel authorPanel = new JPanel();
	    authorPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    authorPanel.setLayout(new BorderLayout());
	    
	    JLabel authorLabel = new JLabel("Author: ");
	    authorField = new JFormattedTextField();
	    authorField.setBorder(BorderFactory.createLoweredBevelBorder());
		    
	    authorPanel.add(authorLabel, BorderLayout.WEST);
	    authorPanel.add(authorField, BorderLayout.CENTER);
	    
	    titleAuthorPanel.add(titlePanel, BorderLayout.WEST);
	    titleAuthorPanel.add(authorPanel, BorderLayout.EAST);
	    
	    topLoadPanel.add(titleAuthorPanel);
	    
	    // Process button
	    JPanel process = new JPanel();
	    process.setLayout(new BorderLayout());
	    
	    processButton = new JButton("Process");
	    processButton.addActionListener(this);
	    processButton.setEnabled(false);
	    
	    process.add(processButton, BorderLayout.WEST);
	    
	    JFormattedTextField area = new JFormattedTextField();
	    process.add(area, BorderLayout.CENTER);
	    area.setVisible(false);
	    
	    process.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
	    
	    topLoadPanel.add(process);
	    
	    loadPanel.add(topLoadPanel, BorderLayout.NORTH);
	    
	    // Bottom Text Display
	    processTextArea = new JTextArea();
	    processTextArea.setEditable(false);
	    processTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
	    
	    loadPanel.add(processTextArea, BorderLayout.CENTER);
    }
  
    /**
     * Helper method for creating summarize panel.
     */
    private void createSummarizePanel()
    {
	    summarizePanel.setLayout(new BorderLayout());
	    summarizePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    summaryTextArea = new JTextArea();
	    summaryTextArea.setEditable(false);
	    
	    JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);
	    summaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	
	    summarizePanel.add(summaryScrollPane, BorderLayout.CENTER);
	    
	    // Remove panel for remove button and textField
	    removePanel = new JPanel();
	    removePanel.setLayout(new FlowLayout());
	    
	    removeButton = new JButton("Remove");
	    removeButton.addActionListener(this);
	    
	    removeField = new JFormattedTextField();
	    removeField.setPreferredSize(new Dimension(50, 25));
	    
	    removePanel.add(removeButton);
	    removePanel.add(removeField);
	    
	    summarizePanel.add(removePanel, BorderLayout.SOUTH);
    }
  
    /**
     * Helper method for creating menu bar.
     */
    private void createMenuBar()
    {
    	menuBar = new JMenuBar();
      
    	// The File menu
    	fileMenu = new JMenu("File");

    	saveItem = new JMenuItem("Save Query");
    	saveItem.addActionListener(this);
      
	    loadItem = new JMenuItem("Load Queries");
	    loadItem.addActionListener(this);
	      
	    exportItem = new JMenuItem("Export Results");
	    exportItem.addActionListener(this);
	      
	    exitItem = new JMenuItem("Exit");
	    exitItem.addActionListener(this);
      
	    fileMenu.add(saveItem);
	    fileMenu.add(loadItem);
	    fileMenu.add(exportItem);
	    fileMenu.add(exitItem);
      
	    menuBar.add(fileMenu);

	    // The Options menu
	    optionsMenu = new JMenu("Options");
	
	    documentsItem = new JMenuItem("Documents");
	    documentsItem.addActionListener(this);
      
	    sourcesItem = new JMenuItem("Sources");
	    sourcesItem.addActionListener(this);
      
	    helpItem = new JMenuItem("Help Data");
	    helpItem.addActionListener(this);
      
	    optionsMenu.add(documentsItem);
	    optionsMenu.add(sourcesItem);
	    optionsMenu.add(helpItem);
	
	    menuBar.add(optionsMenu);

	    // The Help menu
	    helpMenu = new JMenu("Help");

	    aboutItem = new JMenuItem("About");
	    aboutItem.addActionListener(this);
      
	    indexItem = new JMenuItem("Index");
	    indexItem.addActionListener(this);
      
	    helpMenu.add(aboutItem);
	    helpMenu.add(indexItem);
      
	    menuBar.add(helpMenu);

	    // The menu bar
	    setJMenuBar(menuBar);
    } 
  
    /**
     * Highlights the search terms in the long form display if found.
     */
    private void highlight()  
    {	
    	TreeSet<String> prevTerms = searchEngine.getPreviousTerms(); 
    	ArrayList<Interval> intervals = new ArrayList<>();
    	int curr;
    	int found;
	  
    	// Highlight word(s)
    	try 
    	{
    		Highlighter highlighter = longFormTextArea.getHighlighter();
    		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
    		String text = " " + longFormTextArea.getText().toLowerCase() + " ";
          
    		// Find all intervals of the text area to highlight
    		for (String term : prevTerms)
    		{
    			found = 0;
    			curr = 0;
    			while (found >= 0)
    			{
    				found = text.indexOf(term, curr);
    				curr = found + term.length();
    				if (found != -1 && (!Character.isLetter(text.charAt(found - 1)) 
    						&& text.charAt(found - 1) != 0x27)
    						&& !Character.isLetter(text.charAt(curr))) 
    					intervals.add(new Interval(found - 1, curr - 1));
    			}
    		}
      
    		// Add proper highlight intervals
    		for (Interval interval : intervals) 
    			highlighter.addHighlight(interval.getStartIndex(), interval.getEndIndex(), painter);    
    	} 
    	catch (BadLocationException e) 
    	{
    		e.printStackTrace();
    	} 
    }

    /**
     * Responsible for displaying the long form of a document that is selected from the short form
     * output list. Also responsible for calling the highlight() method to highlight the terms
     * being searched for within the specified document.
     * 
     * @param e The event that occurs when a new value from the short form output list is selected
     */
    @SuppressWarnings("unchecked")
    public void valueChanged(ListSelectionEvent e) 
    {
    	JList<String> list;
    	String selected;
	  
    	list = (JList<String>) (e.getSource());
    	selected = (String) list.getSelectedValue();
	  
    	// Pick out long form to be displayed
    	for (Pair pair : pairs) {
    		if (pair.equals(selected)) {
    			selected = pair.getLongForm();
    		}
    	}
	   
    	longFormTextArea.setText(selected);
    	highlight();
    }

    /**
     * Handles the firing of all events (except the event handled by the valueChanged() method) 
     * that occur on this GUI. (i.e. button presses, key presses, etc.)
     * 
     * @param event The event that is fired from a GUI interaction
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
    	if (event.getSource() == browseButton) browseButtonClicked();
    	else if (event.getSource() == processButton) processButtonClicked();
    	else if (event.getSource() == removeButton) removeButtonClicked();
    	else if (event.getSource() == queryField) queryFieldInteractedWith();
    	else if (event.getSource() == clearButton) clearButtonClicked();
    	else if (event.getSource() == clearLong) longFormTextArea.setText("");
    	else if (event.getSource() == saveItem) saveItemClicked();
    	else if (event.getSource() == loadItem) loadItemClicked();
    	else if (event.getSource() == exportItem) exportItemClicked();
    	else if (event.getSource() == exitItem) exitItemClicked();
    	else if (event.getSource() == documentsItem) documentsItemClicked();
    	else if (event.getSource() == sourcesItem) sourcesItemClicked();
    	else if (event.getSource() == helpItem) helpItemClicked();
    	else if (event.getSource() == indexItem) indexItemClicked();
    	else if (event.getSource() == aboutItem) aboutItemClicked();
  	}
 
    /**
     * Handles the events of a browse button click. Opens a File Chooser to browse for 
     * a Source file to load in to the Document Store.
     */
    private void browseButtonClicked()
    {
    	// Set the current directory for the filechooser to be the Sources directory in pirexData
    	File file = new File(PIREX_SOURCES);
    	file.mkdirs();
    	sourcesFileChooser.setCurrentDirectory(file);
	  
    	// Determine filter
    	if (fileTypeComboBox.getSelectedItem() == "Project Gutenberg File")
    		sourcesFileChooser.setFileFilter(new FileNameExtensionFilter("Project Gutenberg Files", "txt"));
//    	else if (fileTypeComboBox.getSelectedItem() == "HTML File")
//    		sourcesFileChooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
//    	else
//    		sourcesFileChooser.setFileFilter(new FileNameExtensionFilter("Rich Text Format Files", "rtf"));
	  
    	// Get Source file
    	int returnVal = sourcesFileChooser.showOpenDialog(PirexGUI.this);
    	if (returnVal == JFileChooser.APPROVE_OPTION)
    	{
    		file = sourcesFileChooser.getSelectedFile();
    		loadFileField.setText(file.getAbsolutePath());
		  
    		// If project Gutenberg File...extract author/title
    		if (fileTypeComboBox.getSelectedItem() == "Project Gutenberg File") {
    			titleField.setText(sourceProcessor.extractTitle(loadFileField.getText()));
    			authorField.setText(sourceProcessor.extractAuthor(loadFileField.getText()));  
    		}
			  
    		processButton.setEnabled(true);
    		processTextArea.setText("");
    	}     
    }

  	/**
  	 * Handles the events of a process button click. Adds the selected Source to the Document
  	 * Store and displays the summary information.
  	 */
  	private void processButtonClicked()
  	{
  	  
  	  String fileName = loadFileField.getText();
  		String title = titleField.getText();
  		String author = authorField.getText();
	  
  		// Clear fields
  		titleField.setText("");
  		authorField.setText("");
  		loadFileField.setText("");
	  
  		// Reset combo box
  		fileTypeComboBox.setSelectedItem("Project Gutenberg File");
	  
  		// Disable process button
  		processButton.setEnabled(false);
	  
  		// Extract the opus if the file is not already in the system
  		if (sourceProcessor.opusSummary().contains(fileName))  
  		{
  			processTextArea.setText("File already in system");
  		}
  		else 
  		{
  			sourceProcessor.extractOpus(fileName, title, author);
  			processTextArea.setText(sourceProcessor.loadSummary());
	  	}

	  	summaryTextArea.setText(sourceProcessor.opusSummary());
  	}
  
	/**
	 * Handles the events of a remove button click. Removes the specified Opus from the 
	 * Document Store. 
	 */
	private void removeButtonClicked() 
	{
		// Remove Opus from document store and inverted index
		try
		{
			int opusNumber = Integer.parseInt(removeField.getText());
			  
			// Bounds check
			if (opusNumber < 0 || opusNumber >= sourceProcessor.getDocumentStore().size())
			{
				JOptionPane.showMessageDialog(null, "There is no Opus: " + opusNumber);
				removeField.setText("");
			}
			else
			{
				// Remove opus and redisplay results
				sourceProcessor.removeOpus(opusNumber);
				removeField.setText("");
				summaryTextArea.setText(sourceProcessor.opusSummary());	  
			}
		} 
		catch(NumberFormatException e) 
		{
			JOptionPane.showMessageDialog(null, "Enter an Integer Value");
			removeField.setText("");
		} 
	}
	  
	/**
	 * Handles the events of an interaction with the query field (combobox). When a user
	 * presses enter a query is performed with whatever search terms are in the field of 
	 * the query field. Short form results are displayed in a list below.
	 */
    private void queryFieldInteractedWith()
	{
    	// Retrieve output
    	pairs = searchEngine.retrieveDocuments((String) queryField.getEditor().getItem());
		  
    	// Display short form results
    	DefaultListModel<String> model = new DefaultListModel<>();
    	for (Pair pair : pairs) model.addElement(pair.getShortForm());
    	shortFormList.setModel(model);
		  
    	// Display number of retrieved documents
    	if (pairs.size() > 0)
    		numberOfRetrievedDoc.setText("Retrieved " + pairs.size() + " Documents");
    	else 
    		numberOfRetrievedDoc.setText("No results found");
		  
    	// Clear long form text field 
    	longFormTextArea.setText("");
    	clearLong.setVisible(true);
		  
    	// Store query in drop down list 
    	String text = (String) queryField.getEditor().getItem();
    	boolean duplicate = false;
    	for (int i = 0; i < queryField.getItemCount(); i++) 
    		if (queryField.getItemAt(i).equals(text)) duplicate = true;
    	if (!duplicate) queryField.addItem(text);  
	}
    
    /**
     * Handles the events of a clear button click. Clears all components on the Search Panel.
     */
    private void clearButtonClicked() 
    {
    	clearLong.setVisible(false);
    	queryField.getEditor().setItem("");
		longFormTextArea.setText("");
		numberOfRetrievedDoc.setText("");
		DefaultListModel<String> newModel = new DefaultListModel<>();
		shortFormList.setModel(newModel);    	
    }
    
    /**
     * Handles the events of a File>Save Query item click. Saves the current query to file. 
     * Defaults to the pirexData/save directory. If directory does not exist it will be created.
     */
    private void saveItemClicked()
    {
    	String saveText = (String) queryField.getEditor().getItem();
		
    	// Bounds check
    	if (saveText.equals("")) 
    	{
    		JOptionPane.showMessageDialog(null, "No data to be saved. Please enter a query");
    	}
    	else 
    	{ 
    		// Create path to saves
    		File file = new File(PIREX_STORE + "/saves");
    		file.mkdirs();

    		JFileChooser save = new JFileChooser(file);
    		save.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    		save.setSelectedFile(new File("Untitled.txt"));
    		save.setDialogTitle("Save Current Query");
			  
    		// Save query to file 
    		int rVal = save.showSaveDialog(PirexGUI.this);
    		if (rVal == JFileChooser.APPROVE_OPTION) {
	    	  
    			String fileName = save.getSelectedFile().getName();
    			String path = save.getCurrentDirectory().toString();
    			try 
    			{
    				PrintWriter writer = new PrintWriter(path + "/" + fileName, "UTF-8");
    				writer.print(saveText);
    				writer.close();

    			} 
    			catch (IOException e)
    			{
    				System.out.println(e.getMessage());
    			} 
    		}
    	}    	
    }
    
    /**
     * Handles the events of a File>Load Queries item click. Loads previously saved queries. 
     * Defaults by looking in the pirexData/Saves directory.
     */
    private void loadItemClicked() 
    {
    	// Make path to saves
    	File file = new File(PIREX_STORE + "/saves");
    	file.mkdirs();

    	JFileChooser load = new JFileChooser(file);
    	load.setMultiSelectionEnabled(true);
    	load.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    	load.setDialogTitle("Load Previous Queries");
		  
    	// Load file(s)
    	int rVal = load.showOpenDialog(PirexGUI.this);
    	if (rVal == JFileChooser.APPROVE_OPTION) {
    		File[] files = load.getSelectedFiles();
    		LinkedList<String> lines = new LinkedList<>();
    		String path = load.getCurrentDirectory().toString();
    		for (File f : files) 
    		{
    			String fileName = f.getName();
    			File input = new File(path + "/" + fileName);
    			String line = "";
					
    			try 
    			{
    				BufferedReader in = new BufferedReader(new FileReader(input));
    				while((line = in.readLine()) != null) lines.add(line);
    				in.close();
    			} 
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}	  
    		}
			  
    		// Populate drop down list with non duplicate load files
    		for (String query : lines)
    		{
    			boolean duplicate = false;
    			for (int i = 0; i < queryField.getItemCount(); i++) 
    				if (queryField.getItemAt(i).equals(query)) duplicate = true;
    			if (!duplicate) queryField.addItem(query); 
    		}
    	}
    }
    
    /**
     * Handles the events of a File>Export Results click. Exports the query results to a file
     * in the pirexData/exports directory. If exports directory does not exist it will be 
     * created.
     */
    private void exportItemClicked()
    {
    	File file = new File(PIREX_STORE + "/exports");
    	file.mkdirs();
		  
    	// Bounds check
    	if (pairs == null || pairs.isEmpty()) 
    		JOptionPane.showMessageDialog(null, "No data to export");
    	else 
    	{
    		// Export dialog
    		JFileChooser export = new JFileChooser(file);
    		export.setFileFilter(new FileNameExtensionFilter("Document Files", "doc"));
    		export.setSelectedFile(new File("Untitled.doc"));
    		export.setDialogTitle("Export Results");
    		
    		// Export to file
			int rVal = export.showSaveDialog(PirexGUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION)
			{
				String fileName = export.getSelectedFile().getName();
				String path = export.getCurrentDirectory().toString();
				try 
				{
					PrintWriter writer = new PrintWriter(path + "/" + fileName, "UTF-8");
					for (Pair pair : pairs) {
						writer.println(pair.getShortForm() + "\n");
						writer.println(pair.getLongForm());
						writer.println("\n");
					}
					writer.close();
				} 
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				} 
			}
    	}	
    }
    
    /**
     * Handles the events of a File>Exit click.
     */
    private void exitItemClicked() 
    {
        sourceProcessor.store.saveToFile();
        System.exit(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Handles the events of a Options>Documents click. Opens a File Chooser which allows the 
     * user to change the Documents Store location if they desire.
     */
    private void documentsItemClicked()
    {
    	JFileChooser documentStoreSelector = new JFileChooser();
    	documentStoreSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	documentStoreSelector.setAcceptAllFileFilterUsed(false);
    	documentStoreSelector.setDialogTitle("Select New Document Store");
		  
    	// Update data 
    	if (documentStoreSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    	{ 
    		searchEngine.setDataStoreLocation(documentStoreSelector.getSelectedFile().toString());
    		sourceProcessor.setDataStoreLocation(documentStoreSelector.getSelectedFile().toString());
    		PIREX_STORE = documentStoreSelector.getSelectedFile().toString();
    		PIREX_SOURCES = PIREX_STORE + "/Sources";
    		
    		// Update Sources directory
    		File file = new File(PIREX_SOURCES);
    		file.mkdirs();
    		sourcesFileChooser.setCurrentDirectory(file);
    		summaryTextArea.setText(sourceProcessor.opusSummary()); 
    	}      	
    }
    
    /**
     * Handles the events of a Options>Sources click. Opens a File Chooser which allows the 
     * user to change the Sources Store location if they desire.
     */
    private void sourcesItemClicked()
    {
	    // Choose new location for Sources directory
    	JFileChooser sourcesStoreSelector = new JFileChooser();
		sourcesStoreSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		sourcesStoreSelector.setAcceptAllFileFilterUsed(false);
		sourcesStoreSelector.setDialogTitle("Select New Sources Store");
		  
		// If selected update the Sources directory
		if (sourcesStoreSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		{ 
			sourcesFileChooser.setCurrentDirectory(sourcesStoreSelector.getSelectedFile());
			PIREX_SOURCES = sourcesStoreSelector.getSelectedFile().toString();
		}
    }

    /**
     * Handles the events of a Options>Help Data click. Opens a File Chooser which allows the 
     * user to change the Help Data Store location if they desire.
     */
    private void helpItemClicked() 
    {
	    // Choose new location for pirexHelp 
    	JFileChooser helpSelector = new JFileChooser();
		helpSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		helpSelector.setAcceptAllFileFilterUsed(false);
		helpSelector.setDialogTitle("Select New Help Directory");
		  
		// If selected update the Sources directory
		if (helpSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		{ 
			String old = PIREX_HELP;
			PIREX_HELP = helpSelector.getSelectedFile().toString();
			createPirexHelp(new File(old), new File(PIREX_HELP));
		}
    }
    
    /**
     * Handles the events of a Help>Index click. Displays the help page which explains various
     * features about Pirex.
     */
    private void indexItemClicked() 
    {
    	try 
    	{
    		PIREX_HELP = PIREX_HELP.replaceAll("\\\\", "/"); // Make windows happy
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("file://" + PIREX_HELP + "/help.html");
            desktop.browse(uri); 	
    	}
    	catch (Exception e) 
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
    /**
     * Handles the events of a Help>About click. Displays the about page for Pirex.
     */
    private void aboutItemClicked() 
    {
    	String aboutInfo = "Pirex allows users to input arbitrary plain-text documents.\n"
    					 + "It indexes and stores input texts permanently in the file\n"
    					 + "system so they can be searched later. It allows users to enter\n"
    					 + "queries to retrieve paragraphs from stored texts matching the\n"
    					 + "queries, and displays retrieved paragraphs to users. Pirex has\n"
    					 + "a graphical user interface for all its functions. Pirex may\n"
    					 + "include various auxiliary features to enhance search capabilities\n"
    					 + "and results investigation.\n\n\n"
    					 + "Developed by:\n\n"
    					 + "David Wickizer\n"
    					 + "Alex Flores\n"
    					 + "Zach Berman\n"
    					 + "Juliana Doebler\n"
    					 + "Brandon Bautista\n"
    					 + "Amanda Harner\n\n\n"
    					 + "Development Environment: Eclipse\n"
    					 + "Programming Language: Java\n"
    					 + "Class: CS345 Fall 2016";

    	new AboutDialog(this, "About Pirex", aboutInfo);
    }
     
}

/**
 * Dialog box for the Help>About page. Displays basic information about Pirex.
 * 
 * @author David Wickizer
 *
 */
class AboutDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for setting up dialog.
	 * 
	 * @param parent Parent frame
	 * @param title Title of the dialog box
	 * @param message Message to be displayed
	 */
	public AboutDialog(JFrame parent, String title, String message) 
	{
	    super(parent, title, true);
	    
	    if (parent != null)
	    {
	    	Dimension parentSize = parent.getSize(); 
	    	Point p = parent.getLocation(); 
	    	setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
	    }
	    
	    // Get image for label
        URL url = this.getClass().getResource("/resources/Pirex_Logo.png");
        ImageIcon icon = new ImageIcon(url);
        
        // Logo label
        JLabel logo = new JLabel(icon);
        logo.setBorder(BorderFactory.createEtchedBorder());
        
        // Text area with information 
        JTextArea infoText = new JTextArea(message);
        infoText.setEditable(false);
        
        // Add logo and message to message pane
	    JPanel messagePane = new JPanel(new BorderLayout());
	    messagePane.add(logo, BorderLayout.NORTH);
	    messagePane.add(infoText, BorderLayout.SOUTH);
	    getContentPane().add(messagePane);
	    
	    // Add ok button to button pane
	    JPanel buttonPane = new JPanel();
	    JButton button = new JButton("OK"); 
	    buttonPane.add(button); 
	    button.addActionListener(this);
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack(); 
	    setResizable(false);
	    setVisible(true);
	}
	  
	/**
	 * Handles the event of an Ok button click. Closes Dialog.
	 * 
	 * @param e The event of an Ok button click
	 */
	public void actionPerformed(ActionEvent e) 
	{
		setVisible(false); 
	    dispose(); 
	}
}
