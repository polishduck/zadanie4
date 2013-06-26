package app.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.controller.mainController;
import app.view.mainView;

public class gaussPanel extends JDialog{
	
    static private String []  kernelSizeString = {"3x3", "5x5", "9x9", "15x15"};
    static private String label = "Wygladzenie Gaussowskie";
 
    private String btnStringOk = "OK";
    private String btnStringCancel = "Anuluj";
    
    private JButton btnOk;
    private JButton btnCancel;
	
	public gaussPanel(mainController mController, mainView frame) {
		super(frame, true);
		setTitle(this.label);
		setSize(400,300);
	
		btnOk = new JButton(btnStringOk);
		btnCancel = new JButton(btnStringCancel);
				
		btnOk.addActionListener(mController);
        btnCancel.addActionListener(mController);
		
		add(btnOk);
	//	add(btnCancel);

	}
	
	public String getOpnion ( String [] a, int value) {
		return a[value];
	}
	

}
