package yidong.nanan.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
@SuppressWarnings("unchecked")
public class EncodingFilter implements Filter {
	private String encode = null;
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//--一行代码解决响应乱码
		response.setContentType("text/html;charset="+encode);
		//--通过修改request对象的和获取请求参数相关的方法,虽然请求参数还是乱码,但是通过这些方法获取时,乱码会被解决
		chain.doFilter(new MyHttpServletRequest((HttpServletRequest) request), response);
	}
	/**
	 * 装饰类,装饰request对象,改造和获取请求参数相关的三个方法,改造成获取有乱码的值,解决乱码后返回
	 * 这样一来,包装过后的request,虽然内部还是乱码,但是通过方法获取时,是解决过后没有乱码的值
	 * @author park
	 *
	 */
	class MyHttpServletRequest extends HttpServletRequestWrapper{
		private HttpServletRequest request = null;
		private boolean hasNotEncode = true;
		public MyHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
		}
		
		@Override
		public Map<String,String[]> getParameterMap() {
			try {
				if(request.getMethod().equals("POST")){
					//--POST提交,一行代码解决乱码,返回
					request.setCharacterEncoding(encode);
					return request.getParameterMap();
				}else if(request.getMethod().equals("GET")){
					//--GET提交获取有乱码的Map,手动编解码解决返回
					Map<String,String[]> map = request.getParameterMap();
					if(hasNotEncode){
						for(Map.Entry<String, String[]> entry : map.entrySet()){
							String [] vs = entry.getValue();
							for(int i = 0;i<vs.length;i++){
								vs[i] = new String(vs[i].getBytes("iso8859-1"),encode);
							}
						}
						hasNotEncode = false;
					}
					return map;
				}else{
					//--其他提交方式
					return request.getParameterMap();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public String[] getParameterValues(String name) {
			return getParameterMap().get(name);
		}
		
		@Override
		public String getParameter(String name) {
			String [] vs = getParameterValues(name);
			return vs == null ? null : vs[0];
		}
		
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encode = filterConfig.getServletContext().getInitParameter("encode");
	}

}
