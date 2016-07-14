package business.ordersubsystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ShoppingCart;
import business.util.Convert;

public class OrderSubsystemFacade implements OrderSubsystem {
	private static final Logger LOG =
			Logger.getLogger(OrderSubsystemFacade.class.getPackage().getName());
	CustomerProfile custProfile;
	List<Order> orderList;

    public OrderSubsystemFacade(CustomerProfile custProfile){
        this.custProfile = custProfile;
    }

	/**
     *  Used by customer subsystem at login to obtain this customer's order history from the database.
	 *  Assumes cust id has already been stored into the order subsystem facade
	 *  This is created by using auxiliary methods at the bottom of this class file.
	 *  First get all order ids for this customer. For each such id, get order data
	 *  and form an order, and with that order id, get all order items and insert
	 *  into the order.
	 */
    public List<Order> getOrderHistory() {
    	//implemented
    	//LOG.warning("Method getOrderHistory() still needs to be implemented");

    	orderList = new ArrayList<>();

    	try {
			List<Integer> orderIdList = this.getAllOrderIds();

			for(int orderId : orderIdList){
				Order order = getOrderData(orderId);
				orderList.add(order);
			}

		} catch (DatabaseException e) {
			LOG.warning("incorrect information from database" + e);
			e.printStackTrace();
		}

    	return orderList;

    }

    public void submitOrder(ShoppingCart cart) throws BackendException {
    	//implement
    	//LOG.warning("The method submitOrder(ShoppingCart cart) in OrderSubsystemFacade has not been implemented");
    	List<CartItem> cartItems = cart.getCartItems();
    	OrderImpl order = new OrderImpl();
		double totalPrice = 0;
    	for(CartItem cartItem : cartItems){
    		OrderItem orderItem = new OrderItemImpl();
    		orderItem.setProductName(cartItem.getProductName());
    		orderItem.setProductId(cartItem.getProductid());
			orderItem.setQuantity(Integer.parseInt(cartItem.getQuantity()));
			orderItem.setUnitPrice(Double.parseDouble(cartItem.getTotalprice()));
			order.addOrderItem(orderItem);

			totalPrice += orderItem.getTotalPrice();
    	}

    	order.setTotalPrice(totalPrice);

    	order.setBillAddress(cart.getBillingAddress());
    	order.setShipAddress(cart.getShippingAddress());
    	order.setPaymentInfo(cart.getPaymentInfo());
    	order.setDate(LocalDate.now());

    	DbClassOrder orderDb = new DbClassOrder();
		try {
			orderDb.submitOrder(custProfile,order);
		} catch (DatabaseException e) {
			LOG.warning("incorrect information from database" + e);
			throw new BackendException(e);
		}
    }

	/** Used whenever an order item needs to be created from outside the order subsystem */
    public static OrderItem createOrderItem(
    		Integer prodId, Integer orderId, String quantityReq, String totalPrice) {
    	//implement
        //LOG.warning("Method createOrderItem(prodid, orderid, quantity, totalprice) still needs to be implemented");
    	//return null;

    	OrderItem orderItem = new OrderItemImpl();
		orderItem.setProductId(prodId);
		orderItem.setOrderId(orderId);
		orderItem.setQuantity(Integer.parseInt(quantityReq));
		orderItem.setUnitPrice(Double.parseDouble(totalPrice));

		return orderItem;
    }

    /** to create an Order object from outside the subsystem */
    public static Order createOrder(Integer orderId, String orderDate, Double totalPrice) {
    	//implemented
        //LOG.warning("Method  createOrder(Integer orderId, String orderDate, String totalPrice) still needs to be implemented");
    	//return null;
    	OrderImpl order = new OrderImpl();
    	order.setOrderId(orderId);
    	order.setDate(Convert.localDateForString(orderDate));
    	order.setTotalPrice(totalPrice);

    	return order;
    }

    ///////////// Methods internal to the Order Subsystem -- NOT public
    List<Integer> getAllOrderIds() throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getAllOrderIds(custProfile);

    }

    /** Part of getOrderHistory */
    List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
        DbClassOrder dbClass = new DbClassOrder();
        return dbClass.getOrderItems(orderId);
    }

    /** Uses cust id to locate top-level order data for customer -- part of getOrderHistory */
    Order getOrderData(Integer orderId) throws DatabaseException {
    	List<Integer> orderIdList = this.getAllOrderIds();

    	DbClassOrder dbClass = new DbClassOrder();
    	Order order = dbClass.getOrderData(orderId);
    	order.setOrderId(orderId);
    	order.setOrderItems(getOrderItems(orderId));

    	return order;
    }

	@Override
	public DbClassOrderForTest getGenericDbClassOrder() {
		// TODO Auto-generated method stub
		return new DbClassOrder();
	}

	public static Order createOrder(int orderid, String orderDate, String totalPrice) {
		// TODO Auto-generated method stub
		LocalDate localDate=null;
		Date convertedDate=null;
		 DateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
		 try {
			 convertedDate =(Date) formatter.parse(orderDate);
			 localDate = convertedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 double price= Double.parseDouble(totalPrice);
		 return new OrderImpl(orderid,localDate,price);
	}


	 public static Order createFakeGenericOrder(){
	    	OrderImpl order=new OrderImpl();
	    	order.setDate(LocalDate.now());
	        OrderItem item = createFakeOrderITem("Pants",1,10000.123);
	        List<OrderItem> items= new ArrayList<OrderItem>();
	         items.add(item);
	         order.setOrderItems(items);

	         Address shipAddr= CustomerSubsystemFacade.createAddress("100", "fakeShip", "test", "test", true, false);
	         Address billAddr= CustomerSubsystemFacade.createAddress("100", "fakeBill", "test", "test", false, true);
	         CreditCard cc= CustomerSubsystemFacade.createCreditCard("Nabin", "2050/02/02", "1234567891234567", "VISA");

	         order.setBillAddress(billAddr);
	         order.setShipAddress(shipAddr);
	         order.setPaymentInfo(cc);


	    	return order;
	    }

	public static OrderItem createFakeOrderITem(String name, int quantity, double totalPrice) {
		// TODO Auto-generated method stub
		return new OrderItemImpl(name, quantity, totalPrice);
	}

	 public static DbClassOrderForTest createGenericDbClassOrderForTest(){
	    	return new DbClassOrder();
	    }
}
