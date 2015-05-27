package org.spg.Service.Based.System.auxiliary;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Utility {
	
	private static String fileName = "config.properties";
	private static Properties properties;
	
	private static void loadPropertiesInstance(){
		try {
			if (properties == null){
				properties = new Properties();
				properties.load(new FileInputStream(fileName));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty (String key){
		loadPropertiesInstance();
		return properties.getProperty(key);
	}
	
	public static String getProperty (String key, String defaultValue){
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return (output != null ? output : defaultValue);
	}
	
	public static void exportToFile(List<String[]> outputList, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			for (String[] strArray : outputList){	
				writer.append(Arrays.toString(strArray) +"\n");
			}
				writer.flush();
				writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
