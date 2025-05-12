package es.us.lsi.dad;

import java.util.Objects;

public class actuadorLedImpl {
	Integer idled;
	Double nivel_luz;
	Long fecha;
	Integer groupId;
	Boolean estado;
	public actuadorLedImpl(Integer  idled, Double nivel_luz, Long fecha, Integer groupId, Boolean estado) {
		super();
		this.idled = idled;
		this.nivel_luz = nivel_luz;
		this.fecha = fecha;
		this.groupId = groupId;
		this.estado = estado;
	}	

	public actuadorLedImpl() {
		super();
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
		actuadorLedImpl other = (actuadorLedImpl) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(groupId, other.groupId) && Objects.equals(idled, other.idled)
				&& Objects.equals(nivel_luz, other.nivel_luz);
	}
	
	

}

