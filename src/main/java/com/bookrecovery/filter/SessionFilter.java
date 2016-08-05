package com.bookrecovery.filter;


import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.until.redis.load.RedisManager;

public class SessionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("this is test begin");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 这里设置如果没有登陆将要转发到的页面
	//	RequestDispatcher dispatcher = request.getRequestDispatcher("/map/mapTest.html");
		HttpServletRequest req = (HttpServletRequest) request;
	//	HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(true);
		SessionBean sessionBean=null;
		String sessionId=null;
		out:
		for(Cookie cookie:req.getCookies()){
			if("JSESSIONID".equals(cookie.getName())){
				sessionId=cookie.getValue();
				String value=RedisManager.getValueByKeyAndGroup("GROUP_0", "JSESSION_"+sessionId);
				sessionBean=JSON.parseObject(value, new SessionBean().getClass());
				break out;
			}
		}
		if(sessionBean==null){
			//新会话,第一次会话
			sessionBean=new SessionBean();
			sessionBean.setSessionId(req.getSession().getId());
			sessionBean.setCreateTime(new Date().getTime());
		}else{
			//二次会话
			sessionBean.getSession(req.getSession());
		}
		if(sessionId==null){
			sessionId=req.getSession().getId();
		}
		// 判断如果没有取到用户信息,就跳转到登陆页面
		/*if (username == null || "".equals(username)) {
			// 跳转到登陆页面
			dispatcher.forward(request, response);
			// System.out.println("用户没有登陆，不允许操作");
			res.setHeader("Cache-Control", "no-store");
			res.setDateHeader("Expires", 0);
			res.setHeader("Pragma", "no-cache");
		} else {
			// 
		}*/
		chain.doFilter(request, response);
		sessionBean.setLastModifyTime(new Date().getTime());
		RedisManager.setValueByKeyAndGroupSetTime("GROUP_0", "JSESSION_"+sessionBean.getSessionId(), JSON.toJSONString(sessionBean.UpdateSessionBean(session)),sessionBean.getMaxInactiveInterval());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("this is test end");
	}

}
