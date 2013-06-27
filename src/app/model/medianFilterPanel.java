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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import app.controller.mainController;
import app.view.mainView;

public class medianFilterPanel extends JDialog{
	
    static private String []  kernelSizeString = {"3x3", "5x5", "9x9", "15x15"};
    static private String label = "Filtr Medianowy";
 
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
	
	public medianFilterPanel(mainController mController, mainView frame) {
		super(frame, true);
		setTitle(this.label);
		setSize(300,150);
		
		pane = this.getContentPane();
		setLayout(null);
		
		btnOk = new JButton(btnStringOk);
		btnCancel = new JButton(btnStringCancel);
				
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
       
        
        btnOk.setBounds(25,75,100,25);
        btnCancel.setBounds(155,75,100,25);
        
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
		return this.kernelSizeString[value];
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

}
