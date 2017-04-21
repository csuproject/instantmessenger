package TeamOrange.instantmessenger;

import java.util.Scanner;

public class CLI {
	private App app;
	private Client client;
	private Scanner in;
	private boolean running;

	private final String login = "login";
	private final String logout = "logout";
	private final String createAccount = "create account";
	private final String exit = "exit";

	private boolean loggedIn = false;

	public CLI(App app){
		this.app = app;
		this.client = app.getClient();
		in = new Scanner(System.in);
	}

	public void run(){
		running = true;
		String input;
		while(running){
			availableOptionsMessage();
			input = in.nextLine();
			interpretInput(input);
		}
	}

	public void availableOptionsMessage(){
		System.out.print("\t( ");
		if(loggedIn){
			System.out.print("Logout, ");
		}
		else{
			System.out.print("Login, CreateAccount, ");
		}
		System.out.print("Exit )\n-> ");
	}

	public void interpretInput(String input){
		input = input.toLowerCase();
		switch(input){
			case login:
			{
				login();
			} break;
			case logout:
			{
				logout();
			} break;
			case createAccount:
			{
				createAccount();
			} break;
			case exit:
			{
				exit();
			} break;
		}
	}

	public void login(){
		System.out.println("\tLOGIN");
		System.out.print("Username: ");
		String userName = in.nextLine();
		System.out.print("Password: " );
		String password = in.nextLine();
		boolean logInSuccessfull = client.login(userName, password);
		if(logInSuccessfull){
			loggedIn = true;
			System.out.println("Logged in as " + userName);
		}
		else
			System.out.println("Failed to login as User (" + userName + ")");
	}

	public void logout(){
		boolean logoutSuccessfull = client.logout();
		if(logoutSuccessfull){
			loggedIn = false;
			System.out.println("Logged out");
		}
		else
			System.out.println("Failed to logout");
	}

	public void createAccount(){
		System.out.println("\tCREATE ACCOUNT");
		System.out.print("Username: ");
		String userName = in.nextLine();
		System.out.print("Password: " );
		String password = in.nextLine();
		boolean userCreated = client.createUser(userName, password);
		if(userCreated)
			System.out.println("User (" + userName + ") was successfully created");
		else
			System.out.println("Failed to create User (" + userName + ")");
	}

	public void exit(){
		boolean closed = client.close();
		if(closed)
			System.out.println("connection closed");
		running = false;
	}

}
