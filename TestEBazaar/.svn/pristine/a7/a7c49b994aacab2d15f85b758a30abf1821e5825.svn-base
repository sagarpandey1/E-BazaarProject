package subsystemtests;
import java.util.List;
import java.util.logging.Logger;

import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.ordersubsystem.OrderSubsystemFacade;
import dbsetup.DbQueriesOrder;
import junit.framework.TestCase;

public class OrderSubsystemTest extends TestCase{
	static String name = "Order Subsystem Test";
	static Logger log = Logger.getLogger(OrderSubsystemTest.class.getName());

	static {
		AllTests.initializeProperties();
	}
	public void testgetOrderHistory() throws BackendException{
		try{
			System.out.println("inside testinggetorderHistory..........");
			List<Order> expected =DbQueriesOrder.readAllOrderForCustomer();
			System.out.println("Pass Expected gets..........");
			CustomerProfile customer =CustomerSubsystemFacade.createCustProfile(1,
					"Test", "Test",false);
			OrderSubsystem oss = new OrderSubsystemFacade(customer);
			List<Order> result=oss.getOrderHistory();
	       System.out.println("Expected size="+expected.size());
	      System.out.println("result size="+result.size());
			assertTrue(expected.size()==expected.size());
		}
		catch(Exception e){
			fail("Some error Occur "+e.getMessage());
		}

	}



}
