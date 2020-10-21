package assignment1;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Cloneable, Serializable{
	private String town;
	private String street;
	private String postcode;
	private int houseNum;
	
	public Address(String town, String street, String postcode, int houseNum) {
		super();
		this.town = town;
		this.street = street;
		this.postcode = postcode;
		this.houseNum = houseNum;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public int getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(int houseNum) {
		this.houseNum = houseNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + houseNum;
		result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (houseNum != other.houseNum)
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (town == null) {
			if (other.town != null)
				return false;
		} else if (!town.equals(other.town))
			return false;
		return true;
	}
	
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

	@Override
	public String toString() {
		return "Address [town=" + town + ", street=" + street + ", postcode=" + postcode + ", houseNum=" + houseNum
				+ "]";
	}	
	
}
