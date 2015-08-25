package main;

import java.io.IOException;

import managingSystem.uuv.ManagingSystemUUV;

public class ENTRUST {

	public static void main(String[] args) {
		try {
			ManagingSystemUUV managingSystemUUV = new ManagingSystemUUV();
			managingSystemUUV.startListening();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
