package app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import app.controller.mainController;
import app.model.*;

@SuppressWarnings("serial")
public class mainView extends JFrame {
	
	public static JTree tree=null;
	public static DefaultTreeModel treeModel ;
	public static imagePanel imagePanel; 
	public JSplitPane splitPane;
	public listPanel listScrollPane;
	public static DicomInputStream dis = null;
	public static DicomObject dcm = null;
	public File file;
	public File[] files;
	public DefaultMutableTreeNode top = new DefaultMutableTreeNode("");
	public static DefaultMutableTreeNode name = null;
	public static DefaultMutableTreeNode img = null;
	public static DefaultMutableTreeNode study = null;
	public static DefaultMutableTreeNode series = null;
	public thresholdPanel tPanel;
	public gaussPanel gPanel;
	public medianFilterPanel mPanel;
	public mainController mc;
	
	public mainView(mainController mController) throws IOException{
		mc = mController;
		setTitle("Przegladarka DICOM");
		setBackground(Color.white);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("Plik");
		JMenu submenu = new JMenu("Filtracja");
		submenu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menu);
	
		JMenuItem menuItem1 = new JMenuItem("Otworz");
		JMenuItem menuItem2 = new JMenuItem("Zamknij");
		JMenuItem menuItem3 = new JMenuItem("Filtracja");
		
		JMenuItem menuItem4 = new JMenuItem("Wygladzanie Gaussowskie");
		JMenuItem menuItem5 = new JMenuItem("Filtr Medianowy");
		JMenuItem menuItem6 = new JMenuItem("Progowanie");
		JMenuItem menuItem7 = new JMenuItem("Wyczysc");
		
		menuItem1.addActionListener(mController);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem2.addActionListener(mController);
		menuItem3.addActionListener(mController);
		menuItem4.addActionListener(mController);
		menuItem5.addActionListener(mController);
		menuItem6.addActionListener(mController);
		menuItem7.addActionListener(mController);

		menu.add(menuItem1);
		menu.add(submenu);
		menu.add(menuItem2);
		
		submenu.add(menuItem4);
		submenu.add(menuItem5);
		submenu.add(menuItem6);
		submenu.add(menuItem7);
		
		setJMenuBar(menuBar);

		tPanel = new thresholdPanel(mController,this);
		gPanel = new gaussPanel(mController,this);
		mPanel = new medianFilterPanel(mController, this);
		
		createUI();
		add(splitPane);
        setVisible(true);
	}

	private void createUI() {

		treeModel = new DefaultTreeModel(top);
		
		tree = new JTree(treeModel);
		tree.setRootVisible(false); // Sets everything invisible
		
	    imagePanel = new imagePanel(mc);
	    
		//listScrollPane = new JScrollPane(list);
	    listScrollPane = new listPanel(tree);
	        
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane , imagePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
			
	    splitPane.setPreferredSize(new Dimension(400, 200));
	    
	    
	}

	public static void updateUI(File[] files2, mainController mController) {
		
		DefaultMutableTreeNode top1 = new DefaultMutableTreeNode("new");
		treeModel.setRoot(top1);
		createNodes(top1, files2);
		
	    tree.setRootVisible(false); 
	    tree.setEditable(true); 
	    tree.setShowsRootHandles(true);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(mController);
		tree.expandPath(new TreePath(top1.getPath()));
		
	}

	private static void createNodes(DefaultMutableTreeNode top, File[] files2) {
		// TODO Auto-generated method stub
		
		int sizee = files2.length;

		String [] names = new String[sizee];
	    String [] study_exam = new String[sizee];
	    String [] series_des = new String[sizee];
	    String [] file_names = new String[sizee];
		
		for (int ii=0; ii<files2.length;ii++) {
			
			System.out.print("Create nodes Opened file " + files2[ii].getAbsolutePath() + "\n");
			
			try {
				dis = new DicomInputStream(files2[ii]);
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
			
			showDetails(dcm);
			
		
			if (dcm.getString( Tag.PatientName) == null) {
				names[ii] = "<unknown>#" + new Integer(ii).toString();
			}
			else
				names[ii] = dcm.getString( Tag.PatientName); 
		
			if (dcm.getString( Tag.StudyDescription) == null) {
				study_exam[ii] = "<study>#" + new Integer(ii).toString();
			}
			else
				study_exam[ii] = dcm.getString(Tag.StudyDescription);	
			
			if (dcm.getString( Tag.SeriesDescription) == null) {
				series_des[ii] = "<Series>#" + new Integer(ii).toString();
			}
			else
				series_des[ii] = dcm.getString(Tag.SeriesDescription);
			
			String[] aa = files2[ii].getName().split("\\.");
			file_names[ii] = aa[0];
		}
		
	    for (int iii1=0;iii1<sizee;iii1++) {
	    		    
		    	if (iii1 == 0) {
		    		name = new DefaultMutableTreeNode(names[iii1]);
			    	top.add(name);	
		    	}
		    	else if (!(names[iii1-1].equals(names[iii1]))) {
		    		name = new DefaultMutableTreeNode(names[iii1]);
			    	top.add(name);	
		    	}
	    	
		    	if (iii1 == 0) {
			    	study = new DefaultMutableTreeNode(study_exam[iii1]);
				    name.add(study);
		    	}
		    	else if (!(study_exam[iii1-1].equals(study_exam[iii1]))) {
			    	study = new DefaultMutableTreeNode(study_exam[iii1]);
				    name.add(study);
		    	}
	    	
		    	if (iii1 == 0) {
				    series = new DefaultMutableTreeNode(series_des[iii1]);
				    study.add(series);
		    	}
		    	else if (!(series_des[iii1-1].equals(series_des[iii1]))) {
				    series = new DefaultMutableTreeNode(series_des[iii1]);
				    study.add(series);
		    	}
		    	
		    	
		    	if (iii1 == 0) {
					img = new DefaultMutableTreeNode(file_names[iii1]);
					series.add(img);
		    	}
		    	else if (!(img.equals(file_names[iii1]))) {
					img = new DefaultMutableTreeNode(file_names[iii1]);
					series.add(img);
		    	}

	    }
	
	    for (int j=0;j<sizee;j++) {

	    }

	}
	private static void showDetails(DicomObject dcm) {
		// TODO Auto-generated method stub
		
		System.out.println("Show Details\n");
		
		String name = dcm.getString( Tag.PatientName);
		System.out.println("Imie i Nazwisko: " + name);
		
		Date date_birth = dcm.getDate(Tag.PatientBirthDate);
		System.out.println("Urodzony: " + date_birth);
		
		String study_exam = dcm.getString(Tag.StudyDescription);			
		System.out.println("Badanie: " + study_exam);
        
		String series_des = dcm.getString(Tag.SeriesDescription);
		System.out.println("Seria: " + series_des);
		
		Date date_des = dcm.getDate(Tag.StudyDate);
		System.out.println("Data badania: " + date_des);
		
		String studyID = dcm.getString(Tag.StudyID);
		System.out.println("ID badania: " + studyID);
		
		String seriesNo = dcm.getString(Tag.SeriesNumber);
		System.out.println("Nr Serii: " + seriesNo);
		
		String instNo = dcm.getString(Tag.InstanceNumber);
		System.out.println("Nr instancji: " + instNo);
						
		String study_type = dcm.getString(Tag.Modality);
		System.out.println("Rodzaj Badanie: " + study_type);
		
		String studyInstance = dcm.getString(Tag.StudyInstanceUID);
		System.out.println("studyInstance: " + studyInstance);
		
		String seriseInstance = dcm.getString(Tag.SeriesInstanceUID);
		System.out.println("seriseInstance: " + seriseInstance);
		
		String sopInstanceUID = dcm.getString(Tag.SOPInstanceUID);
		System.out.println("sopInstanceUID: " + sopInstanceUID + "\n\n");
				
		int samples = dcm.getInt( Tag.SamplesPerPixel);
		System.out.println("-----SamplesPerPixel: " + samples);
		
		int pixelRepresentation = dcm.getInt(Tag.PixelRepresentation);
		System.out.println("-----PixelRepresentation: " + pixelRepresentation);
		
		String photometricInterpretation = dcm.getString( Tag.PhotometricInterpretation);
		System.out.println("-----PhotometricInterpretation: " + photometricInterpretation);
		
		int	columns = dcm.getInt( Tag.Columns);
		System.out.println("-----Columns: " + columns);
		
		int	rows = dcm.getInt( Tag.Rows);
		System.out.println("-----Rows: " + rows);
		
	//	byte [] pixelData = dcm.getBytes(Tag.PixelData);
	//	System.out.println("-----PixelData: " + pixelData.length);
		
		
	}

}
