package com.logicmonitor.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.dao.DocumentDaoImpl;
import com.logicmonitor.model.Document;

/**
 * Servlet implementation class UrlShortner
 */
@WebServlet(value={"/home.html"},loadOnStartup=1)
public class UrlShortner extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UrlShortner() {
        // TODO Auto-generated constructor stub
    }

    
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}


	/**
	 * This servlet loads the index.jsp on startup with setting all URLs present in database in servlet context.
	 * The front-end will load the table using this servlet context.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DocumentDaoImpl db = new DocumentDaoImpl();
		List<Document> allURLs = new ArrayList<Document>();
		allURLs = db.getAllURLs();
		System.out.println("All Urs"+allURLs.size());
		this.getServletContext().setAttribute("allURLs", allURLs);		
		request.getRequestDispatcher("/WEB-INF/html/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
