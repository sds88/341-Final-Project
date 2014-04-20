package administrator_interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import employee_Interface.Main.State;

public class Main {

	public static MySQLConnection conn;
	public static State state = State.HOME;
	
	public enum State {
		HOME,
		STORE,
		CUSTOMER,
		PRODUCT,
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
		case STORE:
			EnterStoreState();
			break;
		case CUSTOMER:
			EnterCustomerState();
			break;
		case PRODUCT:
			EnterProductState();
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
		System.out.println("1.\tAdd a new store.");
		System.out.println("2.\tAdd a new customer.");
		System.out.println("3.\tAdd a new product.");
		System.out.println("4.\tQuit.");
		char selection = ReadChar();
		switch(selection) {
		case '1':
			state = State.STORE;
			break;
		case '2':
			state = State.CUSTOMER;
			break;
		case '3':
			state = State.PRODUCT;
			break;
		case '4':
			state = State.QUIT;
			break;
		default:
			state = State.HOME;
		}
	}
	
	private static void EnterStoreState()
	{
		ClearConsole();
		System.out.println("Enter the store's address:\t");
		String addr = ReadLine();
		System.out.println("Enter the store's opening hours:\t");
		String open = ReadLine();
		System.out.println("Enter the store's closing hours:\t");
		String close = ReadLine();
		System.out.println("Enter the store's phone number:\t");
		String phone = ReadLine();
		String sql = "INSERT INTO Stores (`Address`, `Open Time`, `Close Time`, `Phone Number`)" +
				"VALUES (\""+addr+"\",\""+open+"\",\""+close+"\","+phone+") " +
				"ON DUPLICATE KEY UPDATE `Address` = `Address`, `Phone Number` = `Phone Number`, `Open Time` = `Open Time`, `Close Time` = `Close Time`;";
		conn.ExecuteNonQuery(sql);
		System.out.println("Store added.  Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterCustomerState()
	{
		ClearConsole();
		System.out.println("Enter the customer's name:\t");
		String name = ReadLine();
		System.out.println("Enter the customer's phone number:\t");
		String phone = ReadLine();
		System.out.println("Enter the customer's address:\t");
		String address = ReadLine();
		String sql = "INSERT INTO Customers (`Name`, `Phone Number`, `Address`, `Reward Points`)" +
				"VALUES (\""+name+"\","+phone+",\""+address+"\",0) " +
				"ON DUPLICATE KEY UPDATE `Name` = `Name`, `Phone Number` = `Phone Number`, `Address` = `Address`;";
		conn.ExecuteNonQuery(sql);
		System.out.println("Customer added.  Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterProductState()
	{
		ClearConsole();
		System.out.println("Enter the product's UPC Code:\t");
		String upc = ReadLine();
		System.out.println("Enter the product's name:\t");
		String name = ReadLine();
		System.out.println("Enter the product's brand:\t");
		String brand = ReadLine();
		System.out.println("Enter the product's categories (x, y, z, etc.):\t");
		String categories = ReadLine();
		System.out.println("Enter the product's MSRP:\t");
		String msrp = ReadLine();
		System.out.println("Enter the product's alcohol percent (0 for non alcoholic):\t");
		String percent = ReadLine();
		String sql = "INSERT INTO Products (`UPC Code`, `Product Name`, `Brand`, `Categories`, `MSRP`, `Alcohol Percent`)" +
				"VALUES ("+upc+", \""+name+"\", \""+brand+"\", \""+categories+"\", "+msrp+", "+percent+") " +
				"ON DUPLICATE KEY UPDATE `UPC Code` = `UPC Code`, `Product Name` = `Product Name`, `Brand` = `Brand`, `Categories` = `Categories`, `MSRP` = `MSRP`, `Alcohol Percent` = `Alcohol Percent`;";
		System.out.println(sql);
		conn.ExecuteNonQuery(sql);
		System.out.println("Product added.  Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
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
