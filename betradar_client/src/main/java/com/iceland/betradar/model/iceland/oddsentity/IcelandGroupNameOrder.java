package com.iceland.betradar.model.iceland.oddsentity;

public class IcelandGroupNameOrder {
	private Integer groupOrder;
	private String groupName;

	public Integer getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(Integer groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		IcelandGroupNameOrder that = (IcelandGroupNameOrder) o;

		if(!groupOrder.equals(that.groupOrder)) return false;
		return groupName.equals(that.groupName);

	}

	@Override
	public int hashCode() {
		int result = groupOrder.hashCode();
		result = 31 * result + groupName.hashCode();
		return result;
	}
}
