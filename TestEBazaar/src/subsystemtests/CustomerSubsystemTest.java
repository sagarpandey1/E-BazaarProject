package subsystemtests;

import java.util.List;
import java.util.stream.Collectors;

import dbsetup.DbQueries;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.util.DataUtil;
import junit.framework.TestCase;

public class CustomerSubsystemTest extends TestCase {

	public void testReadDefaultShipAddress() {

		List<Address> listAddr = DbQueries.readCustAddresses();



		Address expected = CustomerSubsystemFacade.createAddress("1000 N. 4th St.", "Fairfield", "IA", "52557", false, false);

		CustomerSubsystem css = new CustomerSubsystemFacade();

		DbClassAddressForTest dbclass = css.getGenericDbClassAddress();

		CustomerProfile custProfile = css.getGenericCustomerProfile();

		try {
			Address found = dbclass.readDefaultBillAddress(custProfile);

			assertTrue(expected.equals(found));

		} catch(Exception e) {
			System.out.println("Address Lists don't match");
		}
	}

	public void testSavingNewAddress(){
		//List<Address> listAddr = DbQueries.readCustAddresses();
		Address newAddr = CustomerSubsystemFacade.createAddress("1000 N. 4th St.", "Fairfield", "IA", "52558", false, false);

		CustomerSubsystem css = new CustomerSubsystemFacade();
		try {

			css.saveNewAddress(newAddr);
			List<Address> listAddr = DbQueries.readCustAddresses();
			List<String> zipList = listAddr.stream()
					.map(z -> z.getZip())
					.collect(Collectors.toList());

			boolean found = false;
			for(String zip : zipList) {

				if(zip.equals("52558"))
					found = true;

			}
		assertTrue(found);

		} catch (BackendException e) {

			e.printStackTrace();
		}
	}
}
