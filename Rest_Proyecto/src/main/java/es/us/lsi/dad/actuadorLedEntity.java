package es.us.lsi.dad;

import java.util.Objects;

public class actuadorLedEntity {
	Integer idled;
	Double nivel_luz;
	Long fecha;
	Integer groupId;
	Boolean estado;
	public actuadorLedEntity(Integer idled, Double nivel_luz, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idled = idled;
		this.nivel_luz = nivel_luz;
		this.fecha = fecha;
		this.groupId = groupId;
		this.estado = estado;
	}
	
	public actuadorLedEntity() {
		super();
	}
	@Override
	public int hashCode() {
		return Objects.hash(estado, fecha, groupId, idled, nivel_luz);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		actuadorLedEntity other = (actuadorLedEntity) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idled, other.idled)
				&& Objects.equals(nivel_luz, other.nivel_luz);
	}
	public Integer getIdled() {
		return idled;
	}
	public void setIdled(Integer idled) {
		this.idled = idled;
	}
	public Double getNivel_luz() {
		return nivel_luz;
	}
	public void setNivel_luz(Double nivel_luz) {
		this.nivel_luz = nivel_luz;
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
	

}

