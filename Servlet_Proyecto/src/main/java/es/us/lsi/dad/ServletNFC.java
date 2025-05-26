package es.us.lsi.dad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletNFC extends HttpServlet{
	private static final long serialVersionUID = -6201150158950823811L;

	private List<sensorImpleNFC> th;
	
	public void init() throws ServletException {
		th = new ArrayList<sensorImpleNFC>();
		sensorImpleNFC medidast = new sensorImpleNFC(1, 1, (long)23.3);
		th.add(medidast);
		//ids.add(0);
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idNFC = req.getParameter("idNFC");
		String valor = req.getParameter("valor");
		
		Integer idNFC2 = Integer.valueOf(idNFC);
		Integer valor2 = Integer.valueOf(valor);

		sensorImpleNFC nfc = new sensorImpleNFC(idNFC2, valor2, (long) 1.1);

		if(th.contains(nfc)){
			response(resp, "Aqui está su NFC regsitrado" + nfc);
		}else {
			response(resp, "No existe ese NFC");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    Gson gson = new Gson();
	    sensorImpleNFC Snfc = gson.fromJson(reader, sensorImpleNFC.class);
		
		if ( !Snfc.getIdNFC().equals("") && !Snfc.getValor().equals("") && !Snfc.getFecha().equals("")) {
			sensorImpleNFC nfc = new sensorImpleNFC(Snfc.getIdNFC(), Snfc.getValor(), Snfc.getFecha());
			
			th.add(nfc);
			response(resp, "Objeto añadido: ");
			resp.getWriter().println(gson.toJson(Snfc));
			resp.setStatus(201);

		}else{
			resp.setStatus(300);
			response(resp, "Objeto del tipo incorrecto");
		}
	   
	}
	
	
	
	
	private void response(HttpServletResponse resp, String msg) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<t1>" + msg + "</t1>");
		out.println("</body>");
		out.println("</html>");
	}
	
	
	
}
