package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import business.customersubsystem.DbClassCustomerProfile.Type;
import business.externalinterfaces.CustomerProfile;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassCreditCard implements DbClass {

	enum Type {
		READ
	};

	private static final Logger LOG = Logger.getLogger(DbClassCreditCard.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private Type queryType;

	private String readQuery = "SELECT * FROM customer WHERE custid = ?";
	private Object[] readParams;
	private int[] readTypes;

	private CreditCardImpl defaultPaymentInfo;
//	private CustomerProfile custProfile;
	
	public CreditCardImpl getDefaultPaymentInfo(CustomerProfile custProfile) throws DatabaseException{
    	queryType = Type.READ;
    	readParams  = new Object[]{custProfile.getCustId()};
    	readTypes = new int[]{Types.INTEGER};
        dataAccessSS.atomicRead(this); 
        return defaultPaymentInfo;
    }
	
	@Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	@Override
	public String getQuery() {
		switch(queryType) {
    	case READ :
    		return readQuery;
    	default :
    		return null;
	}
	}

	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
    	case READ :
    		return readParams;
    	default :
    		return null;
	}
	}

	@Override
	public int[] getParamTypes() {
		switch(queryType) {
    	case READ :
    		return readTypes;
    	default :
    		return null;
	}
	}

	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		
		try {   
            //we take the first returned row
            if(resultSet.next()){
            	defaultPaymentInfo 
                  = new CreditCardImpl(resultSet.getString("nameoncard"), 
                		  resultSet.getString("expdate"), 
                		  resultSet.getString("cardnum"), 
                		  resultSet.getString("cardtype"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
	}

}
