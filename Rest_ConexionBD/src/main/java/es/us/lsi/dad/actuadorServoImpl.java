package es.us.lsi.dad;

import java.util.Objects;

public class actuadorServoImpl {
	Integer idServo;
	Integer valor;
	Long date;
	Integer groupId;
	Boolean Status;
	
	public actuadorServoImpl(Integer idServo, Integer valor, Long date, Integer groupId, Boolean status) {
		super();
		this.idServo = idServo;
		this.valor = valor;
		this.date = date;
		this.groupId = groupId;
		Status = status;
	}
	
	public actuadorServoImpl() {
		super();
	}

	public Integer getIdServo() {
		return idServo;
	}

	public void setIdServo(Integer idServo) {
		this.idServo = idServo;
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
		return Status;
	}

	public void setStatus(Boolean status) {
		Status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Status, date, groupId, idServo, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		actuadorServoImpl other = (actuadorServoImpl) obj;
		return Objects.equals(Status, other.Status) && Objects.equals(date, other.date)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idServo, other.idServo)
				&& Objects.equals(valor, other.valor);
	}

	
	
	
	
	
}
