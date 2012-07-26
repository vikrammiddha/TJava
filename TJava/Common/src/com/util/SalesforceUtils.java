package src.com.util;

import org.apache.log4j.Logger;

import src.com.Config.AppConfig;
import src.com.Exception.ResilientException;
import src.com.SFDC.EnterpriseSession;

/**
 * This class is used to connect to SFDC and get the Session.
 * 
 * @author vmiddha
 *
 */
public final class SalesforceUtils {
	
	private static Logger LOGGER = Logger.getLogger(SalesforceUtils.class);
	
    /**
     * Private constructor.
     */
    private SalesforceUtils() {
        throw new UnsupportedOperationException("Class is not instantiable.");
    }
    
    /**
     * Connects to Master SFDC.
     * 
     * @param appConfig AppConfig instance
     * @return a populated PartnerSession for the Master session.
     * @throws ResilientException 
     */
    public static EnterpriseSession initMasterSession(final AppConfig appConfig) throws ResilientException {
    	LOGGER.info("SalesforceUtils: initMasterSession(): Connecting to SFDC...........");
    	EnterpriseSession session = new EnterpriseSession();
        boolean connected = session.connect(appConfig);
        if(connected) {
        	return session;
        }
        return null;
    }
    
}
