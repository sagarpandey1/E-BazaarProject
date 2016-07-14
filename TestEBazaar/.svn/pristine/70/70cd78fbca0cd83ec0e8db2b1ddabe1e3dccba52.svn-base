package daotests;

import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.ordersubsystem.OrderSubsystemFacade;
import dbsetup.DbQueriesOrder;
import junit.framework.TestCase;

public class DbClassOrderTest extends TestCase {
	static String name = "Browse and Select Test";
	static Logger log = Logger.getLogger(DbClassAddressTest.class.getName());

	static {
		AllTests.initializeProperties();
	}

	public void testSubmitOrder(){
		 try {
			 List<Order> before =DbQueriesOrder.readAllOrderForCustomer();
				CustomerProfile customer =CustomerSubsystemFacade.createCustProfile(1,
						"Test", "Test",false);
				Order ord= OrderSubsystemFacade.createFakeGenericOrder();
				DbClassOrderForTest dbClass= OrderSubsystemFacade.createGenericDbClassOrderForTest();
				dbClass.submitOrderForTest(customer, ord);
				List<Order> after =DbQueriesOrder.readAllOrderForCustomer();

				 System.out.println("before size="+before.size());
			     System.out.println("after size="+after.size());


				 assertTrue(before.size()+1==after.size());
				 DbQueriesOrder.deleteOrder("10000.123");

			} catch(Exception e) {
				fail("Some error Occur "+e.getMessage());
			}
	}
}
