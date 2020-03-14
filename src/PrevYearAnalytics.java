import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main entry point
 * 
 * Compile the input data into a set of transactions,
 * a list that stores client+company, and a list of salespeople,
 * and a mapping of currency to value per USD
 * 
 * Store a mapping of each company to its respective profile,
 * and update the profiles using the transactions
 * 
 * Write to output
 * 
 * @author Dennis
 *
 */
public class PrevYearAnalytics {
	
	public static final String CLIENT_FILE = "client.data";
	public static final String SALES_FILE = "sales.data";
	public static final String TRANSACTION_FILE = "transaction.data";
	public static final String CCY_FILE = "ccy.data";
	
	public static final String OUT_FILE = "out.txt";
	
	private static Set<Transaction> transactionSet = new HashSet<>();
	
	// list of size 2 tuples, (client, company)
	// list index corresponds to client ID
	private static List<String[]> clientCompanyIndex = new ArrayList<>();
	
	// list of sales names
	// list index corresponds to sales ID
	private static List<String> salesPeopleIndex = new ArrayList<>();
	
	private static Map<String, Double> currencyMap = new HashMap<>();
	
	// mapping of each company to its respective profile
	private static Map<String, CompanyProfile> companyProfileMap = new HashMap<>();
	
	public static void main(String[] args) {
		try {
			compileData();
			
			// update the profiles using the transactions
			updateCompanyProfiles();
			
			writeToOutput();
		} catch (IOException e) {
			System.err.println("IOException when reading file: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public static void compileData() throws IOException {
		/* currencyMap and clientCompanyIndex need be populated
		 * before transactionSet can be populated
		 * 
		 * company and USDAmt are transaction properties
		 * that are extracted from clientCompanyIndex and currencyMap
		 */
		populateCurrencyMap();
		populateClientCompanyIndex();
		
		populateTransactionSet();
		
		populateSalesPeopleIndex();
	}
	
	public static void populateCurrencyMap() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(CCY_FILE));
		in.readLine(); // column line
		String line;
		while ((line = in.readLine()) != null
				&& line.length() > 0) {
			String[] ccyData = line.trim().split(";");
			currencyMap.put(ccyData[0], Double.parseDouble(ccyData[1]));
		}
		in.close();
	}
	
	public static void populateClientCompanyIndex() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(CLIENT_FILE));
		in.readLine(); // column line
		String line;
		while ((line = in.readLine()) != null
				&& line.length() > 0) {
			String[] clientData = line.trim().split(";");
			String[] clientCompany = { clientData[1], clientData[4] };
			clientCompanyIndex.add(clientCompany);
			
			// insert this company into our records
			companyProfileMap.put(clientCompany[1], new CompanyProfile());
		}
		in.close();
	}
	
	public static void populateTransactionSet() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TRANSACTION_FILE));
		in.readLine(); // column line
		String line;
		while ((line = in.readLine()) != null
				&& line.length() > 0) {
			// construct a Transaction object and add it to the set
			
			String[] transData = line.trim().split(";");
			
			// only do 2019
			if (!"2019".equals(transData[1].split("/")[2])) {
				continue;
			}
			
			int clientId = Integer.parseInt(transData[4]);
			
			String[] clientCompany = clientCompanyIndex.get(clientId);
			String company = clientCompany[1];
			
			int salesId = Integer.parseInt(transData[5]);
			
			String ccy = transData[2];
			double localCost = Double.parseDouble(transData[3]);
			double USDAmt = localCost / currencyMap.get(ccy);
			
			Transaction transaction = new Transaction(company, salesId, clientId, USDAmt);
			
			transactionSet.add(transaction);
		}
		in.close();
	}
	
	public static void populateSalesPeopleIndex() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(SALES_FILE));
		in.readLine(); // column line
		String line;
		while ((line = in.readLine()) != null
				&& line.length() > 0) {
			String[] salesData = line.trim().split(";");
			salesPeopleIndex.add(salesData[0]);
		}
		in.close();
	}
	
	public static void updateCompanyProfiles() {
		for (Transaction transaction : transactionSet) {
			CompanyProfile profile = companyProfileMap.get(transaction.getCompany());
			profile.insertTransaction(transaction.getSalesId(),
					transaction.getClientId(),
					transaction.getUSDAmt());
		}
	}
	
	public static void writeToOutput() throws IOException {
		String[] companies = companyProfileMap.keySet().toArray(new String[0]);
		Arrays.sort(companies);
		
		PrintWriter out = new PrintWriter(new FileWriter(OUT_FILE));
		for (String company : companies) {
			CompanyProfile profile = companyProfileMap.get(company);
			
			/*
			 * Company
			 * Sales Name
			 * Total Transacted USD Volume of Company
			 * Top Client Name
			 * Total Transacted USD Volume of Top Client
			 */
			out.println(company + ", "
					+ salesPeopleIndex.get(profile.getTopSalesId()) + ", "
					+ roundCent(profile.getTotalVol()) + ", "
					+ clientCompanyIndex.get(profile.getTopClientId())[0] + ", "
					+ roundCent(profile.getTopClientVol()));
		}
		
		out.close();
	}
	
	public static String roundCent(double value) {
		long temp = Math.round(value * 100);
		String tempStr = String.valueOf(temp);
		return tempStr.substring(0, tempStr.length() - 2)
				+ "."
				+ tempStr.substring(tempStr.length() - 2);
	}

}
