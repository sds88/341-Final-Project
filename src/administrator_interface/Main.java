package administrator_interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {

	public static MySQLConnection conn;
	public static State state = State.HOME;
	
	public enum State {
		HOME,
		STORE,
		CUSTOMER,
		PRODUCT,
		NONQUERY,
		QUERY,
		ADVANCED,
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
		case NONQUERY:
			EnterExecuteNonQueryState();
			break;
		case QUERY:
			EnterExecuteQueryState();
			break;
		case ADVANCED:
			EnterAdvancedState();
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
		System.out.println("4.\tRun a data manipulation query.");
		System.out.println("5.\tRun a data definition query.");
		System.out.println("6.\tView advanced query options.");
		System.out.println("7.\tQuit.");
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
			state = State.NONQUERY;
			break;
		case '5':
			state = State.QUERY;
			break;
		case '6':
			state = State.ADVANCED;
			break;
		case '7':
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
		conn.ExecuteNonQuery(sql);
		System.out.println("Product added.  Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterExecuteNonQueryState()
	{
		ClearConsole();
		System.out.println("Enter a data manipulation query (Insert, Update, Delete, etc.):\t");
		String sql = ReadLine();
		conn.ExecuteNonQuery(sql);
		System.out.println("Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterExecuteQueryState()
	{
		ClearConsole();
		System.out.println("Enter a data definition query (SELECT, VIEWS, etc.):\t");
		String sql = ReadLine();
		conn.ExecuteQuery(sql);
		System.out.println("Press enter to continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterAdvancedState()
	{
		ClearConsole();
		System.out.println("Welcome.  Please select an option from the following list:");
		System.out.println("1.\tView x customer's orders.");
		System.out.println("2.\tView orders containing x product.");
		System.out.println("3.\tView orders from store x.");
		System.out.println("4.\tView top ten products from store x.");
		System.out.println("5.\tBack.");
		char selection = ReadChar();
		if(selection == '5')
		{
			state = State.HOME;
			return;
		} 
		else if(selection < '1' || selection > '5')
		{
			System.out.println("Invalid selection.");
			return;
		}
		String sql, input;
		switch(selection) {
		case '1':
			System.out.println("Enter customer id:\t");
			input = ReadLine();
			sql = "Select `Order ID`, `Product Name`, `Amount`, `Timestamp` " +
					"From Orders left join Order_Content using(`Order ID`) left join Products using(`UPC Code`) " +
					"Where `Customer ID` = "+input+";";
			conn.ExecuteQuery(sql);
			break;
		case '2':
			System.out.println("Enter product UPC:\t");
			input = ReadLine();
			sql = "Select `Order ID`, `Store ID`, `Customer ID`, `Amount`, `Timestamp` " +
					"From Orders join Order_Content using(`Order ID`) join Products using(`UPC Code`) " +
					"where `UPC Code` = "+input+";";
			conn.ExecuteQuery(sql);
			break;
		case '3':
			System.out.println("Enter store id:\t");
			input = ReadLine();
			sql = "Select `Order ID`, `Customer ID`, `Total Price`, `Timestamp` " +
					"From Orders " +
					"where `Store ID` = "+input+";";
			conn.ExecuteQuery(sql);
			break;
		case '4':
			System.out.println("Enter store id:\t");
			input = ReadLine();
			sql = "Select `Product Name`, Sum(`Amount`) as `Number Sold` " +
					"From Orders join Order_Content using(`Order ID`) join Products using(`UPC Code`) " +
					"Where `Store ID` = "+input+" " +
					"Group By `UPC Code`" +
					"Order By `Number Sold` DESC " +
					"Limit 0,10";
			conn.ExecuteQuery(sql);
			break;
		}
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
