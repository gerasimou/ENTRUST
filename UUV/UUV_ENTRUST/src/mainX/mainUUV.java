package mainX;

import java.io.IOException;

import managingSystem.ManagingSystem;

public class mainUUV {

	public static void main(String[] args) {		
		try {
			ManagingSystem managingSystem = new ManagingSystem();
			managingSystem.startListening();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
