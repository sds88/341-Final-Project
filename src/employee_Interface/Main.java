package employee_Interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.Date;

public class Main {
	public static MySQLConnection conn;
	public static State state = State.HOME;
	
	public enum State {
		HOME,
		ORDER,
		CUSTOMER1,
		CUSTOMER2,
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
		case CUSTOMER1:
			EnterCustomerLookupState();
			break;
		case CUSTOMER2:
			EnterCustomerAddState();
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
		System.out.println("3.\tAdd a customer.");
		System.out.println("4.\tLookup a product.");
		System.out.println("5.\tCheck inventory.");
		System.out.println("6.\tQuit.");
		char selection = ReadChar();
		switch(selection) {
		case '1':
			state = State.ORDER;
			break;
		case '2':
			state = State.CUSTOMER1;
			break;
		case '3':
			state = State.CUSTOMER2;
			break;
		case '4':
			state = State.PRODUCT;
			break;
		case '5':
			state = State.INVENTORY;
			break;
		case '6':
			state = State.QUIT;
			break;
		default:
			state = State.HOME;
		}
	}
	
	private static void EnterOrderState()
	{
		ClearConsole();
		System.out.println("Enter the Store ID:\t");
		String store = ReadLine();
		// TODO: Make this customer name and have it search for ID
		System.out.println("Enter the Customer ID:\t");
		String customer = ReadLine();
		String price = "0";
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = format.format(date);
		String sql = "INSERT INTO Orders (`Store ID`, `Customer ID`, `Total Price`, `Timestamp`)" +
				"VALUES ("+store+","+customer+","+price+",\""+datetime+"\");";
		String orderId = conn.CreateOrderWithQuery(sql);
		System.out.println("Order #"+orderId+" created.  Begin order content additions:");
		while(true)
		{
			System.out.println("Enter item UPC Code or \"done\" to finish the order:\t");
			String upc = ReadLine();
			if(upc.compareToIgnoreCase("done")==0)
				break;
			System.out.println("Enter amount of item "+upc+":\t");
			String amount = ReadLine();
			sql = "INSERT INTO Order_Content (`Order ID`, `UPC Code`, `Amount`) " +
					"VALUES ("+orderId+","+upc+","+amount+");";
			conn.ExecuteNonQuery(sql);
		}
		System.out.println("Order completed.  Press enter continue to the home screen.");
		ReadChar();
		state = State.HOME;
	}
	
	private static void EnterCustomerLookupState()
	{
		ClearConsole();
		System.out.println("Please select which parameter to search with:");
		System.out.println("1. Customer ID (XXXXXXXXXX).");
		System.out.println("2. Name (First, Last, or both).");
		System.out.println("3. Phone Number (XXXXXXXXXX).");
		System.out.println("4. Address (Street, City, State, ZIP).");
		System.out.println("5. Back.");
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
		System.out.println("Enter the search string:");
		String para = ReadLine();
		String sql = "Select * FROM Customers Where ";
		switch(selection) {
		case '1':	// TODO: Maybe make it so you can search by partial id
			while(!IsValidCustomerID(para))
			{
				System.out.println("Invalid customer id.");
				System.out.println("Please enter a customer id in the form of a group of 10 digits (i.e. 0123456789):");
				para = ReadLine();
			}
			sql += "Customers.`Customer ID` = "+para+";";
			conn.ExecuteQuery(sql);
			break;
		case '2':	// Do not need to check if the entered string is valid
			sql += "Customers.Name LIKE \"%"+para+"%\";";
			conn.ExecuteQuery(sql);
			break;
		case '3':	// TODO: Maybe make it so you can search by partial phone number
			while(!IsValidPhoneNumber(para))
			{
				System.out.println("Invalid phone number.");
				System.out.println("Please enter a phone number in the form of of 10 digits without dashes (i.e. 6143070417):");
				para = ReadLine();
			}
			sql += "Customers.`Phone Number` = "+para+";";
			conn.ExecuteQuery(sql);
			break;
		case '4': // Do not need to check if the entered string is valid
			sql += "Customers.Address LIKE \"%"+para+"%\";";
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
	
	private static Boolean IsValidPhoneNumber(String number)
	{
		// Phone number constraints are the same as the customer id constraints so just check it with that
		return IsValidCustomerID(number);
	}
	
	private static void EnterCustomerAddState()
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
		System.out.println("Please select which parameter to search with:");
		System.out.println("1. UPC Code (XXXXXXXXXX).");
		System.out.println("2. Name.");
		System.out.println("3. Brand.");
		System.out.println("4. MSRP (XX.XX).");
		System.out.println("5. Category.");
		System.out.println("6. Back.");
		char selection = ReadChar();
		if(selection == '6')
		{
			state = State.HOME;
			return;
		} 
		else if(selection < '1' || selection > '6')
		{
			System.out.println("Invalid selection.");
			return;
		}
		System.out.println("Enter the search string:");
		String para = ReadLine();
		String sql = "Select * FROM Products Where ";
		switch(selection) {
		case '1':
			while(!IsValidUPC(para))
			{
				System.out.println("Invalid UPC code.");
				System.out.println("Please enter a UPC code in the form of a group of 12 digits (i.e. 123412341234):");
				para = ReadLine();
			}
			sql += "Products.`UPC Code` = "+para+";";
			conn.ExecuteQuery(sql);
			break;
		case '2':	// Do not need to check if the entered string is valid
			sql += "Products.`Product Name` LIKE \"%"+para+"%\";";
			conn.ExecuteQuery(sql);
			break;
		case '3':	// Do not need to check if the entered string is valid
			sql += "Products.`Brand` LIKE \"%"+para+"%\";";
			conn.ExecuteQuery(sql);
			break;
		case '4': // TODO: Maybe add the option to search for greater/less than a number
			while(!IsValidMSRP(para))
			{
				System.out.println("Invalid MSRP amount.");
				System.out.println("Please enter an MSRP amount in the form of 'dollars.'cents' (i.e. 10.99):");
				para = ReadLine();
			}
			sql += "Products.`MSRP` = "+para+";";
			conn.ExecuteQuery(sql);
			break;
		case '5': // Do not need to check if the entered string is valid
			sql += "Products.`Categories` LIKE \"%"+para+"%\";";
			conn.ExecuteQuery(sql);
			break;
		default:
			break;
		}
	}
	
	private static Boolean IsValidUPC(String upc)
	{
		// Check length
		if(upc.length() != 12)
			return false;
		// Check all characters are digits 0-9
		for(char c : upc.toCharArray())
		{
			if(c < '0' || c > '9')
				return false;
		}
		return true;
	}
	
	private static Boolean IsValidMSRP(String msrp)
	{
		/* Lol.  Taken from double.valueOf() entry in the java api */
		final String Digits     = "(\\p{Digit}+)";
		final String HexDigits  = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally signed decimal integer.
		final String Exp        = "[eE][+-]?"+Digits;
		final String fpRegex    = 
				("[\\x00-\\x20]*"+	// Optional leading "whitespace"
				"[+-]?(" +			// Optional sign character
				"NaN|" +          	// "NaN" string
				"Infinity|" +      	// "Infinity" string		             	
		        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|" +	// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt		          
		        "(\\.("+Digits+")("+Exp+")?)|"+	// . Digits ExponentPart_opt FloatTypeSuffix_opt
		        "((" +		        
		        "(0[xX]" + HexDigits + "(\\.)?)|" + // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt		        
		       	"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" + // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
		       	")[pP][+-]?" + Digits + "))" +
		       	"[fFdD]?))" +
				"[\\x00-\\x20]*");// Optional trailing "whitespace"
		            
		  if (Pattern.matches(fpRegex, msrp))
		            return true;
		  return false;
	}
	
	private static void EnterInventoryState()
	{
		ClearConsole();
		System.out.println("Enter Store ID, or \"back\" to go back:\t");
		String id = ReadLine();
		if(id.compareToIgnoreCase("back") == 0)
		{
			state = State.HOME;
			return;
		}
		String sql = "Select * From Inventory WHERE `Store ID` = "+id+";";
		conn.ExecuteQuery(sql);
	}
	
	private static void EnterQuitState()
	{
		ClearConsole();
		System.out.println("Good bye.");
		conn.CloseConnection();
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
