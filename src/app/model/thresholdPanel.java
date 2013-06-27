package app.model;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import app.controller.mainController;
import app.view.mainView;

public class thresholdPanel extends JDialog{
	
    static private String []  kernelSizeString = {"3x3", "5x5", "9x9", "15x15"};
    static private String label = "Progowanie";
 
    private String btnStringOk = "OK";
    private String btnStringCancel = "Anuluj";
    
    private JButton btnOk;
    private JButton btnCancel;
    private Container pane;
	private JSlider thresholdValue;
	private int max_value=1;

	
	public thresholdPanel(mainController mController, mainView frame) {
		super(frame, true);
		setTitle(this.label);
		setSize(300,150);
		
		pane = this.getContentPane();
		setLayout(null);
		
		thresholdValue = new JSlider(JSlider.HORIZONTAL, 0, max_value-1, max_value/2);
		JLabel sliderLabel = new JLabel("Poziom Progu Odciecia", JLabel.CENTER);
		thresholdValue .setMajorTickSpacing(100);
		thresholdValue .setMinorTickSpacing(10);
		thresholdValue .setPaintTicks(true);
		thresholdValue .setPaintLabels(true);
		thresholdValue.add(sliderLabel);
		
		btnOk = new JButton(btnStringOk);
		btnCancel = new JButton(btnStringCancel);
				
		btnOk.addActionListener(mController);
        btnCancel.addActionListener(mController);
        thresholdValue.setVisible(true);
        btnOk.setVisible(true);
        btnCancel.setVisible(true);
        
        Dimension n = pane.getSize();
        
        thresholdValue.setBounds(25, 25, 200, 50);
        btnOk.setBounds(25,75,100,25);
        btnCancel.setBounds(155,75,100,25);
        
        pane.add(thresholdValue);
		pane.add(btnOk);
		pane.add(btnCancel);


	}
	
	public Container getPane() {
		return this.pane;
	}
	
	public void setMax(int max) {
		this.thresholdValue.setMaximum(max);
	}
	
	
	public String getOpnion(int value) {
		return this.kernelSizeString[value];
	}
	
	public int getValue() {
			return this.thresholdValue.getValue();
		
	}

}
