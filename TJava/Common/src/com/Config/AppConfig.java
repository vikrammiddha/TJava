package src.com.Config;

/**
 * This class is used to hold the Configuration Information from directv.properties file. 
 * 
 * @author vmiddha
 *
 */
public final class AppConfig {

	
	// SFDC
    private String sfdcEndpoint;
	public String getSfdcEndpoint() {
		return sfdcEndpoint;
	}
	public void setSfdcEndpoint(String sfdcEndpoint) {
		this.sfdcEndpoint = sfdcEndpoint;
	}
	public String getSfdcUsername() {
		return sfdcUsername;
	}
	public void setSfdcUsername(String sfdcUsername) {
		this.sfdcUsername = sfdcUsername;
	}
	public String getSfdcPassword() {
		return sfdcPassword;
	}
	public void setSfdcPassword(String sfdcPassword) {
		this.sfdcPassword = sfdcPassword;
	}
	private String sfdcUsername;
    private String sfdcPassword;
    
    
    public String getSfdcBillToItemQueryFields() {
		return sfdcBillToItemQueryFields;
	}
	public void setSfdcBillToItemQueryFields(String sfdcBillToItemQueryFields) {
		this.sfdcBillToItemQueryFields = sfdcBillToItemQueryFields;
	}
	
	private String sfdcBillToItemQueryFields;
    
}