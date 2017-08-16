package com.logicmonitor.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.dao.DocumentDaoImpl;
import com.logicmonitor.utils.LRUCache;

/**
 * Servlet implementation class RedirectionPage
 */
public class RedirectionPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RedirectionPage() {
        super();
    }

	/**
	 *  On any get requests having /rd/ will be redirected to this servlet.
	 *  Servlet will check cache if it's present it will redirect to that url
	 *  if not it will try to get "to" paramter from url and then redirects to url using database.
	 *  if "to" parameter is not present then it will fetch the whole url and query database. 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DocumentDaoImpl db = new DocumentDaoImpl();
		// TODO Auto-generated method stub
		//System.out.println("Param: "+request.getParameter("to"));
		//Map<String,String> map=(Map<String,String>) this.getServletContext().getAttribute("map");
		System.out.println("in redirection");
		LRUCache lru = LRUCache.getInstance();
		String lengthenURL = "";
		String shortURL = request.getParameter("to");
		if(shortURL == null || shortURL.isEmpty())
			shortURL = request.getRequestURL().toString();
		System.out.println("Bhavan"+shortURL);
		try{
			lengthenURL = lru.get(shortURL);
		}
		catch(IllegalArgumentException ex){
			lengthenURL = db.findLongURL(shortURL);
			lru.set(shortURL, lengthenURL);
		}
		System.out.println("Redirection: "+lengthenURL);
		response.sendRedirect(lengthenURL);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
