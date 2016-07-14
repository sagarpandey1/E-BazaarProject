package business.usecasecontrol;

import java.util.List;
import java.util.logging.Logger;

import business.BusinessConstants;
import business.SessionCache;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;

public enum CheckoutController  {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	CustomerSubsystem cust =DataUtil.readCustFromCache();;
	
	private CheckoutController(){
		cust = DataUtil.readCustFromCache();
		if(cust == null){
			cust = new CustomerSubsystemFacade();
		}
	}
	
	public void runShoppingCartRules() throws RuleException, BusinessException {
		//implemented
		
		ShoppingCartSubsystemFacade.INSTANCE.runShoppingCartRules();
		
	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		//implemented
		CustomerSubsystem temp = DataUtil.readCustFromCache();
		if(temp != null && temp != cust) cust = temp;
		cust.runPaymentRules(addr, cc);
	}
	
	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem temp = DataUtil.readCustFromCache();
		if(temp != null && temp != cust) cust = temp;
		return cust.runAddressRules(addr);
	}
	
	public List<Address> getShippingAddresses(CustomerProfile custProf) throws BackendException {
		//implemented
		//LOG.warning("Method CheckoutController.getShippingAddresses has not been implemented");
		//return new ArrayList<Address>();
		CustomerSubsystem temp = DataUtil.readCustFromCache();
		if(temp != null && temp != cust) cust = temp;
		return cust.getAllAddresses();
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		//implemented
		
		scss.runFinalOrderRules();
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard() throws BusinessException {
		//implemented
		CustomerSubsystem temp = DataUtil.readCustFromCache();
		if(temp != null && temp != cust) cust = temp;
		cust.checkCreditCard();
	}
	
	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem temp = DataUtil.readCustFromCache();
		if(temp != null && temp != cust) cust = temp;
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		//implemented
		ShoppingCartSubsystemFacade.INSTANCE.deleteSavedCart();
		cust.submitOrder();
	}


}
