import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.time.*;

public class Server {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int port;
		String text;
		String message = "";
		
		do {
			System.out.print("Enter a port # in range 1025 - 4998: ");
			port = in.nextInt();	
		}while(port < 1025 || port > 4998);

		try {

			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server is listening on port: " + port);

			while (true) {

				Socket socket = serverSocket.accept();
				InputStreamReader inStreamReader = new InputStreamReader(socket.getInputStream());

				BufferedReader reader = new BufferedReader(inStreamReader);

				// Print Writer object used for sending information to the client
				PrintWriter writer = new PrintWriter(socket.getOutputStream());

				text = reader.readLine();


				// Determines which command is being requested, gets the command output from the
				// getCommand function and stores it in message
				switch(text){
					case "1": 
					 	text = runCommand("date");
					 	break;
					case "2":
						text = runCommand("uptime");
						break;
					case "3":
						text = runCommand("free -h");
						break;
					case "4":
						text = runCommand("netstat");
						break;
					case "5":
						text = runCommand("w");
						break;
					case "6":
						text = runCommand("ps -aux");
						break;
					case "7":
						text = "Disconnecting";
						socket.close();
						break;
					default: 
						text = "Invalid input";
						break;
				}

				writer.println(text);
				writer.flush();

				socket.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private static String runCommand(String text) {

		StringBuilder sb = new StringBuilder();

		try {

			Process process = Runtime.getRuntime().exec(text);

			// Buffered reader which reads the Linux command
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			// String which will be used to temporarily store each line of the linux command
			// output
			String line;

			// While there is still a Linux command output line to be read, store it in line
			// and append that line to the message variable followed by a new line
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();

	}
}