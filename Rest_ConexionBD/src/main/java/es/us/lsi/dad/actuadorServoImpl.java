package es.us.lsi.dad;

import java.util.Objects;

public class actuadorServoImpl {
	Integer idServo;
	Integer valor;
	Long fecha;
	Integer groupId;
	Boolean estado;
	
	public actuadorServoImpl(Integer idServo, Integer valor, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idServo = idServo;
		this.valor = valor;
		this.fecha = fecha;
		this.groupId = groupId;
		estado = estado;
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

	public Long getfecha() {
		return fecha;
	}

	public void setfecha(Long fecha) {
		this.fecha = fecha;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Boolean getestado() {
		return estado;
	}

	public void setestado(Boolean estado) {
		estado = estado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(estado, fecha, groupId, idServo, valor);
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
		return Objects.equals(estado, other.estado) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idServo, other.idServo)
				&& Objects.equals(valor, other.valor);
	}

	@Override
	public String toString() {
		return "actuadorServoImpl [idServo=" + idServo + ", valor=" + valor + ", fecha=" + fecha + ", groupId="
				+ groupId + ", estado=" + estado + "]";
	}

	
	
	
	
	
}
