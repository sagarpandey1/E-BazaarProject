package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.Catalog;
import business.externalinterfaces.CatalogTypes;
import business.externalinterfaces.DbClassCatalogForTest;

import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

/**
 * This class is concerned with managing data for a single
 * catalog. To read or update the entire list of catalogs in
 * the database, see DbClassCatalogs
 *
 */
class DbClassCatalog implements DbClass,DbClassCatalogForTest {

	enum Type {INSERT, READ_CATALOG, DELETE_CATALOG};
	@SuppressWarnings("unused")
	private static final Logger LOG =
		Logger.getLogger(DbClassCatalog.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS =
    	new DataAccessSubsystemFacade();

	private Catalog catalog;
	private Type queryType;

	private String insertQuery = "INSERT into CatalogType (catalogname) VALUES(?)";
	private String deleteQuery = "DELETE FROM CatalogType WHERE catalogid = ?";
	private String readQuery = "SELECT * from CatalogType where catalogname = ?";
	private Object[] insertParams, deleteParams, readParams;
	private int[] insertTypes, deleteTypes, readTypes;

    public int saveNewCatalog(String catalogName) throws DatabaseException {
    	//should we allow a new catalog with the same name to be there?
    	queryType = Type.INSERT;
    	insertParams = new Object[]{catalogName};
    	insertTypes = new int[]{Types.VARCHAR};
    	return dataAccessSS.insertWithinTransaction(this);
    }

    public void deleteCatalog(Catalog catalog) throws DatabaseException {
    	//implement
    	//LOG.warning("Method deleteCatalog in DbClassCatalog has not been implemented");
    	queryType = Type.DELETE_CATALOG;
    	deleteParams = new Object[]{catalog.getId()};
    	deleteTypes = new int[]{Types.INTEGER};
    	int num = dataAccessSS.deleteWithinTransaction(this);

    	//this is for testing
    	if(num > 0 ) catalog.setId(-1);
    }

    public Catalog getCatalogFromName(String catName) throws DatabaseException {

    	queryType = Type.READ_CATALOG;
    	readParams = new Object[]{catName};
    	readTypes = new int[]{Types.VARCHAR};
    	dataAccessSS.atomicRead(this);
    	return catalog;
    }

    @Override
	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
    	return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

    @Override
	public String getQuery() {
		switch(queryType) {
			case INSERT:
				return insertQuery;
			case DELETE_CATALOG:
				return deleteQuery;
			case READ_CATALOG:
				return readQuery;
			default:
				return null;
		}
	}
    @Override
   	public Object[] getQueryParams() {
   		switch(queryType) {
   			case INSERT:
   				return insertParams;
   			case DELETE_CATALOG:
   				return deleteParams;
   			case READ_CATALOG:
   				return readParams;
   			default:
   				return null;
   		}
    }
	 @Override
	public int[] getParamTypes() {
		 switch(queryType) {
			case INSERT:
				return insertTypes;
			case DELETE_CATALOG:
				return deleteTypes;
			case READ_CATALOG:
				return readTypes;
			default:
				return null;
		}
	 }
    @Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		// do nothing
		switch(queryType){
			case READ_CATALOG:
				populateCatalog(resultSet);
				break;
			case INSERT:
				populateCatalog(resultSet);
				break;
			default:
				//do nothing
		}
	}

    private void populateCatalog(ResultSet rs) throws DatabaseException{
    	try{
    		if(rs.next()){
    			catalog = new CatalogImpl(rs.getInt("catalogid"),
				//(new CatalogTypesImpl()).getCatalogName(rs.getInt("catalogid")));
    					rs.getString("catalogname"));
    		}
    	}catch(SQLException ex){
    		throw new DatabaseException(ex);
    	}
    }



}
