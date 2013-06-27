package app.model;

import java.awt.Container;
import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import app.controller.mainController;
import app.view.mainView;

@SuppressWarnings("serial")
public class gaussPanel extends JDialog{
	
    static private String []  kernelSizeString = {"3x3", "5x5", "9x9", "15x15"};
    static private String label = "Wygladzanie Gaussowskie";
 
    private String btnStringOk = "OK";
    private String btnStringCancel = "Anuluj";
    
    private JButton btnOk;
    private JButton btnCancel;
    private Container pane;
    private JRadioButton rBtn1;
    private JRadioButton rBtn2;
    private JRadioButton rBtn3;
    private JRadioButton rBtn4;
    private ButtonGroup btnGroup;
    
    public JFormattedTextField sigmaField;
    private NumberFormat sigmaFormat;
    private float sigma;
    private  JLabel sigmaLabel;
	
	public gaussPanel(mainController mController, mainView frame) {
		super(frame, true);
		setTitle(gaussPanel.label);
		setSize(300,180);
		
		pane = this.getContentPane();
		setLayout(null);
		
		btnOk = new JButton(btnStringOk);
		btnCancel = new JButton(btnStringCancel);
		
		sigmaField = new JFormattedTextField(sigmaFormat);
		sigmaField.setValue(new Float(sigma));
		sigmaField.setColumns(10);
		
		sigmaLabel = new JLabel("Sigma");
		sigmaLabel.setLabelFor(sigmaField);
		
				
		btnOk.addActionListener(mController);
        btnCancel.addActionListener(mController);
		
        btnOk.setVisible(true);
        btnCancel.setVisible(true);
        
        Dimension n = pane.getSize();
        System.out.println("wyielkosc:" + n.getWidth() + "x" + n.getHeight());
        
        rBtn1=new JRadioButton(getOpnion(0));
        rBtn2=new JRadioButton(getOpnion(1));
        rBtn3=new JRadioButton(getOpnion(2));
        rBtn4=new JRadioButton(getOpnion(3));
        btnGroup = new ButtonGroup();
        btnGroup.add(rBtn1);
        btnGroup.add(rBtn2);
        btnGroup.add(rBtn3);
        btnGroup.add(rBtn4);
        
        rBtn1.setBounds(14, 25, 50, 15);
        rBtn2.setBounds(80, 25, 50, 15);
        rBtn3.setBounds(147, 25, 50, 15);
        rBtn4.setBounds(213, 25, 60, 15);
       
        sigmaField.setBounds(25, 75, 250, 50);
        btnOk.setBounds(25,150,100,25);
        btnCancel.setBounds(155,150,100,25);
        
        pane.add(sigmaField);
        pane.add(rBtn1);
        pane.add(rBtn2);
        pane.add(rBtn3);
        pane.add(rBtn4);
		pane.add(btnOk);
		pane.add(btnCancel);

		btnGroup.setSelected(rBtn1.getModel(), true);
		System.out.println(btnGroup.toString());
	}
	
	public String getOpnion(int value) {
		return gaussPanel.kernelSizeString[value];
	}
	
	public int getValue() {
		if (this.rBtn1.isSelected() == true)
			return 3;
		if (this.rBtn2.isSelected() == true)
			return 5;
		if (this.rBtn3.isSelected() == true)
			return 9;
		if (this.rBtn4.isSelected() == true)
			return 15;
		return -1;
		
	}

	public Container getPane() {
		return this.pane;
	}
	
	public float getSigma() {
		return this.sigma;
	}

}
