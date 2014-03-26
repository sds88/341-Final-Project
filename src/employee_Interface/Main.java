package employee_Interface;

import java.io.IOException;

public class Main {
	public static MySQLConnection conn;
	public static State state = State.HOME;
	
	public enum State {
		HOME,
		ORDER,
		CUSTOMER,
		PRODUCT,
		INVENTORY,
		QUIT;
	}
	
	public static void main(String[] args) {
		if(args.length != 2)
		{
			System.out.println("Invalid number of arguments.  Proper format is \"Program [username] [password]\"");
			return;
		}
		conn = new MySQLConnection(args[0], args[1]);
		while(state != State.QUIT)
		{
			EnterState(state);
		}
	}
	
	public static void EnterState(State state)
	{
		switch(state) {
		case HOME:
			EnterHomeState();
			break;
		case ORDER:
			EnterOrderState();
			break;
		case CUSTOMER:
			EnterCustomerState();
			break;
		case PRODUCT:
			EnterProductState();
			break;
		case INVENTORY:
			EnterInventoryState();
			break;
		case QUIT:
			EnterQuitState();
			break;
		default:
			System.err.println("Unknown state reached.");
			break;
			
		}
	}
	
	private static void EnterHomeState()
	{
		System.out.println("Welcome.  Please select an option from the following list:");
		System.out.println("1.\tInput an order.");
		System.out.println("2.\tLookup a customer.");
		System.out.println("3.\tLookup a product.");
		System.out.println("4.\tCheck inventory.");
		System.out.println("5.\tQuit.");
		char selection = '0';
		try {
			selection = (char) System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Selection = '"+selection+"'");
		switch(selection) {
		case '1':
			state = State.ORDER;
			break;
		case '2':
			state = State.CUSTOMER;
			break;
		case '3':
			state = State.PRODUCT;
			break;
		case '4':
			state = State.INVENTORY;
			break;
		case '5':
			state = State.QUIT;
			break;
		default:
			state = State.HOME;
		}
	}
	
	private static void EnterOrderState()
	{
		
	}
	
	private static void EnterCustomerState()
	{
		
	}
	
	private static void EnterProductState()
	{
		
	}
	
	private static void EnterInventoryState()
	{
		
	}
	
	private static void EnterQuitState()
	{
		
	}

}
