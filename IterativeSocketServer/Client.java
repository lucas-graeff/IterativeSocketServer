import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Client implements Runnable {

	// Global variable declarations
	static String text;
	static String IP;
	static int port;
	static int numOfClients;
	static int totalRuntime = 0;
	static String[] runtimes;
	static int recordedRuntimes = 0;
	int clientNum;
	Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		
		// User enters an IP Address 
		System.out.print("Enter an IP Address: ");
		IP = in.nextLine();

		// User enters a port number which is stored in port variable
		do {
			System.out.print("Enter a port # in range 1025 - 4998: ");
			port = in.nextInt();
			in.nextLine();
		}while(port < 1025 || port > 4998);
		
        mainFunction();

	}

	public static void mainFunction() {

		Scanner in = new Scanner(System.in);
		ArrayList<Thread> clients = new ArrayList<Thread>();

		// Menu
		System.out.println("Enter one of the following commands:");
                System.out.println("1 - Host Current Date/Time");
                System.out.println("2 - Uptime");
                System.out.println("3 - Host Memory Use");
                System.out.println("4 - Host Netstat");
                System.out.println("5 - Host Current Users");
                System.out.println("6 - Host Current Processes");
                System.out.println("7 - Exit");
			
		//Takes user input and closes program if user chose 7
		if((text = in.nextLine()).contains("7")){
			System.out.println("Exiting");
			System.exit(0);
		}

		System.out.print("Number of clients: ");
		numOfClients = in.nextInt();
		
		
		runtimes = new String[numOfClients];

		// Creates a thread object and adds it to the clients array
		// (one for each client)
		for (int i = 1; i <= numOfClients; i++) {
			clients.add(new Thread(new Client(i)));
		}

		// Runs the run function once for each client
		// (Threads are run simultaneously)
		for (int i = 0; i < numOfClients; i++) {
			clients.get(i).start();
		}
	}

	// Client object constructor
	public Client(int num) {
		clientNum = num;
	}


	public void run() {
		
		long runtime;
		StringBuilder sb = new StringBuilder();
		
		try {
			long start = System.currentTimeMillis();
			Socket socket = new Socket(IP, port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());

			writer.println(text);
			writer.flush();


			InputStreamReader inStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(inStreamReader);

			String line;
			
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			socket.close();

			long stop = System.currentTimeMillis();
			
			// Runtime in milliseconds
			runtime = stop - start;
			
			System.out.println("Client " + clientNum + ": " + sb);
			
			// Sums the total runtime of all clients
			totalRuntime += runtime;
			
			// Adds the runtime to the runtimes array
			runtimes[clientNum - 1] = "Client " + clientNum + " runtime: " + runtime + "ms";
			++recordedRuntimes;
			
			if (numOfClients == recordedRuntimes) {
				printRuntimes();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printRuntimes() {

		// displays all of the client runtimes
		for (int i = 0; i < numOfClients; i++) {					
			System.out.println(runtimes[i]);
		}
		
		System.out.println("Total runtime: " + totalRuntime + "ms");
		System.out.println("Average runtime: " + totalRuntime / numOfClients + "ms");

		//Resets values and reruns menu function
		recordedRuntimes = 0;
		totalRuntime = 0;
		mainFunction();
	}
}