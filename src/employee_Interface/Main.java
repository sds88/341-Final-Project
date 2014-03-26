package employee_Interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
		ClearConsole();
		System.out.println("Welcome.  Please select an option from the following list:");
		System.out.println("1.\tInput an order.");
		System.out.println("2.\tLookup a customer.");
		System.out.println("3.\tLookup a product.");
		System.out.println("4.\tCheck inventory.");
		System.out.println("5.\tQuit.");
		char selection = ReadChar();
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
		ClearConsole();
		System.out.println("Please select which parameter to search with:");
		System.out.println("1. Customer ID (XXXXXXXXXX).");
		System.out.println("2. Name (First, Last, or both).");
		System.out.println("3. Phone Number (XXXXXXXXXX).");
		System.out.println("4. Address (Street, City, State, ZIP).");
		System.out.println("5. Back.");
		char selection = ReadChar();
		System.out.println("Enter the search string:");
		String para = ReadLine();
		String sql = "Select * FROM Customers Where ";
		switch(selection) {
		case '1':
			while(!IsValidCustomerID(para))
			{
				System.out.println("Invalid customer id.");
				System.out.println("Please enter a customer id in the form of a group of 10 digits (i.e. 0123456789):");
				para = ReadLine();
			}
			sql += "Customers.`Customer ID` = "+para+";";
			conn.ExecuteQuery(sql);
			break;
		default:
			break;
		}
		
	}
	
	private static Boolean IsValidCustomerID(String id)
	{
		// Check length
		if(id.length() != 10)
			return false;
		// Check all characters are digits 0-9
		for(char c : id.toCharArray())
		{
			if(c < '0' || c > '9')
				return false;
		}
		return true;
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

	private static char ReadChar()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		char c = '\0';
		try {
			c = (char) br.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	private static String ReadLine()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String string = "";
		try {
			string = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	private static void ClearConsole()
	{
		try {
			String os = System.getProperty("os.name");
			if(os.contains("Windows"))
			{
				Runtime.getRuntime().exec("cls");
			}
			else
			{
				Runtime.getRuntime().exec("clear");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
