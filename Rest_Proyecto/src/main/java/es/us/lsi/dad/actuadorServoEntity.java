package es.us.lsi.dad;

import java.util.Objects;

public class actuadorServoEntity {
	Integer idServo;
	Integer valor;
	Long fecha;
	Integer groupId;
	Boolean estado;
	public actuadorServoEntity(Integer idServo, Integer valor, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idServo = idServo;
		this.valor = valor;
		this.fecha = fecha;
		this.groupId = groupId;
		this.estado = estado;
	}
	
	public actuadorServoEntity() {
	}
	
	public Integer getIdServo() {
		return idServo;
	}
	public void setIdServo(Integer idServo) {
		this.idServo = idServo;
	}
	public Integer getValor() {
		return valor;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public Long getFecha() {
		return fecha;
	}
	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
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
		actuadorServoEntity other = (actuadorServoEntity) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idServo, other.idServo)
				&& Objects.equals(valor, other.valor);
	}
	

	
}
