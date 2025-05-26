package es.us.lsi.dad;

import java.util.Objects;

public class sensorImpleNFC{
	protected Integer idNFC;
	protected Integer valor;
	protected Long fecha;
	public sensorImpleNFC() {
		super();
		// TODO Auto-generated constructor stub
	}
	public sensorImpleNFC(Integer idNFC, Integer valor, Long fecha) {
		super();
		this.idNFC = idNFC;
		this.valor = valor;
		this.fecha = fecha;
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
	@Override
	public int hashCode() {
		return Objects.hash(fecha, idNFC, valor);
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
		return Objects.equals(fecha, other.fecha) && Objects.equals(idNFC, other.idNFC)
				&& Objects.equals(valor, other.valor);
	}
	@Override
	public String toString() {
		return "sensorImpleNFC [idNFC=" + idNFC + ", valor=" + valor + ", fecha=" + fecha + "]";
	}

	

	
	
}