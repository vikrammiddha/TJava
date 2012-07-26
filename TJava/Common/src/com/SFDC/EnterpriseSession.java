package src.com.SFDC;

import java.net.URL;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import src.com.Config.AppConfig;
import src.com.Exception.ResilientException;

import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.*;
import com.sforce.soap.partner.fault.*;;

/**
 * This class is used to initialize SFDC Client Stub SoapBindingStub. And this class is used as an entry point to SFDC communication.
 * 
 * @author jkannan
 *
 */

public class EnterpriseSession {

	private static Logger LOGGER = Logger.getLogger(EnterpriseSession.class);

	private SoapBindingStub binding;
	private LoginResult loginResult = null;
	AppConfig appConfig = null;

	public EnterpriseSession() {
	}

	/**
	 * Initialize the Appconfig and call to login.
	 * 
	 * @param appConfig
	 * @return
	 * @throws ResilientException 
	 */
	public boolean connect(final AppConfig appConfig) throws ResilientException {
		this.appConfig = appConfig;
		return login();
	}

	/**
	 * used to Login to SFDC and initialize SFDC Client Stub SoapBindingStub.
	 * 
	 * @return
	 * @throws ResilientException 
	 */
	private boolean login() throws ResilientException {

		// Provide feed back while we create the web service binding
		LOGGER.info("Creating the binding to the web service...");

		/*
		 * URL wsdlURL; try { wsdlURL = new File("enterprise.wsdl").toURL();
		 * }catch (IOException e) { throw new
		 * RuntimeException("Can't find WSDL.", e); }
		 */
		try {
			// SforceService service = new
			// SforceServiceLocator(wsdlURL.toString(), new
			// QName("urn:partner.soap.sforce.com", "SforceService"));
			// binding = (SoapBindingStub)service.getSoap();

			binding = (SoapBindingStub) new SforceServiceLocator().getSoap(new URL(appConfig.getSfdcEndpoint()));
			LOGGER.info("The login url is: " + binding._getProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY));
		}catch (Exception ex) {
			LOGGER.error("creating binding to soap service, error was: \n" + ex.getMessage());
			throw new ResilientException("Error while creating binding soap service " + ex.getMessage(), ex);
		}

		// Time out after a minute
		binding.setTimeout(60000);

		// Attempt the login giving the user feedback
		LOGGER.info("Login into Salesforce.com now....");
		try {
			loginResult = binding.login(appConfig.getSfdcUsername(),
					appConfig.getSfdcPassword());
		} catch (LoginFault lf) {
			LOGGER.error("Login errors " + lf.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+lf.getExceptionMessage(),lf);
			 
		} catch (UnexpectedErrorFault uef) {
			LOGGER.error("Login errors " + uef.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+uef.getExceptionMessage(),uef);
			 
		} catch (RemoteException re) {
			LOGGER.error("Login errors " + re.getMessage());
			throw new ResilientException("Error while creating binding soap service"+re.getMessage(),re);
	 
			 
		}

		LOGGER.info("The session id is: " + loginResult.getSessionId());
		LOGGER.info("The new server url is: " + loginResult.getServerUrl());

		// set the session header for subsequent call authentication
		binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY,
				loginResult.getServerUrl());

		// Create a new session header object and set the session id to that
		// returned by the login
		SessionHeader sh = new SessionHeader();
		sh.setSessionId(loginResult.getSessionId());
		binding.setHeader(new SforceServiceLocator().getServiceName()
				.getNamespaceURI(), "SessionHeader", sh);

		if (loginResult.isPasswordExpired()) {
			LOGGER.error("An error has occurred. Your password has expired.");
			return false;
		}

		LOGGER.info("EnterpriseSession: login(): Successfully Connected to SFDC...........");
		return true;
	}

	/**
	 * Submit query to SFDC.
	 * 
	 * @param query
	 *            SOQL query
	 * @return QueryResult
	 * @throws ResilientException 
	 */
	public QueryResult query(final String query) throws ResilientException {
		 
			try {
				return binding.query(query);
			} catch (InvalidSObjectFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
				} catch (MalformedQueryFault e) {
					LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
					throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			}
				catch (InvalidFieldFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (InvalidIdFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (UnexpectedErrorFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (InvalidQueryLocatorFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (RemoteException e) {
				LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
			} 
		
	}

	/**
	 * Submit queryMore() call to SFDC.
	 * 
	 * @param queryLocator
	 *            from previous query
	 * @return QueryResult object
	 * @throws ResilientException 
	 */
	public QueryResult queryMore(final String queryLocator) throws ResilientException {

		try {
			return binding.queryMore(queryLocator);
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}catch (Exception e) {
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}

	}

	/**
	 * Submit create() call to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to create
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public SaveResult[] create(final SObject[] sobjects) throws ResilientException {
		 
			try {
				 SaveResult[] sr = binding.create(sobjects);
				 return sr;
			} catch (InvalidSObjectFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
				} catch (MalformedQueryFault e) {
					LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
					throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (InvalidFieldFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (InvalidIdFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (UnexpectedErrorFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (InvalidQueryLocatorFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (RemoteException e) {
				LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
			}catch (Exception e) {
				throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
			}
		 
	}

	/**
	 * Submit update() call to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to update
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public SaveResult[] update(final SObject[] sobjects) throws ResilientException {
		try {
			return binding.update(sobjects);
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}catch (Exception e) {
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}
	}

	/**
	 * Submit delete() call to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to create
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public DeleteResult[] delete(final String[] ids) throws ResilientException {
		try {
			return binding.delete(ids);
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}
	}
	
	/**
	 * Create batches when the number of records to update exceeds the API limit and submit update() call recursively to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to update
	 * @param totalRecords         
	 *            Number of records to update
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public SaveResult[] updateMore(final SObject[] sobjects, int totalRecords) throws ResilientException {
		try {	
			// Define the maximum API limit
			final int MAX_BATCH_SIZE = 200;
			
			int totalCount = totalRecords;
			int posCounter = 0;
			int resultsCounter =0;
			
			// List of SaveResult to hold the consolidated results
			SaveResult[] saveResults = new SaveResult[totalRecords];
			
			// Repeat this loop until complete list of SObjects is updated
			while (totalCount > 0)
	            {
				// Get the number of SObject records that need to be updated				
	                        int batchCount = totalCount;
	                        
	                  // Check if the number is larger than the maximum API limit
	                        if (totalCount > MAX_BATCH_SIZE) {
	                        	//Split into batches of permitted size
	                                batchCount = MAX_BATCH_SIZE;
	                        }
	                  // Define a new list of SObject to hold a subset of the list of SObjects that need to be updated
	                        SObject[] updateSObj = new SObject[batchCount];
	                        int thisBatchCounter = 0;
	                        
	                  // Create the current batch from the list of SObject that need to be updated 
	                        for (int i = posCounter; i < posCounter + batchCount; i++) {
	                              updateSObj[thisBatchCounter] = sobjects[i];
	                              thisBatchCounter++;
	                        }
	                  // List of SaveResult to hold the current batch update results      
	                        SaveResult[] saveTempResults = null;
	                  // Update the current batch
	                        saveTempResults = binding.update(updateSObj);
	                    
	                  // Save the results of the current batch update into the consolidated results      
	                        for (int j= 0; (saveTempResults!=null && j<saveTempResults.length); j++) {
	                                saveResults[resultsCounter]=saveTempResults[j];
	                                resultsCounter++;	                                                        	                
	                        }
	                  // Adjust position to the next SObject in the list      
	                        posCounter = posCounter + batchCount;
	                  // Calculate the number of SObject records yet to be updated.      
	                        totalCount = totalCount - batchCount;
	            }			
			return saveResults;
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}catch (Exception e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}
	}
	
	/**
	 * Create batches when the number of records to insert exceeds the API limit and submit update() call recursively to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to update
	 * @param totalRecords         
	 *            Number of records to update
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public SaveResult[] createMore(final SObject[] sobjects, int totalRecords) throws ResilientException {
		try {	
			// Define the maximum API limit
			final int MAX_BATCH_SIZE = 200;
			
			int totalCount = totalRecords;
			int posCounter = 0;
			int resultsCounter =0;
			
			// List of SaveResult to hold the consolidated results
			SaveResult[] saveResults = new SaveResult[totalRecords];
			
			// Repeat this loop until complete list of SObjects is inserted
			while (totalCount > 0)
	            {
				// Get the number of SObject records that need to be inserted				
	                        int batchCount = totalCount;
	                        
	                  // Check if the number is larger than the maximum API limit
	                        if (totalCount > MAX_BATCH_SIZE) {
	                        	//Split into batches of permitted size
	                                batchCount = MAX_BATCH_SIZE;
	                        }
	                  // Define a new list of SObject to hold a subset of the list of SObjects that need to be inserted
	                        SObject[] insertSObj = new SObject[batchCount];
	                        int thisBatchCounter = 0;
	                        
	                  // Create the current batch from the list of SObject that need to be inserted 
	                        for (int i = posCounter; i < posCounter + batchCount; i++) {
	                              insertSObj[thisBatchCounter] = sobjects[i];
	                              thisBatchCounter++;
	                        }
	                  // List of SaveResult to hold the current batch update results      
	                        SaveResult[] saveTempResults = null;
	                  // Update the current batch
	                        saveTempResults = binding.create(insertSObj);
	                    
	                  // Save the results of the current batch update into the consolidated results      
	                        for (int j= 0; (saveTempResults!=null && j<saveTempResults.length); j++) {
	                                saveResults[resultsCounter]=saveTempResults[j];
	                                resultsCounter++;	                                                        	                
	                        }
	                  // Adjust position to the next SObject in the list      
	                        posCounter = posCounter + batchCount;
	                  // Calculate the number of SObject records yet to be inserted.      
	                        totalCount = totalCount - batchCount;
	            }			
			return saveResults;
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}
	}
	/**
	 * Create batches when the number of records to deleted exceeds the API limit and submit update() call recursively to SFDC.
	 * 
	 * @param sobjects
	 *            Objects to update
	 * @param totalRecords         
	 *            Number of records to update
	 * @return List of SaveResult objects
	 * @throws ResilientException 
	 */
	public DeleteResult[] deleteMore(final String[] id, int totalRecords) throws ResilientException {
		try {	
			// Define the maximum API limit
			final int MAX_BATCH_SIZE = 200;
			
			int totalCount = totalRecords;
			int posCounter = 0;
			int resultsCounter =0;
			
			// List of DeleteResult to hold the consolidated results
			DeleteResult[] delResults = new DeleteResult[totalRecords];
			
			// Repeat this loop until complete list of SObjects Id is deleted
			while (totalCount > 0)
	            {
				// Get the number of SObject records that need to be deleted				
	                        int batchCount = totalCount;
	                        
	                  // Check if the number is larger than the maximum API limit
	                        if (totalCount > MAX_BATCH_SIZE) {
	                        	//Split into batches of permitted size
	                                batchCount = MAX_BATCH_SIZE;
	                        }
	                  // Define a new list of SObject to hold a subset of the list of SObjects that need to be deleted
	                        String[] insertSObj = new String[batchCount];
	                        int thisBatchCounter = 0;
	                        
	                  // Create the current batch from the list of SObject that need to be deleted 
	                        for (int i = posCounter; i < posCounter + batchCount; i++) {
	                              insertSObj[thisBatchCounter] = id[i];
	                              thisBatchCounter++;
	                        }
	                  // List of DeleteResult to hold the current batch deleted results      
	                        DeleteResult[] delTempResults = null;
	                  // Update the current batch
	                        delTempResults = binding.delete(insertSObj);
	                    
	                  // Save the results of the current batch deleted into the consolidated results      
	                        for (int j= 0; (delTempResults!=null && j<delTempResults.length); j++) {
	                                delResults[resultsCounter]=delTempResults[j];
	                                resultsCounter++;	                                                        	                
	                        }
	                  // Adjust position to the next SObject in the list      
	                        posCounter = posCounter + batchCount;
	                  // Calculate the number of SObject records yet to be deleted.      
	                        totalCount = totalCount - batchCount;
	            }			
			return delResults;
		} catch (InvalidSObjectFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
			} catch (MalformedQueryFault e) {
				LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
				throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidFieldFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidIdFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (UnexpectedErrorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (InvalidQueryLocatorFault e) {
			LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
		} catch (RemoteException e) {
			LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
			throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
		}
	}


}
