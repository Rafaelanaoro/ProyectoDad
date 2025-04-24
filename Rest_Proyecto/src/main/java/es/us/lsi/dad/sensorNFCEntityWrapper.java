package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class sensorNFCEntityWrapper {
	private List<sensorNFCEntity> ListaNFC;
	
	public sensorNFCEntityWrapper() {
		super();
	}
	public sensorNFCEntityWrapper(Collection<sensorNFCEntity> ListaNFC) {
		super();
		this.ListaNFC = new ArrayList<sensorNFCEntity>(ListaNFC);
	}
	public sensorNFCEntityWrapper(List<sensorNFCEntity> ListaNFC) {
		super();
		this.ListaNFC = new ArrayList<sensorNFCEntity>(ListaNFC);
	}
	public List<sensorNFCEntity> getListaNFC() {
		return ListaNFC;
	}
	public void setListaNFC(List<sensorNFCEntity> listaNFC) {
		ListaNFC = listaNFC;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ListaNFC);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		sensorNFCEntityWrapper other = (sensorNFCEntityWrapper) obj;
		return Objects.equals(ListaNFC, other.ListaNFC);
	}
	@Override
	public String toString() {
		return "sensorNFCEntityWrapper [ListaNFC=" + ListaNFC + "]";
	}
	
	

}
