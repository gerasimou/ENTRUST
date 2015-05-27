package org.spg.Service.Based.System.prism;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class PrismExecutor {
	
	PrintWriter out;

	private ServerSocket serverSocket;
	PrismAPI api = new PrismAPI();

	public static void main(String[] args) {
		PrismExecutor prismExecutor = null;
		try{
			prismExecutor = new PrismExecutor();
			prismExecutor.start(Integer.parseInt(args[0]));
		}
		catch (IOException e){
			System.out.println("Prism Executor exception");
		}
		finally{
			prismExecutor.out.close();
		}
	}

	
	public String analize(String model, String propertyFile) {
		api.setPropertiesFile(propertyFile);
		api.loadModel(model);
		List<Double> res = api.runPrism();
		StringBuilder finalRes = new StringBuilder();
		for (Double value : res) {
			finalRes.append(String.valueOf(value));
			finalRes.append("@");
		}
//		api.closeDown();
//		 api = null;
		 return finalRes.toString();
	}

	
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Accepting from port: "+port);
		
		Socket socket = serverSocket.accept();

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());

		while (true) {
			String[] input = this.readModel(in);
			String model = input[0];
			String propertyFile = input[1];
//			System.out.println("Model: "+model);
//			System.out.println("Property: "+propertyFile);
			String results = this.analize(model, propertyFile);
			System.out.println(results);
			this.writeResult(out, results);
		}
	}
	
	
	private void writeResult(PrintWriter out, String message){
//		System.out.println("Sending out: "+message);
		message = message.substring(0, message.length()-1)+"\nEND\n";
		out.print(message);
        out.flush();
//        out.close();
	}

	
	private String[] readModel(BufferedReader in) throws IOException {
		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
			line = in.readLine();
			if(line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

//		System.out.println("Received from client...");
		String res[] = modelBuilder.toString().split("@");
		res[1] =res[1].trim(); 
		return res;
	}
}
