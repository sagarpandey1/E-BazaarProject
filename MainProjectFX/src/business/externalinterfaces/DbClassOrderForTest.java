package business.externalinterfaces;

import java.util.List;

import middleware.externalinterfaces.DbClass;

public interface DbClassOrderForTest extends DbClass {

	public void submitOrderForTest(CustomerProfile customer, Order order);
	public List<Integer> readOrderHistory(CustomerProfile customer);
}
