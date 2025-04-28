package es.us.lsi.dad;

import java.util.Objects;

public class sensorNFCEntity{
	protected Integer idNFC;
	protected Integer valor;
	protected Long fecha;
	protected Integer groupId;
	protected Boolean estado;
	public sensorNFCEntity(Integer idNFC, Integer valor, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idNFC = idNFC;
		this.valor = valor;
		this.fecha = fecha;
		this.groupId = groupId;
		this.estado = estado;
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
		return Objects.hash(estado, fecha, groupId, idNFC, valor);
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
		return Objects.equals(estado, other.estado) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idNFC, other.idNFC)
				&& Objects.equals(valor, other.valor);
	}
	
	
	
	
	
}
