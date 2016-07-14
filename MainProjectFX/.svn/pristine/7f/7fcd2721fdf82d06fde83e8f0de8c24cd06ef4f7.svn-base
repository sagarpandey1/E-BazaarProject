
package business.ordersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.exceptions.ParseException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.util.Convert;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;


class DbClassOrder implements DbClass, DbClassOrderForTest {
	enum Type {GET_ORDER_ITEMS, GET_ORDER_IDS, GET_ORDER_DATA, 
		SUBMIT_ORDER, SUBMIT_ORDER_ITEM};
	
	private static final Logger LOG = 
		Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = 
    	new DataAccessSubsystemFacade();
    
    private Type queryType;
    
    private String orderItemsQuery = "SELECT * FROM OrderItem WHERE orderid = ?";
    private String orderIdsQuery = "SELECT orderid FROM Ord WHERE custid = ?";
    private String orderDataQuery = "SELECT orderdate, totalpriceamount FROM Ord WHERE orderid = ?";
    private String submitOrderQuery = "INSERT into Ord "+
        "(custid, shipaddress1, shipcity, shipstate, shipzipcode, billaddress1, billcity, billstate,"+
           "billzipcode, nameoncard,  cardnum,cardtype, expdate, orderdate, totalpriceamount) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
    private String submitOrderItemQuery = "INSERT into orderitem " + 
        "(orderid, productid, quantity, totalprice, shipmentcost, taxamount) " +
    		"VALUES(?,?,?,?,?,?)";
    Object[] orderItemsParams, orderIdsParams, orderDataParams, orderHistoryParams, 
    			submitOrderParams, submitOrderItemParams;
    int[] orderItemsTypes, orderIdsTypes, orderDataTypes, orderHistoryTypes, 
    			submitOrderTypes, submitOrderItemTypes;
    
    //This is set by submitOrder and then used by submitOrderData
    private CustomerProfile custProfile;
    private List<Integer> orderIds;
    private List<OrderItem> orderItems;
    private OrderImpl orderData;
    private Order order;
    private Integer orderId;
    
    
    DbClassOrder(){}
    
    List<Integer> getAllOrderIds(CustomerProfile custProfile) throws DatabaseException {
        queryType = Type.GET_ORDER_IDS;
        orderIdsParams = new Object[]{custProfile.getCustId()};
        orderIdsTypes = new int[]{Types.INTEGER};
        dataAccessSS.atomicRead(this);
        return Collections.unmodifiableList(orderIds);      
    }
    
    Order getOrderData(Integer orderId) throws DatabaseException {
    	queryType = Type.GET_ORDER_DATA;
    	orderDataParams = new Object[]{orderId};
    	orderDataTypes = new int[]{Types.INTEGER};
    	dataAccessSS.atomicRead(this);      	
        return orderData;
    }
    
    /**
	 *  This method submits top-level data in Order to the Ord table (this is
	 *  executed within the helper method submitOrderData)
	 *  and then, after it gets the order id, it submits each OrderItem from
	 *  Order to the OrderItem table (items are submitted one at a time
	 *  using submitOrderItem). All this is done within a transaction.
	 *  Separate methods are provided 
     */
    void submitOrder(CustomerProfile custProfile, Order order) throws DatabaseException {
    	//LOG.warning("Method submitOrder(CustomerProfile custProfile, Order order) has not beenimplemented");
    	//implemented
    	
    	this.order = order;
    	this.custProfile = custProfile;
    	
    	dataAccessSS.establishConnection(this);
    	dataAccessSS.startTransaction();
    	try{
    		Integer orderId = submitOrderData();
        	List<OrderItem> orderItems = order.getOrderItems();
        	for(OrderItem orderItem : orderItems){
        		orderItem.setOrderId(orderId);
        		submitOrderItem(orderItem);
        	}
        	dataAccessSS.commit();
    	}catch(DatabaseException e){
    		LOG.warning("Database error, rolling back...");
    		dataAccessSS.rollback();
    		throw (e);
    	}finally{
    		dataAccessSS.releaseConnection();
    	}
    	
    	
    }
	    
    /** This is part of the general submitOrder method */
	private Integer submitOrderData() throws DatabaseException {	
		queryType = Type.SUBMIT_ORDER;
		Address shipAddr = order.getShipAddress();
        Address billAddr = order.getBillAddress();
        CreditCard cc = order.getPaymentInfo();
    	submitOrderParams = new Object[]{custProfile.getCustId(),
    	                  shipAddr.getStreet(),
    	                  shipAddr.getCity(),
    	                  shipAddr.getState(),
    	                  shipAddr.getZip(),
    	                  billAddr.getStreet(),
    	                  billAddr.getCity(),
    	                  billAddr.getState(),
    	                  billAddr.getZip(),
    	                  cc.getNameOnCard(),
    	                  cc.getCardNum(),
    	                  cc.getCardType(),
    	                  cc.getExpirationDate(),
    	                  Convert.localDateAsString(order.getOrderDate()),
    	                  order.getTotalPrice()};
    	
    	submitOrderTypes = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//shipping
    			Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//billing
    			Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,//cc
    			Types.VARCHAR, Types.DOUBLE};
		//creation and release of connection handled by submitOrder
    	//this should be part of a transaction started in submitOrder
    	return dataAccessSS.insert();
	}
	
	/** This is part of the general submitOrder method */
	private void submitOrderItem(OrderItem item) throws DatabaseException {
        queryType=Type.SUBMIT_ORDER_ITEM;
        //LOG.warning("Method submitOrderItem(OrderItem item) in DbClassOrder has not been implemented.");
        //implemented
        submitOrderItemParams = new Object[]{item.getOrderId(), item.getProductId(), 
        								item.getQuantity(), item.getTotalPrice(), 2.0, 3.0};
        
        submitOrderItemTypes = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, 
        			Types.DOUBLE, Types.DOUBLE, Types.DOUBLE};
        
        //creation and release of connection handled by submitOrder
        //this should be part of a transaction started in submitOrder
       dataAccessSS.insert();        
	}
   
    public List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
    	//implemented 
//    	LOG.warning("Method getOrderItems(Integer orderId) has not been implmeented");
    	
    	queryType = Type.GET_ORDER_ITEMS;
    	orderItemsParams = new Object[]{orderId};
    	orderItemsTypes = new int[]{Types.INTEGER};
    	dataAccessSS.atomicRead(this);
    	
    	return Collections.unmodifiableList(orderItems);
    }
   
    private void populateOrderItems(ResultSet rs) throws DatabaseException {
    	//LOG.warning("Method populateOrderItems(ResultSet) still needs to be implemented");
       //implemented
    	
    	orderItems = new ArrayList<OrderItem>();
    	
    	try{
    		while(rs.next()){
        		int productId = rs.getInt("productid");
        		int quantity = rs.getInt("quantity");
        		double totalPrice = rs.getDouble("totalPrice");
        		
        		ProductSubsystem pss = new ProductSubsystemFacade();
        		
        		String productName = pss.getProductFromId(productId).getProductName();
        		OrderItem nextItem = new OrderItemImpl(productName, quantity, totalPrice/quantity);
        		
        		orderItems.add(nextItem);
        		
        	}
    		
    	}catch(SQLException e){
    		throw new DatabaseException(e);
    	} catch (BackendException e) {
			e.printStackTrace();
		}
    	
    }
    
    private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
        orderIds = new LinkedList<Integer>();
        try {
            while(resultSet.next()){
                orderIds.add(resultSet.getInt("orderid"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }
    
    private void populateOrderData(ResultSet resultSet) throws DatabaseException { 
    	//implemented
    	//LOG.warning("Method populateOrderData(ResultSet resultSet) still needs to be implemented");

    	try {
			while (resultSet.next()) {
				//int orderId = resultSet.getInt("orderid");
				String date = resultSet.getString("orderdate");
				double totalPrice = resultSet.getDouble("totalpriceamount");
				orderData = new OrderImpl();
				if(date!=null)
					orderData.setDate(Convert.localDateForString(date));
				orderData.setTotalPrice(totalPrice);
				
			}
		} catch (SQLException e) {
			LOG.warning("failed to connection database"+ e);
			throw new DatabaseException(e);
		}
    }    
 
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
    	switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		populateOrderItems(resultSet);
	    		break;
	    	case GET_ORDER_IDS:
	    		populateOrderIds(resultSet);
	    		break;
	    	case GET_ORDER_DATA :
	        	populateOrderData(resultSet);
	        	break;
	        default :
	        	//do nothing
    	}
    }
    
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    }
    
    public String getQuery() {
    	switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsQuery;
	    	case GET_ORDER_IDS:
	    		return orderIdsQuery;
	    	case GET_ORDER_DATA :
	        	return orderDataQuery;
	    	case SUBMIT_ORDER:
	    		return submitOrderQuery;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemQuery;
	        default :
	        	return null;
		}
    }

	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsParams;
	    	case GET_ORDER_IDS:
	    		return orderIdsParams;
	    	case GET_ORDER_DATA :
	        	return orderDataParams;
	    	case SUBMIT_ORDER:
	    		return submitOrderParams;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemParams;
	        default :
	        	return null;
		}
	}

	@Override
	public int[] getParamTypes() {
		switch(queryType) {
	    	case GET_ORDER_ITEMS:
	    		return orderItemsTypes;
	    	case GET_ORDER_IDS:
	    		return orderIdsTypes;
	    	case GET_ORDER_DATA :
	        	return orderDataTypes;
	    	case SUBMIT_ORDER:
	    		return submitOrderTypes;
	    	case SUBMIT_ORDER_ITEM:
	    		return submitOrderItemTypes;
	        default :
	        	return null;
		}
	}

	@Override
	public void submitOrderForTest(CustomerProfile custProfile, Order order) {
		// TODO Auto-generated method stub
		try {
			submitOrder(custProfile,order);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Integer> readOrderHistory(CustomerProfile customer) {
		// TODO Auto-generated method stub
		
		try {
			return getAllOrderIds(customer);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}
   
     
    
}
