package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class actuadorServoEntityWrapper {

	private List<actuadorServoEntity> ListaServo;
	
	public actuadorServoEntityWrapper() {
		super();
	}
	public actuadorServoEntityWrapper(List<actuadorServoEntity> ListaServo) {
		super();
		this.ListaServo = ListaServo;
	}
	public actuadorServoEntityWrapper(Collection<actuadorServoEntity> ListaServo) {
		super();
		this.ListaServo = new ArrayList<actuadorServoEntity>(ListaServo);
	}
	public List<actuadorServoEntity> getListaServo() {
		return ListaServo;
	}
	public void setListaServo(List<actuadorServoEntity> listaServo) {
		ListaServo = listaServo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ListaServo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		actuadorServoEntityWrapper other = (actuadorServoEntityWrapper) obj;
		return Objects.equals(ListaServo, other.ListaServo);
	}
	@Override
	public String toString() {
		return "actuadorServoEntityWrapper [ListaServo=" + ListaServo + "]";
	}
	
	
}
