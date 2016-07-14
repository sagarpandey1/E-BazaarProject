package dbsetup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import alltests.AllTests;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.DbConfigKey;

public class DbQueriesOrder {
	static {
		AllTests.initializeProperties();
	}
	static final DbConfigProperties PROPS = new DbConfigProperties();
	static Connection con = null;
	static Statement stmt = null;
	static final String USER = PROPS.getProperty(DbConfigKey.DB_USER.getVal());
    static final String PWD = PROPS.getProperty(DbConfigKey.DB_PASSWORD.getVal());
    static final String DRIVER = PROPS.getProperty(DbConfigKey.DRIVER.getVal());
    static final int MAX_CONN = Integer.parseInt(PROPS.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal()));
    static final String PROD_DBURL = PROPS.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    static final String ACCT_DBURL = PROPS.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	static Connection prodCon = null;
	static Connection acctCon = null;
    String insertStmt = "";
	String selectStmt = "";

	/* Connection setup */
	static {
		try {
			Class.forName(DRIVER);
		}
		catch(ClassNotFoundException e){
			//debug
			e.printStackTrace();
		}
		try {
			prodCon = DriverManager.getConnection(PROD_DBURL, USER, PWD);
			acctCon = DriverManager.getConnection(ACCT_DBURL, USER, PWD);
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	// just to test this class
	public static void testing() {
		try {
			stmt = prodCon.createStatement();
			stmt.executeQuery("SELECT * FROM Product");
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}


	public static List<Order> readAllOrderForCustomer() throws BackendException {
		String queryOrder = "SELECT orderid, orderdate, totalpriceamount FROM Ord WHERE custid = 1";

		List<Order> orderList = new LinkedList<Order>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(queryOrder);
                while(rs.next()) {
                	int orderid=rs.getInt("orderid");
    				String orderDate=rs.getString("orderdate");
    				Double totalPrice=rs.getDouble("totalpriceamount");

                    Order ordr
                      = OrderSubsystemFacade.createOrder(orderid,  String.valueOf(orderDate), String.valueOf(totalPrice) );

                    orderList.add(ordr);
                }
                stmt.close();
               for(Order o: orderList){
                	o.setOrderItems(getOrderItemByOrderID(o.getOrderId()));
                }


		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		return orderList;

	}

	public static List<OrderItem> getOrderItemByOrderID(Integer id) throws BackendException{
		String queryOrderItems = "SELECT * FROM OrderItem WHERE orderid = "+id;

		List<OrderItem> items= new ArrayList<OrderItem>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(queryOrderItems);
                while(rs.next()) {
                	Integer orderId= id;
                	int productid=rs.getInt("productid");
    	    		int quantity=rs.getInt("quantity");
    	    		double totalPrice=rs.getDouble("totalprice");


                    OrderItem ordrItem
                      = OrderSubsystemFacade.createFakeOrderITem("Pants",quantity, totalPrice);
                    		  items.add(ordrItem);
                }
                stmt.close();



		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		return items;
		}

	public static void deleteOrder(String totalPrice) throws BackendException{
		String queryselectOrderId = "SELECT orderid FROM ord WHERE  totalpriceamount= "+
	                            Double.parseDouble(totalPrice)+" and custid=1";

		System.out.println(queryselectOrderId);
		try {
			System.out.println("inside delete order...............");
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(queryselectOrderId);
			Integer orderId=0;
                while(rs.next()) {
                	orderId= rs.getInt("orderid");
                	System.out.println("gor orderid to delete="+orderId);
                }
                stmt.close();

                stmt = acctCon.createStatement();
                String deleteOrder= "DELETE FROM ord where orderid="+orderId;
                stmt.executeUpdate(deleteOrder);
                stmt.close();

                stmt = acctCon.createStatement();
                String deleteOrderItems= "DELETE FROM OrderItem WHERE orderid = "+orderId;
                stmt.executeUpdate(deleteOrderItems);
                stmt.close();


                stmt.close();



		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		}
	}
