package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.PhoneDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.PersonVo;

@WebServlet("/pbc")
public class PhonebookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("PhonebookController");

		String action = request.getParameter("action");
		// System.out.println(action);
		
		if ("writeform".equals(action)) {
			System.out.println("action=writeform");
			
			WebUtil.forward(request, response, "/WEB-INF/writeForm.jsp");
			
		} else if("write".equals(action)) {
			System.out.println("action=write");
			
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			
			PersonVo vo = new PersonVo(name, hp, company);
			
			PhoneDao dao = new PhoneDao();
			dao.personInsert(vo);
	
			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			
		} else if("updateform".equals(action)) {
			System.out.println("action=updateform");
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			PhoneDao dao = new PhoneDao();
			PersonVo person = dao.getPerson(id);
			person.setPersonId(id);
			
			request.setAttribute("personVo", person);
			
			WebUtil.forward(request, response, "/WEB-INF/updateForm.jsp");
			
		} else if("update".equals(action)) {
			System.out.println("action=update");
			
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			int id = Integer.parseInt(request.getParameter("id"));
			
			PersonVo vo = new PersonVo(id, name, hp, company);
			
			PhoneDao dao = new PhoneDao();
			dao.personUpdate(vo);
			
			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			
		} else if("delete".equals(action)) {
			System.out.println("action=delete");
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			PhoneDao dao = new PhoneDao();
			dao.personDelete(id);
			
			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			
		} else {
			PhoneDao dao = new PhoneDao();
			List<PersonVo> personList = dao.getPersonList();
			
			request.setAttribute("pList", personList);
		
			WebUtil.forward(request, response, "/WEB-INF/list.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
