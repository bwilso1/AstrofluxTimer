package AstroTimer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI{
	
	private JPanel northPan;
	private JPanel centerPan;
	private JPanel southPan;
	private JPanel eastPan;
	
	private JButton[] systemButtons;
	private JButton[][] planetButtons;
	
	//private JButton save;
	//private JButton load;
	private JButton exit;
	private JButton about;
	
	private JButton defaultTimeButton;
	private JButton OverWriteTimer;
	private JButton defaultWarningLimitButton;
	
	private JLabel overWriteStatus;
	

	private String defaultTimeString;
	private int defaultWarningLimit;
	
	private boolean printDebug;
	private boolean overWriteMode;
	
	private TimerManager mgr;

	
	
	GUI(){
		JFrame window = new JFrame("Astroflux Timers");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mgr = new TimerManager();
		printDebug = true;
		defaultTimeString = "00:45:00";
		defaultWarningLimit = 300;
		overWriteMode = false; 
		
		buildNorthPan();
		buildCenterPan();
		buildEastPan();
		buildSouthPan();
		addActionListeners();
		
		window.setLayout(new BorderLayout());
		window.add(centerPan, BorderLayout.CENTER);
		window.add(southPan, BorderLayout.SOUTH);
		window.pack();
		window.setVisible(true);
	}

	private void buildNorthPan() {
		// TODO Auto-generated method stub
		
	}

	private void buildCenterPan() {
		centerPan = new JPanel();
		int HGAP =3;
		int VGAP = 3;
		
		String[] sysList = mgr.getSystemList();
		
		
		Color myRed = new Color(215,15,15);
		Color myGreen = new Color(34,189,40);
		Color myBlue = new Color(30,20,200);
		JPanel Antor = new JPanel();
		JPanel Gellan = new JPanel();
		JPanel Fulzar = new JPanel();
		
		//GridLayout(Rows, Cols, HGAP, VGAP)
		Antor.setBackground(myBlue);
		Antor.setLayout(new GridLayout(8,0,HGAP,VGAP));
		Gellan.setBackground(myGreen);
		Gellan.setLayout(new GridLayout(8,0,HGAP,VGAP));
		Fulzar.setBackground(myRed);
		Fulzar.setLayout((new GridLayout(8,0,HGAP,VGAP)));
		
		systemButtons = new JButton[sysList.length];
		for(int i = 0; i<sysList.length; i++){
			//systemButtons[i] = new JButton(String.format("%13s", sysList[i]));
			systemButtons[i] = new JButton(sysList[i]);
			systemButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			
			if(i<5)
				Antor.add(systemButtons[i]);
			else if(i>=5 && i<13)
				Gellan.add(systemButtons[i]);
			else
				Fulzar.add(systemButtons[i]);
		}
		//GridLayout(Rows, Cols, HGAP, VGAP)
		centerPan.setLayout(new GridLayout(0,3,3,3));
		centerPan.add(Antor);
		centerPan.add(Gellan);
		centerPan.add(Fulzar);
		
	}//end build center

	private void buildEastPan() {
		eastPan = new JPanel();
		defaultTimeButton = null;
		OverWriteTimer = new JButton("Overwrite Timer");
		defaultWarningLimitButton = new JButton("Set Warning Time");
		overWriteStatus = new JLabel();
		
	}

	private void buildSouthPan() {
		southPan = new JPanel();
		southPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		//save = new JButton("Save");
		//load = new JButton("Load");
		exit = new JButton("Exit");
		about = new JButton("About");
		
		//southPan.add(save);
		//southPan.add(load);
		southPan.add(about);
		southPan.add(exit);
	}

	private void addActionListeners() {
		PlanetButtonListen test = new PlanetButtonListen();
		for(int i = 0; i<systemButtons.length; i++){
			systemButtons[i].addActionListener(test);
		}
		
		// Need to add southPanel "controlls"
		about.addActionListener(new SouthPanListener());
		exit.addActionListener(new SouthPanListener());
		
	}

	public static void main(String[] args){
		GUI brian = new GUI();
	}
	
	private class SouthPanListener implements ActionListener{
		SouthPanListener(){
			
		}
		
		public void actionPerformed(ActionEvent event){
			if(event.getSource()== exit){
				System.exit(0);
			}
			else if(event.getSource()== about){
				String message = "Astroflux Timer\n";
				message = message + "by Brian Wilson\n\n" +
				"this program was built to keep track of planet\n" + 
				"capture times in the video game Astroflux\n" + 
				"\nClick on a sector to bring up individual planets\n" +
				"then click on a planet.  If a timer is already set, \n" +
				"the time left is displayed, if not, you are prompted for\n" +
				"the time you want to set a timer for.  If timer is set for\n"+
				"more than 5 mins, then an additional 5 min warning timer is \n" +
				"set automatically.  Timers sound off with a Pop up\n";
				JOptionPane.showMessageDialog(null,message);
			}
		}
		
	}
	
	private class PlanetButtonListen implements ActionListener{
		private String[][] pList2d;
		private String[] sList;
		
		PlanetButtonListen(){
			pList2d = mgr.getPlanetList2D();
			sList = mgr.getSystemList();
		}
		
		public void actionPerformed(ActionEvent event){
			boolean hit = false;
			int planetNum = -1;
			for(int i = 0; hit== false && i<systemButtons.length; i++){
				if(event.getSource()==systemButtons[i]){
					hit = true;
					planetNum = JOptionPane.showOptionDialog(centerPan, "Select Planet", "In "+ sList[i] , JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, pList2d[i], null);
					
					if(planetNum!=-1){
						if(mgr.hasTimer(i, planetNum)){
							JOptionPane.showMessageDialog(centerPan, "Time left at "+ pList2d[i][planetNum]+"\n"+mgr.getTimeLeftList(i, planetNum));
						}
						else {
							int[] temp = getUserStartTime();
							if(temp==null){
								//user aborted, skipping
							}
							else{
								mgr.addTimer(i, planetNum, temp[0], temp[1], temp[2], defaultWarningLimit);
							}
						}//end else ifNoTimer
						
					}//end if planet NUm != -1
					
				}//end IF eventSourc == button
				
			}//end Sys List traversal
			overWriteMode = false;
			overWriteCheck();
		}//end action performed	
		
	}//end planet button listnere
	
	private void overWriteCheck(){
		if(overWriteMode == true){
			overWriteStatus.setBackground(Color.red);
			overWriteStatus.setText("OVERWRITE");
		}
		else{
			overWriteStatus.setBackground(centerPan.getBackground());
			overWriteStatus.setText("         ");
		}
	}
	
	private int[] getUserStartTime(){

		String input;
		String[] inputArray;
		int[] returnArray = null;
		boolean flagDone = false;
		
		while(flagDone==false){
			input = JOptionPane.showInputDialog(centerPan, "Input time HH:MM:SS", defaultTimeString);
			
			if(input == null || input.isEmpty() || input.trim().isEmpty()){
				if(printDebug)System.out.println("input was null/empty/whitespace");
				//user aborted, skip
				flagDone = true;
				returnArray = null;
			}
			else{

				try{
					input = input.trim();
					inputArray = input.split("[ :;,.'-/*+]");
					returnArray = new int[3];
					if(inputArray == null || inputArray.length==0){
						flagDone = true;
						JOptionPane.showMessageDialog(centerPan, "No timer added");
					}
					else if(inputArray.length>3){
						throw new InvalidFormException();
					}
					else if(inputArray.length==3){
						returnArray[0] = Integer.parseInt(inputArray[0]);//hours
						returnArray[1] = Integer.parseInt(inputArray[1]);//mins
						returnArray[2] = Integer.parseInt(inputArray[2]);//sec
						flagDone = true;
					}
					else if(inputArray.length==2){
						returnArray[0] = 0;//hrs
						returnArray[1] = Integer.parseInt(inputArray[0]);//min
						returnArray[2] = Integer.parseInt(inputArray[1]);//sec
						flagDone = true;
					}
					else{
						
						returnArray[0] = 0;
						returnArray[1] = Integer.parseInt(inputArray[0]);
						returnArray[2] = 0;
						JOptionPane.showMessageDialog(centerPan, "one num entered, handling as mins");
						flagDone = true;
					}
				}//end try
				catch(InvalidFormException| NumberFormatException e){
					JOptionPane.showMessageDialog(centerPan, "Invalid form, please use HH MM SS or \nif Hours=0  MM SS\nwhere H=Hours, M=Minutes, S=Seconds");
				}
				catch(NullPointerException e){
					JOptionPane.showMessageDialog(centerPan, "Null pointer while creating timer, contact Developer\nTimer not added, returning to SystemList");
					flagDone = true;
				}
			}//end else IF INPUT EMPTY
			
		}//end while flag
		
		return returnArray;
	}//end get start time


}
