package business.ordersubsystem;

import business.externalinterfaces.OrderItem;


public class OrderItemImpl implements OrderItem {
	private int orderItemId;
	private int orderId;
	private String productName;
	private int productId;
	private int quantity;
	private double unitPrice;
	
	public OrderItemImpl(){
	}
	
	public OrderItemImpl(String name, int quantity, double price) {
		productName = name;
		this.quantity = quantity;
		this.unitPrice = price;
	}
	
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int itemID) {
		this.orderItemId = itemID;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderID) {
		this.orderId = orderID;
	}


	public String getProductName() {
		return productName;
	}
	public void setProductName(String n) {
		productName = n;
	}


	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int q) {
		quantity = q;
	}


	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double price) {
		unitPrice = price;
	}

	public double getTotalPrice() {
		return unitPrice * quantity;
	}


	@Override
	public int getProductId() {
		return productId;
	}

	@Override
	public void setProductId(int id) {
		productId = id;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderItemImpl [orderItemId=" + orderItemId + ", orderId=" + orderId + ", productName=" + productName
				+ ", productId=" + productId + ", quantity=" + quantity + ", unitPrice=" + unitPrice + "]";
	}

	

}
