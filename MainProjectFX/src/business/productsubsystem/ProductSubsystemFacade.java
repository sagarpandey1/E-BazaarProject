package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.util.TwoKeyHashMap;

public class ProductSubsystemFacade implements ProductSubsystem {
	private static final Logger LOG =
			Logger.getLogger(ProductSubsystemFacade.class.getPackage().getName());
	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}
	public static Product createProduct(Catalog c, String name,
			LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}
	public static Product createProduct(Catalog c, Integer pi, String pn, int qa,
			double up, LocalDate md, String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}

	/** obtains product for a given product name */
    public Product getProductFromName(String prodName) throws BackendException {
    	try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
    }
    public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer,String,Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}

	}
    public Product getProductFromId(Integer prodId) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}

    public List<Catalog> getCatalogList() throws BackendException {
    	try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();

		} catch(DatabaseException e) {
			throw new BackendException(e);
		}

    }

    public List<Product> getProductList(Catalog catalog) throws BackendException {
    	try {
    		DbClassProduct dbclass = new DbClassProduct();
    		return dbclass.readProductList(catalog);
    	} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
    }

	public int readQuantityAvailable(Product product) {
		//IMPLEMENT
		//LOG.warning("Method readQuantityAvailable(Product product) has not been implemented");
		//return 2;
		try{
			DbClassProduct dbclass = new DbClassProduct();
			Product p = dbclass.readProduct(product.getProductId());
			return p.getQuantityAvail();
		}catch(DatabaseException e){
			//throw new BackendException(e);
		}
		return 0; //need to review
	}

	public int saveNewCatalog(String catalogName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.saveNewCatalog(catalogName);
		} catch(DatabaseException e) {
    		throw new BackendException(e);
    	}
	}

	@Override
	public TwoKeyHashMap<Integer, String, Product> getProductTable() throws BackendException {
		// TODO Auto-generated method stub
		//return null;

		try {
    		DbClassProduct dbClass = new DbClassProduct();
    		return dbClass.readProductTable();
    	} catch(DatabaseException e) {
    		LOG.warning("incorrect information from database" + e);
    		throw new BackendException(e);
    	}
	}
	@Override
	public TwoKeyHashMap<Integer, String, Product> refreshProductTable() throws BackendException {
		// TODO Auto-generated method stub

		try {
			DbClassProduct dbClass = new DbClassProduct();
			return dbClass.refreshProductTable();
		} catch(DatabaseException e) {
			LOG.warning("incorrect information from database" + e);
    		throw new BackendException(e);
    	}

		//return null;
	}

	@Override
	public Catalog getCatalogFromName(String catName) throws BackendException {
		try{
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.getCatalogFromName(catName);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}
	@Override
	public void saveNewProduct(Product product) throws BackendException {
		try{
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.saveNewProduct(product, product.getCatalog());
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}
	@Override
	public void deleteProduct(Product product) throws BackendException {
		try{
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.deleteProduct(product);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}
	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
		try{
			DbClassCatalog dbclass = new DbClassCatalog();
			dbclass.deleteCatalog(catalog);
		}catch(DatabaseException e){
			throw new BackendException(e);
		}
	}

	/////////////////// For unit testing only ////////////////
	public DbClassProductForTest getGenericDbClassProduct() {
		return new DbClassProduct();
	}
	@Override
	public DbClassCatalogForTest getGenericDbClassCatalog() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DbClassCatalogForTest getGenericDbClassCatalogs() {
		// TODO Auto-generated method stub
		return new DbClassCatalog();
	}


}
