package src.com.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * This class is used to initialize and get the Configuration detail from directv.properties file.
 * 
 * @author jkannan
 *
 */
public class Configurator {

	private static Logger LOGGER = Logger.getLogger(Configurator.class);

	/**
	 * Private constructor.
	 */
	private Configurator() {
		throw new UnsupportedOperationException("Class is not instantiable.");
	}

	/**
	 * initialize and get the Configuration
	 * 
	 * @return
	 */
	public static AppConfig getAppConfig() {

		//LOGGER.info("Configurator:getAppConfig(): Configuring the Application credentials .........................");
		
		Properties props = new Properties();
		AppConfig appConfig = new AppConfig();

		try {
			props.load(new FileInputStream("Resilient.properties"));

			// SFDC
			appConfig.setSfdcEndpoint(props.getProperty("sfdc.sfdcEndpoint"));
			appConfig.setSfdcUsername(props.getProperty("sfdc.sfdcUsername"));
			appConfig.setSfdcPassword(props.getProperty("sfdc.sfdcPassword"));
									
			
			appConfig.setSfdcBillToItemQueryFields(props.getProperty("resilient.launchReport.sfdcBillToItemQueryFields"));
			
		} catch (IOException e) {
			LOGGER.error("Exception while configuring the Application credentials ..." + e);
		}
		return appConfig;
	}

}
