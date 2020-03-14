/**
 * Represents the relevant data gathered from the files at a modular level:
 * company ID, sales ID, client ID, USD transaction amount
 * @author Dennis
 *
 */
public class Transaction {
	
	/* We store sales and client IDs instead of names just in case
	 * different salespeople or clients share the same names
	 */
	private String company;
	private int salesId;
	private int clientId;
	private double USDAmt;

	public Transaction(String company, int salesId, int clientId, double USDAmt) {
		this.company = company;
		this.salesId = salesId;
		this.clientId = clientId;
		this.USDAmt = USDAmt;
	}
	
	public String getCompany() {
		return company;
	}

	public int getSalesId() {
		return salesId;
	}

	public int getClientId() {
		return clientId;
	}

	public double getUSDAmt() {
		return USDAmt;
	}

}
