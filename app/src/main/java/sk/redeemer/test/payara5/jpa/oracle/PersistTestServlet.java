package sk.redeemer.test.payara5.jpa.oracle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class PersistTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	PersistTestEjb ejb;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LocalDateTime time = ejb.testPersist();
		long nano = time.getLong(ChronoField.NANO_OF_SECOND);
		
		response.getWriter()
			.append(PersistTestEjb.LOG_VALUE_PREFIX)
			.append(PersistTestEjb.FORMAT.format(time))
			.append('\n')
			.append((nano == 0) ? PersistTestEjb.LOG_NOK : PersistTestEjb.LOG_OK)
			.flush();;
	}

}
