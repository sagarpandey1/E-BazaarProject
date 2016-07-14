package presentation.data;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.usecasecontrol.ViewOrdersController;

public enum ViewOrdersData {
	INSTANCE;
	private static final Logger LOG = 
		Logger.getLogger(ViewOrdersData.class.getSimpleName());
	private OrderPres selectedOrder;
	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}
	
	public List<OrderPres> getOrders() {
		//LOG.warning("ViewOrdersData method getOrders() has not been implemented.");
		
		List<OrderPres> orderPresList = new ArrayList<>();
		
		SessionCache context = SessionCache.getInstance();
		CustomerSubsystem customer = (CustomerSubsystem)context.get(BusinessConstants.CUSTOMER);

		List<Order> orders;
		
			orders = ViewOrdersController.INSTANCE.getOrderHistory(customer);//orderSS.getOrderHistory();
			for(Order order : orders){
				selectedOrder = new OrderPres();
				selectedOrder.setOrder(order);
				orderPresList.add(selectedOrder);
			}
		
		return orderPresList;
		//return DefaultData.ALL_ORDERS;
	}
}
