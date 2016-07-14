package daotests;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.util.Convert;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import middleware.exceptions.DatabaseException;

public class DbClassProductTest extends TestCase {

	static String name = "Add new Product Test";
	static Logger log = Logger.getLogger(DbClassProductTest.class.getName());

	static {
		AllTests.initializeProperties();
	}


	public void testSaveNewProduct() throws BackendException, DatabaseException {
		ProductSubsystemFacade css = new ProductSubsystemFacade();
		Catalog catalog=ProductSubsystemFacade.createCatalog(0, "test");
		LocalDate dt=Convert.localDateForString("02/02/2016");
		Product prod=ProductSubsystemFacade.createProduct(catalog,0, "TestProduct", 10, 20,dt,"This is a test product");

		//List<Address> expected = DbQueries.readCustAddresses();

		//test real dbclass address

		DbClassProductForTest dbProd= css.getGenericDbClassProduct();

		//css.saveNewProduct(prod);
		dbProd.saveNewProduct(prod, catalog);


		try{

			List<Product> products=css.getProductList(catalog);
			Product result=products.get(0);
			assertTrue(result.getCatalog().getId()== catalog.getId()
					&& result.getProductName().equals(prod.getProductName() )
					&& result.getMfgDate().equals(prod.getMfgDate())
					&& result.getQuantityAvail()== prod.getQuantityAvail()
					&& result.getUnitPrice()==prod.getUnitPrice()
					&& result.getDescription().equals(prod.getDescription()));
		}
		catch(Exception e){
			fail("Products Lists don't match");
		}

		//cleanup
		//DbQueries.deleteProductRowBuCatalogId(catalog.getId());
//		css.getProductFromId(prodId);
//		DbClassAddressForTest dbclass = css.getGenericDbClassAddress();
//		CustomerProfile custProfile = css.getGenericCustomerProfile();

//		try {
//			List<Address> found = dbclass.readAllAddresses(custProfile);
//
//			assertTrue(expected.size() == found.size());
//
//		} catch(Exception e) {
//			fail("Address Lists don't match");
//		}

	}
}
