package es.us.lsi.dad;

import java.util.Objects;

public class sensorImpleNFC{
	protected Integer idNFC;
	protected Integer value;
	protected Long date;
	protected Integer groupId;
	protected Boolean status;
	
	
	public sensorImpleNFC(Integer idNFC, Integer value, Long date, Integer groupId, Boolean status) {
		super();
		this.idNFC = idNFC;
		this.value = value;
		this.date = date;
		this.groupId = groupId;
		this.status = status;
	}
	
	public sensorImpleNFC() {
		super();
	}

	public Integer getIdNFC() {
		return idNFC;
	}

	public void setIdNFC(Integer idNFC) {
		this.idNFC = idNFC;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, groupId, idNFC, status, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		sensorImpleNFC other = (sensorImpleNFC) obj;
		return Objects.equals(date, other.date) && Objects.equals(groupId, other.groupId)
				&& Objects.equals(idNFC, other.idNFC) && Objects.equals(status, other.status)
				&& Objects.equals(value, other.value);
	}

}
