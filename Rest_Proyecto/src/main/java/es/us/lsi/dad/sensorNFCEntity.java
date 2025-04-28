package es.us.lsi.dad;

import java.util.Objects;

public class sensorNFCEntity{
	protected Integer idNFC;
	protected Integer valor;
	protected Long date;
	protected Integer groupId;
	protected Boolean status;
	
	
	public sensorNFCEntity(Integer idNFC, Integer valor, Long date, Integer groupId, Boolean status) {
		super();
		this.idNFC = idNFC;
		this.valor = valor;
		this.date = date;
		this.groupId = groupId;
		this.status = status;
	}
	
	public sensorNFCEntity() {
		super();
	}

	public Integer getIdNFC() {
		return idNFC;
	}

	public void setIdNFC(Integer idNFC) {
		this.idNFC = idNFC;
	}

	public Integer getvalor() {
		return valor;
	}

	public void setvalor(Integer valor) {
		this.valor = valor;
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
		return Objects.hash(date, groupId, idNFC, status, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		sensorNFCEntity other = (sensorNFCEntity) obj;
		return Objects.equals(date, other.date) && Objects.equals(groupId, other.groupId)
				&& Objects.equals(idNFC, other.idNFC) && Objects.equals(status, other.status)
				&& Objects.equals(valor, other.valor);
	}

	
}
