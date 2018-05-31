package AstroTimer;

import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ConsoleDriver {
	//Scanner stdin;
	
	public static void main (String[] args){
		TimerManager test = new TimerManager();
		
		JFrame dummy = new JFrame("hi");
		//img needs to be in root.
	    JLabel holder = new JLabel(new ImageIcon("Untitled.png"));
	    JPanel h = new JPanel();
	    h.add(holder);
	    h.setVisible(true);
	    dummy.add(h);
	    
	    dummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    dummy.setPreferredSize(new Dimension(300,300));
	    dummy.pack();
	    dummy.setVisible(true);

		
		//test.addTimer(0,0,0, 2, 30);//1min 30 sec
		//test.addTimer(1,2,0,0,30);

		
		Scanner stdin = new Scanner(System.in);
		String input;
		String[] numString;
		int systemNum, planetNum, inMins, inSec = 0;
		int choice = 0;
		int num =0;
		while(choice != 3){
			System.out.println("1. New\n2. Get\n3. Exit");
			choice = Integer.parseInt(stdin.nextLine());
			
			if(choice ==1){
				System.out.println("Sys Plan mins sec");
				input = stdin.nextLine();
				numString = input.split(" ");
				try{
					systemNum = Integer.parseInt(numString[0]);
					planetNum = Integer.parseInt(numString[1]);
					inMins = Integer.parseInt(numString[2]);
					inSec = Integer.parseInt(numString[3]);

					test.addTimer(systemNum, planetNum, 0, inMins, inSec, 300);

					System.out.println("Timer added");
				}catch(Exception e){
					System.err.println("exception caught");
				}
			}//end if 1
			else if(choice == 2){
				System.out.println("Sys and Planet num? 0-");
				input = stdin.nextLine().trim();
				numString = input.split(" ");
				systemNum = Integer.parseInt(numString[0]);
				planetNum = Integer.parseInt(numString[1]);

				System.out.println(test.getTimeLeftList(systemNum, planetNum));

				
			}
			else if(choice == 3){ }
			else{
				System.out.println("invalid choice");
			}
			
			
		}//end while
		

		stdin.close();
	}//end main
	

	

}//end class
