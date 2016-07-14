package business.customersubsystem;

import java.util.ArrayList;
import java.util.List;

import middleware.creditverifcation.CreditVerificationFacade;
import middleware.exceptions.DatabaseException;
import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;
import middleware.externalinterfaces.CreditVerificationProfile;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;


public class CustomerSubsystemFacade implements CustomerSubsystem {
	ShoppingCartSubsystem shoppingCartSubsystem;
	OrderSubsystem orderSubsystem;
	List<Order> orderHistory;
	Address defaultShipAddress;
	Address defaultBillAddress;
	CreditCardImpl defaultPaymentInfo;
	CustomerProfileImpl customerProfile;
	
	/** Use for loading order history,
	 * default addresses, default payment info, 
	 * saved shopping cart,cust profile
	 * after login */
    public void initializeCustomer(Integer id, int authorizationLevel) throws BackendException {
	    boolean isAdmin = (authorizationLevel >= 1);
		loadCustomerProfile(id, isAdmin);
		loadDefaultShipAddress();
		loadDefaultBillAddress();
		loadDefaultPaymentInfo();
		shoppingCartSubsystem = ShoppingCartSubsystemFacade.INSTANCE;
		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.retrieveSavedCart();
		loadOrderData();
    }
    
    void loadCustomerProfile(int id, boolean isAdmin) throws BackendException {
    	try {
			DbClassCustomerProfile dbclass = new DbClassCustomerProfile();
			customerProfile = dbclass.readCustomerProfile(id);
			customerProfile.setIsAdmin(isAdmin);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
    }
    void loadDefaultShipAddress() throws BackendException {
    	try {
			DbClassAddress dbClass = new DbClassAddress();
			defaultShipAddress = dbClass.readDefaultShipAddress(customerProfile);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
    }
	void loadDefaultBillAddress() throws BackendException {
    	try {
			DbClassAddress dbClass = new DbClassAddress();
			defaultBillAddress = dbClass.readDefaultBillAddress(customerProfile);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
	}
	void loadDefaultPaymentInfo() throws BackendException {
		//implemented
		try {
			DbClassCreditCard dbClass = new DbClassCreditCard();
			defaultPaymentInfo = dbClass.getDefaultPaymentInfo(customerProfile);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	void loadOrderData() throws BackendException {
		// retrieve the order history for the customer and store here
		orderSubsystem = new OrderSubsystemFacade(customerProfile);
		orderHistory = orderSubsystem.getOrderHistory();
	}
	/**
	 * Customer Subsystem is responsible for obtaining all the data needed by
	 * Credit Verif system -- it does not (and should not) rely on the
	 * controller for this data.
	 */
	@Override
	public void checkCreditCard() throws BusinessException {
		List<CartItem> items = ShoppingCartSubsystemFacade.INSTANCE
				.getCartItems();
		ShoppingCart theCart = ShoppingCartSubsystemFacade.INSTANCE
				.getLiveCart();
		Address billAddr = theCart.getBillingAddress();
		CreditCard cc = theCart.getPaymentInfo();
		double amount = DataUtil.computeTotal(items);
		CreditVerification creditVerif = new CreditVerificationFacade();
		try {
			CreditVerificationProfile profile = CreditVerificationFacade.getCreditProfileShell();
			profile.setFirstName(customerProfile.getFirstName());
			profile.setLastName(customerProfile.getLastName());
			profile.setAmount(amount);
			profile.setStreet(billAddr.getStreet());
			profile.setCity(billAddr.getCity());
			profile.setState(billAddr.getState());
			profile.setZip(billAddr.getZip());
			profile.setCardNum(cc.getCardNum());
			profile.setExpirationDate(cc.getExpirationDate());
			creditVerif.checkCreditCard(profile);
		} catch (MiddlewareException e) {
			throw new BusinessException(e);
		}
	}
    /**
     * Returns true if user has admin access
     */
    public boolean isAdmin() {
    	return customerProfile.isAdmin();
    }
    
    /** 
     * Use for saving an address created by user  
     */
    public void saveNewAddress(Address addr) throws BackendException {
    	try {
			DbClassAddress dbClass = new DbClassAddress();
			dbClass.setAddress(addr);
			dbClass.saveAddress(customerProfile);
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}
    }
    
    public CustomerProfile getCustomerProfile() {
		return customerProfile;
	}

	public Address getDefaultShippingAddress() {
		return defaultShipAddress;
	}

	public Address getDefaultBillingAddress() {
		return defaultBillAddress;
	}
	public CreditCard getDefaultPaymentInfo() {
		return defaultPaymentInfo;
	}
 
    
    /** 
     * Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window 
	 */
    public List<Address> getAllAddresses() throws BackendException {
    	DbClassAddress dbClassAddress = new DbClassAddress();
		List<Address> addrList = null;
		try {
			
			addrList = dbClassAddress.readAllAddresses(customerProfile);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return addrList;
    }

	public Address runAddressRules(Address addr) throws RuleException,
			BusinessException {

		Rules transferObject = new RulesAddress(addr);
		transferObject.runRules();

		// updates are in the form of a List; 0th object is the necessary
		// Address
		AddressImpl update = (AddressImpl) transferObject.getUpdates().get(0);
		return update;
	}

	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		Rules transferObject = new RulesPayment(addr, cc);
		transferObject.runRules();
	}
	
	
	 public static Address createAddress(String street, String city,
				String state, String zip, boolean isShip, boolean isBill) {
			return new AddressImpl(street, city, state, zip, isShip, isBill);
		}

		public static CustomerProfile createCustProfile(Integer custid,
				String firstName, String lastName, boolean isAdmin) {
			return new CustomerProfileImpl(custid, firstName, lastName, isAdmin);
		}

		public static CreditCard createCreditCard(String nameOnCard,
				String expirationDate, String cardNum, String cardType) {
			return new CreditCardImpl(nameOnCard, expirationDate, cardNum, cardType);
		}

	@Override
	public List<Order> getOrderHistory() {
		// TODO Auto-generated method stub
		return orderHistory;
	}

	@Override
	public void setShippingAddressInCart(Address addr) {
		defaultShipAddress = (AddressImpl)addr;
	}

	@Override
	public void setBillingAddressInCart(Address addr) {
		defaultBillAddress = (AddressImpl)addr;
	}

	@Override
	public void setPaymentInfoInCart(CreditCard cc) {
		// TODO Auto-generated method stub
		defaultPaymentInfo = (CreditCardImpl) cc;
	}

	@Override
	public void submitOrder() throws BackendException {
		
		orderSubsystem.submitOrder(shoppingCartSubsystem.getLiveCart());
		refreshAfterSubmit();
	}

	@Override
	public void refreshAfterSubmit() throws BackendException {
		
		loadOrderData();
		shoppingCartSubsystem.clearLiveCart();
	}

	@Override
	public ShoppingCartSubsystem getShoppingCart() {
		
		return shoppingCartSubsystem;
	}

	@Override
	public void saveShoppingCart() throws BackendException {
		// TODO Auto-generated method stub
		shoppingCartSubsystem.saveLiveCart();
	}
	
	@Override
	public CustomerProfile getGenericCustomerProfile() {
		return new CustomerProfileImpl(1, "FirstTest", "LastTest");
	}
	
	@Override
	public DbClassAddressForTest getGenericDbClassAddress() {
		return new DbClassAddress();
	}
}
