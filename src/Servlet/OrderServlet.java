package Servlet;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Bean.Dish;
import Bean.Order;
import Bean.PageModel;
import DAO.SearchDAO;
import DAO.SortDAO;
import DAOIMPL.DishIMPL;
import DAOIMPL.OrderIMPL;
import JDBC.BaseDAO;
import JDBC.DAOFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    //查询订单信息
	@SuppressWarnings("unchecked")
	private String searchByOrderNo(String OrderNo) {
		//ͨ初始化列表
		ArrayList<Order> list = new ArrayList<>();
		SearchDAO searchDAO =(SearchDAO)DAOFactory.newInstance("Order");
		String[] params = {OrderNo};
		list = (ArrayList<Order>)searchDAO.searchByPrimaryKey(params);
		//初始化JSON数组
		JSONArray jsonArray = new JSONArray();
		for(int i =0;i<list.size();i++) {
			jsonArray.add(JSONObject.fromObject(list.get(i)));
		}
		return jsonArray.toString();
	}
	//通过主键查询订单
	@SuppressWarnings("unchecked")
	private ArrayList<Order> searchCurrentOrder(String OrderNo){
		ArrayList<Order> list = new ArrayList<>();
		SearchDAO searchDAO = (SearchDAO)DAOFactory.newInstance("Order");
		String[] params = {OrderNo};
		list = (ArrayList<Order>)searchDAO.searchByPrimaryKey(params);
		return list;
	}
	/**
	 * 查询当前订单内的菜品信息。
	 * @param OrderNo
	 * @return
	 */
	private ArrayList<Dish> searchDishOfOrder(String OrderNo){
		ArrayList<Dish> list = new ArrayList<>();
		OrderIMPL orderDAO=new OrderIMPL();
		String []params= {OrderNo};
		list=orderDAO.searchDishOfOrder(params);
		return list; 
	}
	//查询所有订单信息
	@SuppressWarnings("unchecked")
	private String searchAllOrders() {
		ArrayList<Order> list = new ArrayList<>();
		SearchDAO searchDAO =(SearchDAO)DAOFactory.newInstance("Order");
		list=(ArrayList<Order>)searchDAO.searchAll();
		//生成JSON数组。
		JSONArray jsonArray=new JSONArray();
		for(int i =0;i<list.size();i++) {
			jsonArray.add(JSONObject.fromObject(list.get(i)));
		}
		return jsonArray.toString();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		//获得要查询的订单号。
		String ordernumber = null;
		switch (action) {
		//所有订单界面的查询
		case "search":
			String OrderNo = request.getParameter("searchText");
			response.setCharacterEncoding("utf-8");
			response.getWriter().println(searchByOrderNo(OrderNo));			
			break;
		case "getPageModel":
			response.setCharacterEncoding("utf-8");
			response.getWriter().println(getPageModel(request,response));
			break;
		case "searchByPage":
			response.setCharacterEncoding("utf-8");
			response.getWriter().println(searchByPage(request, response,false));
			break;
		//所有订单界面的加载
		case "load":
			response.getWriter().println(searchAllOrders());
			break;
		case "statement":
			//获取订单号
			ordernumber=request.getParameter("ordernumber");
			ArrayList<Order> orderList=searchCurrentOrder(ordernumber);
            request.setAttribute("orderList", orderList);
            //查询订单中的菜品
            ArrayList<Dish> dishList=searchDishOfOrder(ordernumber);
            request.setAttribute("dishlist", dishList);
            //跳转订单详情业
        	RequestDispatcher rDispatcher=request.getRequestDispatcher("./MerchantJSP/OrderStatement.jsp");
        	rDispatcher.forward(request, response);	
			break;		
		default:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	/**
	 * 通过页面查询
	 * @param request
	 * @param response
	 * @return string
	 */
	@SuppressWarnings("unchecked")
	public String searchByPage(HttpServletRequest request,HttpServletResponse response,boolean isSort) {
		SearchDAO searchDAO = (SearchDAO)DAOFactory.newInstance("Order");
		//查询页面第一页的所有菜品信息
		int currenPage = Integer.parseInt(request.getParameter("currentPage"));
		ArrayList<Order> list = (ArrayList<Order>)searchDAO.searchByPage(currenPage, PageModel.getPageSize());
		if(isSort) {
			SortDAO sortDAO = (SortDAO)DAOFactory.newInstance("Order");
			Order[] dishs = (Order[])list.toArray();
			list.clear();
			list.addAll(sortDAO.descSort(dishs));
		}
		JSONArray jsonArray = new JSONArray();
		for(int i =0;i<list.size();i++) {
			jsonArray.add(JSONObject.fromObject(list.get(i)));
		}
		return jsonArray.toString();
	}
	/**
	 * 获得pagemodel
	 * @param currentPage
	 * @param pageSize
	 * @return 一组json对象的字符串
	 */	private String getPageModel(HttpServletRequest request,HttpServletResponse response) {
		SearchDAO searchDAO = (SearchDAO)DAOFactory.newInstance("Order");
		//分页信息
		PageModel pageModel = new PageModel(searchDAO.getCount());
		JSONObject jsonObject = JSONObject.fromObject(pageModel);
		request.setAttribute("DishPageModel", pageModel);
		return jsonObject.toString();
	}

}
