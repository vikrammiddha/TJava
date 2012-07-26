/*########################################################################### 
# File..................: MainClass.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 24-07-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This is a test class to confirm connectivity to SFDC		   							 
########################################################################### 
*/


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bean.BillItem;

import src.com.Config.AppConfig;
import src.com.Config.Configurator;
import src.com.Exception.ResilientException;
import src.com.SFDC.EnterpriseSession;
import src.com.util.SalesforceUtils;


public class MainClass {
	
	private EnterpriseSession eSession = null;
	
	private static AppConfig appConfig = null;
	
	private static QuerySFDC querySfdc = null;
	
	private static Logger LOGGER = Logger.getLogger(MainClass.class);
	
	private ReportUtils utils = null;
	
	public MainClass() throws Exception{
		
		appConfig = Configurator.getAppConfig();
		
		try{
			
			eSession = SalesforceUtils.initMasterSession(appConfig);				
			
		}catch(ResilientException de) {
			LOGGER.error("Exception while initializing SFDC Login Session...: " + de);
			throw de;
		}
		
		querySfdc = new QuerySFDC(eSession);
		
		utils = new ReportUtils();
				
	}
	
	/**
	 * The main class.
	 * @throws Exception 
	 * 
	 */
	public static void main(String[] s) throws Exception {
		
		MainClass mObj = new MainClass();
		mObj.generateReports("","","B-00003062");
		
	}
	
	
	private Boolean generateReports(String accountNumber, String billRunId, String billId){
				
		LOGGER.info("Method Started : generateReports");
		
		Boolean status = true;
		
		if(utils.isBlank(accountNumber) && utils.isBlank(billRunId) && utils.isBlank(billId)){			
			return false;	
		}
		
		try{
			String query = utils.getQuery(appConfig);
			
			if(utils.isNotBlank(accountNumber)){
				query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Account__r.Espresso_PC__Account_Number__c", accountNumber , query);
			}
			
			if(utils.isNotBlank(billRunId)){
				query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Bill_Run_ID__c", billRunId , query);
			}
			
			if(utils.isNotBlank(billId)){
				query = utils.addWhereClause("Espresso_Bill__Bill__r.Name", billId, query);
			}
			
			LOGGER.info("Query prepared : " + query);
						
			ArrayList<BillItem> biItemList = utils.populateBillItemBeans(query, querySfdc);
			
			LOGGER.info("Total number of Bill Item records queried: " + biItemList.size());
								
			// This is the place where Nimil's code will start with bitemList as Input;
			
		}catch(Exception e){
			LOGGER.error("Exception occured while preparing data for Bill Item. Cause : " + e.getStackTrace());
		}
		
		return status;
	}
	
	
}
