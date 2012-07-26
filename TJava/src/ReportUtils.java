import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bean.BillItem;

import src.com.Config.AppConfig;

public class ReportUtils {

	private static Logger LOGGER = Logger.getLogger(ReportUtils.class);
	
	public String getQuery( AppConfig appConfig){
		
		return "SELECT " + appConfig.getSfdcBillToItemQueryFields() ;
		
	}
	
	public Boolean isNotBlank(String val){
		
		if(null != val && "" != val.trim()){
			return true;
		}
		return false;
	}
	
	public Boolean isBlank(String val){
		
		if(null == val || "" != val.trim()){
			return true;
		}
		return false;
	}
	
	public String addWhereClause(String key, String value, String query){
		
		if(query.toUpperCase().contains("WHERE")){
			query += " AND " + key + "='" + value + "'";  
		}else{
			query += " WHERE " + key + "='" + value + "'";
		}
		
		return query;
	}
	
	
	public ArrayList<BillItem> populateBillItemBeans(String query, QuerySFDC querySFDC){
		
		ArrayList<BillItem> retBIList = new ArrayList<BillItem>();
		
		try{
			
			HashMap<String,Object>[] resultMap = querySFDC.executeQuery(query);
			
			for(int i=0; i<resultMap.length; i++){
				
				HashMap<String,Object> hm = resultMap[i];
				BillItem biObj = new BillItem();
				
				biObj.setAssetName((String)hm.get("ESPRESSO_BILL__ASSET__R.NAME"));
				biObj.setBillEndDate((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__BILL_TO__C"));
				//biObj.setBillPeriod((String)hm.get("Espresso_Bill__Asset__R.Name"));
				biObj.setDateFrom((String)hm.get("ESPRESSO_BILL__DATE_FROM__C"));
				biObj.setDateTo((String)hm.get("ESPRESSO_BILL__DATE_TO__C"));
				biObj.setRetalGross((String)hm.get("ESPRESSO_BILL__GROSS_AMOUNT_1__C"));
				biObj.setAccountNumber((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C"));
				
				retBIList.add(biObj);
				
			}
			
		}catch(Exception e){
			LOGGER.error("Error occured while query SFDC for BillItems. Cause : " + e.getStackTrace());
		}
		
		return retBIList;
	}
	
	 
}
