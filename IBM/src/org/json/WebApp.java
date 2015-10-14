/* 
 * WebApp.java
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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* *
 * This Application accepts names of cities or zip-codes and provides 
 * weather related information such as the current condition, temperature etc.
 * 
 * Author		Ameya Murar
 * 
 */

public class WebApp extends JFrame {
	private JTextField textField;									// A text field to hold the name of the city or the zip code
	private JTextArea textArea;									// A text area to display the information queried from the API
	private JLabel label;										// A label which indicates what is required to be inputted into the text area
	private JButton button1;									// A button to indicate that the weather required should be in Celcius unit
	private JButton button2;									// A button to indicate that the weather required should be in Fahrenheit unit
	private JButton button3;									// A button to indicate that the weather required should be in Kelvin unit	
	private JLabel map;										// A label which holds the map of the required city or zip code
	static int counter=0;										// A counter to indicate how many times has the API been queried
	
	/**
	 * The Constructor
	 *
	 * It is used to give title to the frame and set other paramaters such as the layout of all the components
	 */
	
	public WebApp(){
		super("Weatherpedia!");									// The name of the Application
		setLayout(new FlowLayout());								// Using the FlowLayout style
		
		textField=new JTextField(20);								// Declaring the text field
		textArea=new JTextArea(5,60);								// Declaring the text area
		label=new JLabel("City/Zip Code: ");							// Declaring the label
		button1=new JButton("Celcius");								// Declaring the Celcius button
		button2=new JButton("Fahrenheit");							// Declaring the Fahrenheit button
		button3=new JButton("Kelvin");								// Declaring the Kelvin button
		add(label);													// Adding the label to the Application
		add(textField);										// Adding the text field to the Application
		add(button1);										// Adding the Celcius button to the Application
		add(button2);										// Adding the Fahrenheit button to the Application
		add(button3);										// Adding the Kelvin button to the Application
		add(textArea);										// Adding the text area to the Application
		
		
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String input=textField.getText();					// String input stores the value in the text field
				String degreeOfMeasurement = "Celcius";
				try {
					getData(input, degreeOfMeasurement);				// method call to the getData() method
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String input=textField.getText();					// String input stores the value in the text field
				String degreeOfMeasurement = "Fahrenheit";
				try {
					getData(input, degreeOfMeasurement);				// method call to the getData() method
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		button3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String input=textField.getText();					// String input stores the value in the text field
				String degreeOfMeasurement = "Kelvin";
				try {
					getData(input, degreeOfMeasurement);				// method call to the getData() method
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * The getData method
	 *
	 * Querying the API and displaying all the information in the form of JSON Object
	 */
	
	public void getData(String input, String degreeOfMeasurement) throws JSONException, IOException{
		if(counter != 0){
			remove(map);									// Reset the map to that of the new city or zip code
		}
    	if(input.contains(" ")){
    		input = input.replaceAll(" ", "_");							// Checking for cities with two letters, for example, New York
    	}
    	degreeOfMeasurement=degreeOfMeasurement.toLowerCase();						// converting the temperature measurement unit to lower case
    	boolean flag=isNumber(input);									// Calling the isNumber() method
    	URL url = null;
    	
    	/*
    	 * Checking whether the input is a name of a city or a zip code
    	 * and querying the appropriate URL
    	 * 
    	 */
    	
    	try{
    		if(flag){
    			url = new URL("http://api.openweathermap.org/data/2.5/weather?zip="+input+"&appid=bd82977b86bf27fb59a04b61b657fb6f");
    		}else{
    			url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+input+"&appid=bd82977b86bf27fb59a04b61b657fb6f");
    		}
    	}
    	catch(MalformedURLException e){
    		textArea.setText("Invalid input");
    		e.printStackTrace();
    	}
    	
    	/*
    	 * Parsing the contents of the API and storing the information in the JSONObject jsonObj
    	 */
    	
        BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));
        String inputLine;
        JSONObject jsonObj = null;
		while ((inputLine = in.readLine()) != null){
			jsonObj=new JSONObject(inputLine);
		}
		in.close();
		
		/*
		 * Extracting the information about the current condition at a particular location
		 */
		
		Object weather=jsonObj.get("weather");
		String s=weather.toString();
		int ind=s.indexOf("main");
		String s1=s.substring(ind+7);
		int ind1=s1.indexOf("\"");
		
		/*
		 * Extracting the information about the latitude and longitude at a particular location
		 */
		
		Object coordinates=jsonObj.get("coord");
		String t=coordinates.toString();
		int ind2=t.indexOf("lon");
		int ind3=t.indexOf("lat");
		
		/*
		 * Extracting the information about the current, minimum and maximum temperatures at a particular location
		 */
		
		Object main=jsonObj.get("main");
		String u=main.toString();
		int ind4=u.indexOf("temp");
		int ind5=u.indexOf("temp_min");
		int ind6=u.indexOf("temp_max");
		int ind7=u.indexOf(",", u.indexOf(",") + 1);
		
		Float currTemp = 0.0f;
		Float minTemp = 0.0f;
		Float maxTemp = 0.0f;
		
		/*
		 * The default temperature in the API is in Kelvin, here we convert to either celcius or fahrenheit as per request
		 */
		
		if(degreeOfMeasurement.equals("celcius")){
			currTemp = (float) (Float.parseFloat(u.substring(ind4+6, ind5-2)) - 273.15);
			minTemp = (float) (Float.parseFloat(u.substring(ind5+10, ind7-1)) - 273.15);
			maxTemp = (float) (Float.parseFloat(u.substring(ind6+10, u.length()-1)) - 273.15);
		}else if(degreeOfMeasurement.equals("fahrenheit")){
			currTemp = (float) (Float.parseFloat(u.substring(ind4+6, ind5-2)) * (9.0/5.0) - 459.67);
			minTemp = (float) (Float.parseFloat(u.substring(ind5+10, ind7-1)) * (9.0/5.0) - 459.67);
			maxTemp = (float) (Float.parseFloat(u.substring(ind6+10, u.length()-1)) * (9.0/5.0) - 459.67);
		}else{
			currTemp = (float) Float.parseFloat(u.substring(ind4+6, ind5-2));
			minTemp = (float) Float.parseFloat(u.substring(ind5+10, ind7-1));
			maxTemp = (float) Float.parseFloat(u.substring(ind6+10, u.length()-1));
		}
		
		String currentTemperature = currTemp.toString();
		String minimumTemperature = minTemp.toString();
		String maximumTemperature = maxTemp.toString();
       
        JSONObject json=new JSONObject();								// Creating a new JSON Object
        json.put("current_condition", s1.substring(0, ind1));						// Storing the current condition 
        json.put("lon", t.substring(ind2+5, ind3-2));							// Storing the longitude
        json.put("lat", t.substring(ind3+5, t.length()-1));						// Storing the latitude
        json.put("temp", currentTemperature.substring(0, 5));						// Storing the current temperature
        json.put("min_temp", minimumTemperature.substring(0, 5));					// Storing the minimum temperature
        json.put("max_temp", maximumTemperature.substring(0, 5));					// Storing the maximum temperature
        String output=json.toString();									// Converting from json object to string
        textArea.setText(output);									// Printing the output in the text area
        
        /*
         * Printing the map of the region
         */
        
        String imageOfMap="map.jpg";
        URL urlMap=new URL("http://maps.google.com/maps/api/staticmap?center="+input+"&zoom=14&size=512x512&maptype=roadmap");
        InputStream is=urlMap.openStream();
        OutputStream os=new FileOutputStream(imageOfMap);
        
        byte[] b=new byte[2048];									// Declaring a bytes array
        int length;
        
        while((length=is.read(b)) != -1){
        	os.write(b, 0, length);									// Writing to the outputstream
        }
        
        is.close();											// Closing the inputstream
        os.close();											// Closing the outputstream
        
        map=new JLabel(new ImageIcon((new ImageIcon("map.jpg")).getImage().getScaledInstance(450, 300,
                java.awt.Image.SCALE_SMOOTH)));
        add(map);											// Adding the map of the region to the Application
        
        counter++;											// Keeping track of the number of times the API has been queried
    }
	
	/**
	 * The isNumber() method
	 *
	 * Checking whether the input string is a name of a city or zip code
	 */
	
	public boolean isNumber(String input){
    	try{
    		int temp=Integer.parseInt(input);
    	}catch(NumberFormatException e){
    		return false;
    	}
    	return true;
    }
}
