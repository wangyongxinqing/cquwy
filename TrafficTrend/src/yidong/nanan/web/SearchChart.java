package yidong.nanan.web;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataset;

import yidong.nanan.dao.OLTTrafficDao;
import yidong.nanan.entity.DataBean;
import yidong.nanan.entity.UpdateIpAndPort;
import yidong.nanan.util.AddStringDateOneDay;
import yidong.nanan.util.PojoUtil;

@SuppressWarnings(value={"serial","deprecation"})
public class SearchChart extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String step=request.getParameter("step");
		if("first".equals(step))
			initSearchCondition(request,response);
		else if("second".equals(step))
			updatePortByDevice(request, response);
		else if("third".equals(step))
			showGraphic(request, response);
	}
	private void showGraphic(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		String device=request.getParameter("device");
		String type=request.getParameter("type");
		String name="";
		if("1".equals(type)){
			type="t.upport";
			name="OLT上行口流量监控趋势图";
		} else if("2".equals(type)){
			type="t.downport";
			name="OLT下行口流量监控趋势图";
		}
		String port=request.getParameter("port");
		String oltip=request.getParameter("ip");
		String fromtime=request.getParameter("fromtime");
		String totime=request.getParameter("totime");
		String showtype=request.getParameter("showtype");
		if("1".equals(showtype)){   //按日显示
			String sql="select t.upavg+100 avg,t.uppeak+120 peak,t.importtime from t_olt_traffic t where t.importtime>='"+fromtime
					+"' and t.importtime<='"+AddStringDateOneDay.addOneDay(totime)+"' and "
					+"t.devicename='"+device+"' and "+type+"='"+port+"' and oltip='"+oltip+"' order by t.importtime";
			List<DataBean> dataBeans=PojoUtil.getPojoListFromDB(sql, DataBean.class);
			TimeSeries timeSeriesAvg=new TimeSeries("上行平均流量利用率",Day.class);
			TimeSeries timeSeriesPeak=new TimeSeries("上行峰值流量利用率",Day.class);
			for(DataBean dataBean:dataBeans){
				String date=dataBean.getImporttime();
				int year = Integer.parseInt(date.substring(0, date.indexOf("-")));  
				int month = Integer.parseInt(date.substring(date.indexOf("-")+1, date.lastIndexOf("-")));   
				int day = Integer.parseInt(date.substring(date.lastIndexOf("-")+1, date.indexOf(" ")));   
				Day days=new Day(day,month,year);
				timeSeriesAvg.add(days,dataBean.getAvg());
				timeSeriesPeak.add(days,dataBean.getPeak());
			}
			TimeSeriesCollection timeseriescolection=new TimeSeriesCollection();
			timeseriescolection.addSeries(timeSeriesAvg);
			timeseriescolection.addSeries(timeSeriesPeak);
			backImage(request, out, timeseriescolection,name);
		}else if("2".equals(showtype)){  //按周显示
			String sql="select t.upavg+100 avg,t.uppeak+120 peak,t.importtime from t_olt_traffic t where t.importtime>='"+fromtime
					+"' and t.importtime<='"+AddStringDateOneDay.addOneDay(totime)+"' and "
					+"t.devicename='"+device+"' and "+type+"='"+port+"' and oltip='"+oltip+"' order by t.importtime";
			List<DataBean> dataBeans=PojoUtil.getPojoListFromDB(sql, DataBean.class);
			TimeSeries timeSeriesAvg=new TimeSeries("上行平均流量利用率",Week.class);
			TimeSeries timeSeriesPeak=new TimeSeries("上行峰值流量利用率",Week.class);
			for(DataBean dataBean:dataBeans){
				String date=dataBean.getImporttime();
				int year = Integer.parseInt(date.substring(0, date.indexOf("-")));  
				int month = Integer.parseInt(date.substring(date.indexOf("-")+1, date.lastIndexOf("-")));   
//				int day = Integer.parseInt(date.substring(date.lastIndexOf("-")+1, date.indexOf(" ")));   
//				Day days=new Day(day,month,year);
				Week days = new Week(month, year);
				timeSeriesAvg.addOrUpdate(days,dataBean.getAvg());
				timeSeriesPeak.addOrUpdate(days,dataBean.getPeak());
			}
			TimeSeriesCollection timeseriescolection=new TimeSeriesCollection();
			timeseriescolection.addSeries(timeSeriesAvg);
			timeseriescolection.addSeries(timeSeriesPeak);
			backImage(request, out, timeseriescolection,name);
		}else if("3".equals(showtype)){	 //按月显示
			String sql="select t.upavg+100 avg,t.uppeak+120 peak,t.importtime from t_olt_traffic t where t.importtime>='"+fromtime
					+"' and t.importtime<='"+AddStringDateOneDay.addOneDay(totime)+"' and "
					+"t.devicename='"+device+"' and "+type+"='"+port+"' and oltip='"+oltip+"' order by t.importtime";
			List<DataBean> dataBeans=PojoUtil.getPojoListFromDB(sql, DataBean.class);
			TimeSeries timeSeriesAvg=new TimeSeries("上行平均流量利用率",Month.class);
			TimeSeries timeSeriesPeak=new TimeSeries("上行峰值流量利用率",Month.class);
			for(DataBean dataBean:dataBeans){
				String date=dataBean.getImporttime();
				int year = Integer.parseInt(date.substring(0, date.indexOf("-")));  
				int month = Integer.parseInt(date.substring(date.indexOf("-")+1, date.lastIndexOf("-")));   
//				int day = Integer.parseInt(date.substring(date.lastIndexOf("-")+1, date.indexOf(" ")));   
//				Day days=new Day(day,month,year);
				Month days = new Month(month, year);
				timeSeriesAvg.addOrUpdate(days,dataBean.getAvg());
				timeSeriesPeak.addOrUpdate(days,dataBean.getPeak());
			}
			TimeSeriesCollection timeseriescolection=new TimeSeriesCollection();
			timeseriescolection.addSeries(timeSeriesAvg);
			timeseriescolection.addSeries(timeSeriesPeak);
			backImage(request, out, timeseriescolection,name);
		}
	}
	private void backImage(HttpServletRequest request, PrintWriter out,
			TimeSeriesCollection timeseriescolection,String name) throws IOException {
		JFreeChart jfreechart=createChart(name,timeseriescolection);
		String filename = ServletUtilities.saveChartAsPNG(jfreechart, 1500, 1000, null,request.getSession());
		String graphURL = request.getContextPath() + "/DisplayChart?filename="+ filename;
		out.write(graphURL);
		out.flush();
		out.close();
	}
	private JFreeChart createChart(String name,XYDataset xydataset) {
		 String x_name="日期";
	     String y_name="利用率";
	     JFreeChart jfreechart=ChartFactory.createTimeSeriesChart(name, x_name, y_name, xydataset,true,true,false);
	     //它是管整图的最外面的那个背景
	     jfreechart.setBackgroundPaint(Color.pink);//Color 是paint类型的对象
	     XYPlot categoryplot=jfreechart.getXYPlot();//当是这样的时，得改成XYPlot而不是CategoryPlot
	     NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis(); 
	     ValueAxis domainAxis = categoryplot.getDomainAxis(); //这个也是要改的
	     //定义字体格式
	     Font font = new Font("微软雅黑", Font.CENTER_BASELINE, 30);
	     TextTitle title = new TextTitle(name); 
	     //设置标题的格式
	     title.setFont(font);
	     //把标题设置到图片里面 
	     jfreechart.setTitle(title);
	     domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 30)); 
	     domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 30)); 
	     numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 30)); 
	     numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 30)); 
	     jfreechart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 30)); 
	     //categoryplot.setDomainCrosshairVisible(false);
	     categoryplot.setDomainGridlinePaint(Color.green);
	     categoryplot.setDomainGridlinesVisible(true);//它可以让背景线的竖线消失
	     categoryplot.setDomainGridlinePaint(Color.yellow);//这个是管背景线的竖线
	     return jfreechart;
	}
	private void updatePortByDevice(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out=response.getWriter();
		String device=request.getParameter("device");
		String type=request.getParameter("type");
		String sql="";
		if("1".equals(type))   //上行口标识
			sql="select distinct t.upport port,t.oltip ip from t_olt_traffic t where t.devicename='"+device+"' order by t.upport";
		else if("2".equals(type))
			sql="select distinct t.downport port,t.oltip ip from t_olt_traffic t where t.devicename='"+device+"' order by t.downport";
		List<UpdateIpAndPort> updateIpAndPortList=PojoUtil.getPojoListFromDB(sql, UpdateIpAndPort.class);
		JSONArray jsonArray=new JSONArray();
		for(UpdateIpAndPort updateIpAndPort:updateIpAndPortList){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("port", updateIpAndPort.getPort());
			jsonObject.put("ip", updateIpAndPort.getIp());
			jsonArray.add(jsonObject);
		}
		out.write(jsonArray.toString());
		out.flush();
		out.close();
	}
	private void initSearchCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sql="select distinct t.devicename from t_olt_traffic t";
		List<String> deviceNames=OLTTrafficDao.getAllList(sql);
		String type=request.getParameter("type");
		request.setAttribute("deviceNames", deviceNames);
		request.setAttribute("type", type);
		request.getRequestDispatcher("/search.jsp").forward(request, response);
	}
}
