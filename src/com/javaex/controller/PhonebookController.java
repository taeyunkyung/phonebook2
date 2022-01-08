package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@WebServlet("/pbc")
public class PhonebookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("PhonebookController");

		String action = request.getParameter("action");
		// System.out.println(action);

		PhoneDao phoneDao = new PhoneDao();
		List<PersonVo> personList = phoneDao.getPersonList();	
		// System.out.println(personList);
		
		if ("list".equals(action)) {
			System.out.println("action=list");
			// request 내 attribute 영역에 personList를 pList라는 이름으로 넣기
			request.setAttribute("pList", personList);

			// 포워드
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/list.jsp");
			rd.forward(request, response);
			
		} else if ("writeform".equals(action)) {
			System.out.println("action=writeform");
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/writeForm.jsp");
			rd.forward(request, response);
			
		} else if("write".equals(action)) {
			System.out.println("action=write");
			
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			
			PersonVo vo = new PersonVo(name, hp, company);
			phoneDao.personInsert(vo);
	
			response.sendRedirect("/phonebook2/pbc?action=list");
			
		} else if("updateform".equals(action)) {
			System.out.println("action=updateform");
			
			int id = Integer.parseInt(request.getParameter("id"));
			PersonVo person = phoneDao.getPerson(id);
			person.setPersonId(id);
			
			request.setAttribute("personVo", person);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/updateForm.jsp");
			rd.forward(request, response);
			
		} else if("update".equals(action)) {
			System.out.println("action=update");
			
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");
			int id = Integer.parseInt(request.getParameter("id"));
			
			PersonVo vo = new PersonVo(id, name, hp, company);
			phoneDao.personUpdate(vo);
			
			response.sendRedirect("/phonebook2/pbc?action=list");
			
		} else if("delete".equals(action)) {
			System.out.println("action=delete");
			
			int id = Integer.parseInt(request.getParameter("id"));
			phoneDao.personDelete(id);
			
			response.sendRedirect("/phonebook2/pbc?action=list");
			
		} else {
			System.out.println("파라미터 없음");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
