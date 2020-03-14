import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing sales id to total volume mapping,
 * client id to total volume mapping,
 * and total volume of this company
 * @author Dennis
 *
 */
public class CompanyProfile {
	
	private Map<Integer, Double> salesToVol = new HashMap<>();
	private Map<Integer, Double> clientToVol = new HashMap<>();
	
	private int topSalesId = -1;

	private double topSalesVol = 0;
	
	private int topClientId = -1;
	private double topClientVol = 0;
	
	private double totalVol = 0;

	/**
	 * Update this company profile with a new transaction.
	 * Make sure the top sales and top client IDs are up to date
	 * @param salesId
	 * @param clientId
	 * @param USDAmt
	 */
	public void insertTransaction(int salesId, int clientId, double USDAmt) {
		// total volume for this sales person
		double totalSalesVol = USDAmt;
		
		if (salesToVol.containsKey(salesId)) {
			totalSalesVol += salesToVol.get(salesId);
		}
		
		// update the sales id to total volume mapping
		salesToVol.put(salesId, totalSalesVol);
		
		// update the top sales id and volume for this company
		if (totalSalesVol > topSalesVol) {
			topSalesId = salesId;
			topSalesVol = totalSalesVol;
		}
		
		// total amount for this client
		double totalClientVol = USDAmt;
		
		if (clientToVol.containsKey(clientId)) {
			totalClientVol += clientToVol.get(clientId);
		}
		
		// update the client id to total volume mapping
		clientToVol.put(clientId, totalClientVol);
		
		// update the top client id and volume for this company
		if (totalClientVol > topClientVol) {
			topClientId = clientId;
			topClientVol = totalClientVol;
		}
		
		totalVol += USDAmt;
	}
	
	public int getTopSalesId() {
		return topSalesId;
	}

	public double getTotalVol() {
		return totalVol;
	}

	public int getTopClientId() {
		return topClientId;
	}

	public double getTopClientVol() {
		return topClientVol;
	}
	
}
