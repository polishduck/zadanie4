package app.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import app.view.*;
import app.model.*;

public class mainController implements ActionListener, TreeSelectionListener, MouseListener, MouseMotionListener {
	
	private mainView myView;
	public File[] files;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public JFileChooser fc = null;
	public patientData pData;
	public BufferedImage image = null;
	public Dimension d;
	public int width;
	public int height;
	public boolean drag = false;
	public int mouseX = 0;
	public int mouseY = 0;

	public mainController() throws IOException {
		myView = new mainView(this);
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new DicomFilter());
		fc.setAcceptAllFileFilterUsed(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) 
		{
			JButton btn = ((JButton)e.getSource());
			String label = btn.getText();
			
			if (btn.getParent() == myView.mPanel.getPane()) {
				if(label.equals("OK")) {
					System.out.print("pushed ok\n");
					System.out.print("wartosc: " + myView.mPanel.getValue() + "\n");
					
					MedianFilter f = new MedianFilter(myView.mPanel.getValue());
					BufferedImage dstImage = null;
					dstImage = f.filter(image);
					
					BufferedImage newImage = mainView.imagePanel.scaleImage(dstImage, width, height, null);
					BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
					ImageIcon icon = new ImageIcon(newImage2);
					mainView.imagePanel.setIcon(icon);
					myView.mPanel.setVisible(false);
				}
				if(label.equals("Anuluj")) {
					System.out.print("pushed cancel\n");
					myView.mPanel.setVisible(false);
				}
				
			}
			
			
			if (btn.getParent() == myView.tPanel.getPane()) {
				if(label.equals("OK")) {
					System.out.print("pushed ok\n");
					System.out.print("wartosc: " + myView.tPanel.getValue() + "\n");
					int v = myView.tPanel.getValue();
					byte[] data = new byte[dcm.getInt( Tag.Columns)];
					for (int i = v; i<data.length;i++) {
						data[i] = (byte) 255;
					}

					BufferedImageOp op = new LookupOp(new ByteLookupTable(0, data),null);
					BufferedImage newImagexx = op.filter(image, null);

					
					BufferedImage newImage = mainView.imagePanel.scaleImage(newImagexx, width, height, null);
					BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
					ImageIcon icon = new ImageIcon(newImage2);
					mainView.imagePanel.setIcon(icon);
					myView.tPanel.setVisible(false);
				}
				if(label.equals("Anuluj")) {
					System.out.print("pushed cancel\n");
					myView.tPanel.setVisible(false);
				}
			}
			if (btn.getParent() == myView.gPanel.getPane()) {
				if(label.equals("OK")) {
					System.out.print("gauss pushed ok\n");
					System.out.print(2.0f);
					System.out.print("wartosc: " + myView.gPanel.getValue() + "\n");
					System.out.print("wartosc sigmny: " +myView.gPanel.sigmaField.getText());
					int aa = myView.gPanel.getValue();
					
					float val = new Float(myView.gPanel.sigmaField.getText());
					
					float [] kernel = new float[aa*aa];
					for (int ii=0; ii<kernel.length;ii++) {
						kernel[ii] = 1.0f / val;
					}
					BufferedImageOp op = new ConvolveOp(new Kernel(aa,aa,kernel),ConvolveOp.EDGE_NO_OP,null);
					BufferedImage newImagexx = op.filter(image, null);

					
					BufferedImage newImage = mainView.imagePanel.scaleImage(newImagexx, width, height, null);
					BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
					ImageIcon icon = new ImageIcon(newImage2);
					mainView.imagePanel.setIcon(icon);
									
					myView.gPanel.setVisible(false);
				}
				if(label.equals("Anuluj")) {
					System.out.print("pushed cancel\n");
					myView.gPanel.setVisible(false);
					
				}
			}
		}
		
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem menuItem = ((JMenuItem)e.getSource());
			String label = menuItem.getText();

			if(label.equals("Otworz")) {
				System.out.print("Open call");
				int returnVal = fc.showOpenDialog(myView);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					files = fc.getSelectedFiles();
				}
				
				for (int ii=0; ii<files.length;ii++) {
					
					System.out.print("Opened file " + files[ii].getAbsolutePath() + "\n");
					
					try {
						dis = new DicomInputStream(files[ii]);
						dcm = dis.readDicomObject();
					} catch (IOException e1) {
						// ex
					} finally {
						if (dis != null) {
							CloseUtils.safeClose(dis);
						}
						
					}

					Iterator<DicomElement> iter = dcm.datasetIterator();
					while ( iter.hasNext() ) {
						DicomElement tag = iter.next();
				// print dicom tag
						System.out.println( tag );
						}					
				}
				mainView.updateUI(files,this);
			}
			if(label.equals("Wygladzanie Gaussowskie")) {
				System.out.println("Wygladzanie Gaussowskie");
				if (mainView.imagePanel.hasImage == true) {
					System.out.println("Widac\n");
					myView.gPanel.setVisible(true);
				}
				else {
					System.out.println("Pusty obraz\n");
					JOptionPane.showMessageDialog(null,"Wczytaj obraz" ,"Brak Obrazu", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			if(label.equals("Filtr Medianowy")) {
				System.out.println("Filtr Medianowy");
				if (mainView.imagePanel.hasImage == true) {
					System.out.println("Widac\n");
					myView.mPanel.setVisible(true);
				}
				else {
					System.out.println("Pusty obraz\n");
					JOptionPane.showMessageDialog(null,"Wczytaj obraz" ,"Brak Obrazu", JOptionPane.WARNING_MESSAGE);
				}
			}
			if(label.equals("Progowanie")) {
				System.out.println("Progowanie");
				if (mainView.imagePanel.hasImage == true) {
					System.out.println("Widac\n");
					myView.tPanel.setMax(dcm.getInt( Tag.Columns));
					myView.tPanel.setVisible(true);
				}
				else {
					System.out.println("Pusty obraz\n");
					JOptionPane.showMessageDialog(null,"Wczytaj obraz" ,"Brak Obrazu", JOptionPane.WARNING_MESSAGE);
				}
			}
			if(label.equals("Wyczysc")) {
				System.out.println("Wyczysc");
				BufferedImage newImage = mainView.imagePanel.scaleImage(image, width, height, null);
				BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
				ImageIcon iconxx = new ImageIcon(newImage2);
				mainView.imagePanel.setIcon(iconxx);
			}
						
			if(label.equals("Zamknij")) {
				System.exit(0);
			}
			
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)mainView.tree.getLastSelectedPathComponent();
		if (node == null) return;
		if (node.isLeaf()) {
			//String a = clicked_index(mainView.tree, node);
	
			String aa = node.getUserObject().toString();
			String a = aa + ".dcm";
			int index = 0;	
			
			for (int i=0; i<files.length;i++) {
				if (files[i].getName().equals(a)) {
					index = i;
				}
			}
				
			//TreeNode parent = node.getParent();

			System.out.println("click na itemie nr:" +  index + " i nazwie" + a);
	
			System.out.print("Opened file " + files[index].getAbsolutePath() + "\n");
			
			try {
				dis = new DicomInputStream(new File(files[index].getAbsolutePath()));
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
			
			
			String name = dcm.getString( Tag.PatientName);
			Date birth = dcm.getDate(Tag.PatientBirthDate);
			Calendar date_birth = Calendar.getInstance();
			if (birth !=null) {
				date_birth.set(Calendar.DATE, birth.getDay());
				date_birth.set(Calendar.MONTH, birth.getMonth());
				date_birth.set(Calendar.YEAR, birth.getYear()+1900);
					
			}
			String study_type = dcm.getString(Tag.Modality);

			pData = new patientData(name, date_birth, study_type);
			
			FileImageInputStream fiis;
			
			
			try {
				fiis = new FileImageInputStream(new File(files[index].getAbsolutePath()));
				image = ImageIO.read(fiis);   //(fiis);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			mainView.imagePanel.hasImage = true;
			d = mainView.imagePanel.getSize();
			width =(int) d.getWidth();
			height =(int) d.getHeight();
			System.out.println("wymiar okna:" + width + "x" +height);
			BufferedImage newImage = mainView.imagePanel.scaleImage(image, width, height, null);
			BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
			ImageIcon icon = new ImageIcon(newImage2);
			mainView.imagePanel.setIcon(icon);

		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	    if (drag == true) {
	        mouseX = e.getX();
	        mouseY = e.getY();
	        
	    }
	    contrastChange(image,mouseX, mouseY);
	    System.out.println("drag:" + mouseX + "x" + mouseY + "\n");
	}
	
	private void contrastChange(BufferedImage image2, int mouseX2, int mouseY2) {
		BufferedImageOp op = new RescaleOp(mouseX2/100, mouseY2/100, null);
		BufferedImage dest = op.filter(image,null);
		BufferedImage newImage = mainView.imagePanel.scaleImage(dest, width, height, null);
		BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
		ImageIcon icon = new ImageIcon(newImage2);
		mainView.imagePanel.setIcon(icon);
		
	}

	public void mouseMoved(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {
		drag = true;
	}
	public void mouseReleased(MouseEvent e) {
		drag = false;
	}

}
