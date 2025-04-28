package es.us.lsi.dad;

import java.util.Objects;

public class sensorImpleNFC{
	protected Integer idNFC;
	protected Integer valor;
	protected Long fecha;
	protected Integer groupId;
	protected Boolean estado;
	
	
	public sensorImpleNFC(Integer idNFC, Integer valor, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idNFC = idNFC;
		this.valor = valor;
		this.fecha = fecha;
		this.groupId = groupId;
		this.estado = estado;
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
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fecha, groupId, idNFC, estado, valor);
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
		return Objects.equals(fecha, other.fecha) && Objects.equals(groupId, other.groupId)
				&& Objects.equals(idNFC, other.idNFC) && Objects.equals(estado, other.estado)
				&& Objects.equals(valor, other.valor);
	}

}
