package business.customersubsystem;

import business.externalinterfaces.Address;

public class AddressImpl implements Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	private boolean isShippingAddress = false;
	private boolean isBillingAddress = false;
	
	public AddressImpl(String str, String c, String state, String zip, 
			boolean isShip, boolean isBill) {
		street=str;
		city=c;
		this.state=state;
		this.zip=zip;
		isShippingAddress = isShip;
		isBillingAddress = isBill;
	}
	public AddressImpl() {}
	public boolean isShippingAddress() {
		return isShippingAddress;
	}
	
	public boolean isBillingAddress() {
		return isBillingAddress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	@Override
	public String toString() {
		return "AddressImpl [street=" + street + ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AddressImpl))
			return false;
		if(obj == null) 
			return false;
		AddressImpl address = (AddressImpl) obj;
		return (address.street.equals(this.street) &&
				address.city.equals(this.city) &&
				address.state.equals(this.state) &&
				address.zip.equals(this.zip) &&
				address.isBillingAddress == this.isBillingAddress &&
				address.isShippingAddress == this.isShippingAddress);
	}
	
}
