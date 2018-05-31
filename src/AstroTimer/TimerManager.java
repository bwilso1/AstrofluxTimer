package AstroTimer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TimerManager{
	private BriansCountDownTimer[][] expiredList;
	private Timer[][] warningList;
	private BriansCountDownTimer[] customWarnList;
	
	private String[] systemList;
	private String[] planetList;
	private String[][] planetList2D;
	private boolean printDebug;
	private boolean suppressWarning;
	
	
	TimerManager(){
		printDebug = true;
		suppressWarning = true;
		
		systemList = new String[]{"Hyperion","Kapello","Venturi","Durian","Arrenius","Kritillian","Hozar","Zergilin","Mitrilion","Vibrilian","Sarkinon","Neurona","Cynapsian","Fulzar","Vorsran"};
		planetList = new String[]{"Erath","Endarion","Asteroid L34","Daren","Vargen","Dustla","Centurion","Rocky","Mari","Ventari","Herion","Illion","Kantala","Ronduria","Anvol","Melenda","Valden","Opitali","Valen","Graven"," Negundo","Rubrum","Carnea","Holophylla","Elata","Arborea","Mikus"," Eufelion","Axalon","Melifron","Kuji","Anquin","Zada","Tufir","Polueno","Glictan"," Alba","Sinesis","Cystena","Armoji","Orgasin","Kaludon","Roskur"," Ambrosia","Calufo","Barbulos","Mivilus","Withered Roots"," Aladivin","Fexucius","Amadun","Vlardenon","Gardenon","Darkenon","Argenon","Golgata III","Akandri"," Exretorum","Efedrit","Elondrit"," New Erath","Ymer","Lak","Ur","Anjio","Szyte","Wazyr","Alkzo","Mazim","?"};
		planetList2D = new String[systemList.length][];
		
		planetList2D[0] = new String[]{"Erath","Endarion","Daren","Vargen","Asteroid L34"};
		planetList2D[1] = new String[]{"Dustla","Centurion","Rocky","Mari"};
		planetList2D[2] = new String[]{"Ventari"};
		planetList2D[3] = new String[]{"Herion","Illion","Kantala","Ronduria"};
		planetList2D[4] = new String[]{"Anvol","Melenda","Valden","Opitali","Valen","Graven" };
		planetList2D[5] = new String[]{"Negundo","Rubrum","Carnea","Holophylla","Elata","Arborea","Mikus" };
		planetList2D[6] = new String[]{"Eufelion","Axalon","Melifron" };
		planetList2D[7] = new String[]{"Kuji","Anquin","Zada","Tufir","Polueno","Glictan" };
		planetList2D[8] = new String[]{"Alba","Sinesis","Cystena","Armoji","Orgasin","Kaludon","Roskur" };
		planetList2D[9] = new String[]{"Ambrosia","Calufo","Barbulos","Mivilus","Withered Roots" };
		planetList2D[10] = new String[]{"Aladivin","Fexucius","Amadun","Vlardenon","Gardenon","Darkenon","Argenon" };
		planetList2D[11] = new String[]{"Golgata III","Akandri" };
		planetList2D[12] = new String[]{"Exretorum","Efedrit","Elondrit" };
		planetList2D[13] = new String[]{"New Erath","Ymer","Lak","Ur","Anjio","Szyte","Wazyr","Alkzo","Mazim" };
		planetList2D[14] = new String[]{"?"};

		
		expiredList = new BriansCountDownTimer[systemList.length][];
		warningList = new Timer[systemList.length][];
		customWarnList = new BriansCountDownTimer[5];
		
		for(int i = 0; i<planetList2D.length; i++){
			expiredList[i] = new BriansCountDownTimer[planetList2D[i].length];
			warningList[i] = new Timer[planetList2D[i].length];
		}
		
		for(int i = 0; i<planetList2D.length; i++){
			for(int k = 0; k<planetList2D[i].length; k++){
				expiredList[i][k] = null;
				warningList[i][k] = null;
			}//end K
			
		}//end I
		
		
		
	}
	
	public void addTimer(int systemNum, int planetNum, int inHours, int inMins, int inSec, int defWarnInSeconds){
		int delaySec = (inHours*3600) + (inMins*60) + inSec;
		if(printDebug){
			if(delaySec<1)
				System.out.println("Delay less than 1 sec");
		}
		if(delaySec>0){
		
		expiredList[systemNum][planetNum] = new BriansCountDownTimer(delaySec*1000,new PlanetExpiredListener(systemNum,planetNum));
		expiredList[systemNum][planetNum].start();
		expiredList[systemNum][planetNum].setRepeats(false);
		if(printDebug) 
			System.out.println("new expired timer started "+systemList[systemNum]+" "+planetList2D[systemNum][planetNum]+ " "+ LocalTime.now());
		}
		if(delaySec>defWarnInSeconds){
			delaySec = delaySec - defWarnInSeconds;
			
			warningList[systemNum][planetNum] = new Timer(delaySec*1000,new PlanetWarningListener(systemNum,planetNum));
			warningList[systemNum][planetNum].start();
			warningList[systemNum][planetNum].setRepeats(false);
			if(printDebug) System.out.println("new warning timer started "+systemList[systemNum]+" "+planetList2D[systemNum][planetNum]+" "+ LocalTime.now());
			//t2.getDelay();///code for looking up time left
			
			
		}
		else{
			
			//need to thread so it doesn't pause
			if(suppressWarning==false)
				popUp("less than 5 mins");
			if(printDebug) System.out.println("less than 5 mins");
			
		}
		
	}//end add
	
	public void addMiscTimer(String message, int counterNum, int inHours, int inMins, int inSec){
		int delay = inHours*3600 + inMins*60 + inSec;
		customWarnList[counterNum]= new BriansCountDownTimer(delay*1000, new CustomWarningListener(message,counterNum));
		if(printDebug) System.out.println("New custom timer " + counterNum + " started at  "+LocalTime.now());
		
	}
	
	public String[] getPlanetList(){
		return planetList;
	}
	
	public String[] getSystemList(){
		return systemList;
	}
	
	public String[][] getPlanetList2D(){
		return planetList2D;
	}
	
	public String[][] getAllTimers(){
		//ArrayList<ArrayList<String>> returnTemp = new ArrayList<ArrayList<String>>();
		String[][] temp = new String[expiredList.length][];
		
		for(int i = 0;i<temp.length;i++){
			temp[i] = new String[expiredList[i].length];
		}
		
		
		
		for(int i = 0; i<expiredList.length;i++){
			for(int k = 0; k<expiredList[i].length; k++){
				if(expiredList[i][k]!=null){
					temp[i][k] = new String(expiredList[i][k].getTimeLeft());
				}
				else
					temp[i][k] = null;
				
			}//end K planet traverse
			
		}//end I System traverse
		
		return temp;
	}
	
	public boolean hasTimer(int systemNum, int planetNum){
		boolean isActive = false;
		
		if(expiredList[systemNum][planetNum]!=null)
			isActive = true;
		
		return isActive;
	}
	
	public boolean hasTimer(int custWarnNum){
		boolean isActive = false;
		if(customWarnList[custWarnNum]!=null)
			isActive = true;
		
		return isActive;
		
	}
	
	public String getTimeLeftList(int systemNum, int planetNum){
		String returnString = null;
		if(expiredList[systemNum][planetNum]!=null ){
			returnString = expiredList[systemNum][planetNum].getTimeLeft();
			return returnString;
		}
		else
			return null;
		
	}
	
	public String getTimeLeftList(int custWarnNum){
		String returnString = null;
		if(customWarnList[custWarnNum]!= null){
			returnString = customWarnList[custWarnNum].getTimeLeft();
			return returnString;
			
		}
		else
			return returnString;
		
	}
	
	private void popUp(String inMessage){
		class PopUpClass implements Runnable{
			
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null,inMessage);
			}//end run
			
			
		}//end innner class
		PopUpClass s = new PopUpClass();
		Thread r = new Thread(s);
		r.start();
		
	}//end popUp

	private class PlanetExpiredListener implements ActionListener{

		private int expiredClassPlanetNum;
		private int expiredClassSystemNum;
		
		PlanetExpiredListener(int systemNum, int planetNum){
			super();
			expiredClassPlanetNum = planetNum;
			expiredClassSystemNum = systemNum;
		}
		
	    public void actionPerformed(ActionEvent evt) {
	    	
	       popUp(planetList2D[expiredClassSystemNum][expiredClassPlanetNum]+" in " + systemList[expiredClassSystemNum] + " expired");
	       expiredList[expiredClassSystemNum][expiredClassPlanetNum].stop();
	       expiredList[expiredClassSystemNum][expiredClassPlanetNum] = null;
	       if(printDebug) 
	    	   System.out.println("expired list " + expiredClassSystemNum + " " + expiredClassPlanetNum +" set to null"+" "+ LocalTime.now());
	    }
	}//end expired class
	
	private class PlanetWarningListener implements ActionListener{

		private int warnClassPlanetNum;
		private int warnClassSystemNum;
		
		PlanetWarningListener(int systemNum, int planetNum){
			warnClassPlanetNum = planetNum;
			warnClassSystemNum = systemNum;
			
		}
		
		public void actionPerformed(ActionEvent evt) {
			// popUp(planetList[planetNum] + " expires in " + mins +":"+sec);
			popUp(planetList2D[warnClassSystemNum][warnClassPlanetNum] +" in "+ systemList[warnClassSystemNum] + " expires in 5:00");
			warningList[warnClassSystemNum][warnClassPlanetNum].stop();
			warningList[warnClassSystemNum][warnClassPlanetNum] = null;
			if(printDebug) 
				System.out.println("warning list " + warnClassSystemNum +" " + warnClassPlanetNum +" set to null" +" "+ LocalTime.now());
		}
		
	}//end warning class
	
	private class CustomWarningListener implements ActionListener{
		private String message;
		private int warnNumber;
		CustomWarningListener(String inMessage, int num){
			super();
			message = inMessage;
			warnNumber = num;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			popUp(message);
			customWarnList[warnNumber] = null;
			if(printDebug) 
				System.out.println("custom warn " + warnNumber + " set to null");
			
		}
		
		
	}
	@SuppressWarnings("serial")
	private class BriansCountDownTimer extends Timer{
		int secondsLeft;
		LocalTime EndTime;

		
		BriansCountDownTimer(int delayMili, ActionListener listen){
			super(delayMili,listen);
			secondsLeft = delayMili/1000;
			EndTime = LocalTime.now().plusSeconds(secondsLeft);
		}
			
		
		public String getTimeLeft(){
			LocalTime result = EndTime.minusSeconds(LocalTime.now().toSecondOfDay());
			
			return String.format("%02d:%02d:%02d", result.getHour(), result.getMinute(), result.getSecond());
		}
		
		
	}//end B countdown timer
	
	
}


