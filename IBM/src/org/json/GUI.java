/* 
 * GUI.java
 * 
 * Version: 
 * 1    
 * 
 * Date: 10/13/2015
 *  
 * Revisions: 
 *      
 */

package org.json;

import javax.swing.JFrame;

/* *
 * A generic class which defines the outline of the Application
 * 
 * Author		Ameya Murar
 * 
 */

public class GUI {
	
	/**
	 * The main method
	 *
	 * param     args    command line arguments 
	 */
	
	public static void main(String[] args) throws Exception {
    	WebApp app=new WebApp();
    	app.setSize(700,500);								// set the size of the frame to 700*500
    	app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				// when the 'X' is clicked, it will close the frame
    	app.setVisible(true);
    }
}
