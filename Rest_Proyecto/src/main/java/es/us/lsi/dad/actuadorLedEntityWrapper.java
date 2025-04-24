package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class actuadorLedEntityWrapper {
	private List<actuadorLedEntity> ListaLed;
	
	public actuadorLedEntityWrapper() {
		super();
	}
	public actuadorLedEntityWrapper(List<actuadorLedEntity> ListaLed) {
		super();
		this.ListaLed = ListaLed;
	}
	public actuadorLedEntityWrapper(Collection<actuadorLedEntity> ListaLed) {
		super();
		this.ListaLed = new ArrayList<actuadorLedEntity>(ListaLed);
	}
	public List<actuadorLedEntity> getListaLed() {
		return ListaLed;
	}
	public void setListaLed(List<actuadorLedEntity> listaLed) {
		ListaLed = listaLed;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ListaLed);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		actuadorLedEntityWrapper other = (actuadorLedEntityWrapper) obj;
		return Objects.equals(ListaLed, other.ListaLed);
	}
	@Override
	public String toString() {
		return "actuadorLedEntityWrapper [ListaLed=" + ListaLed + "]";
	}
	
	
	
}

