package yidong.nanan.entity;

public class OLTTrafficEntity {
	private String devicename;   //设备名称
	private String area;		 //区域名称
	private String oltip;		 //OLT IP地址
	private String upport;		 //上行口标识
	private String upavg;		 //上行平均流量
	private String uppeak;		 //上行峰值流量
	private String downavg;		 //下行平均流量
	private String downpeak;	 //下行峰值流量
	private String importtime;	 //导入时间
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getOltip() {
		return oltip;
	}
	public void setOltip(String oltip) {
		this.oltip = oltip;
	}
	public String getUpport() {
		return upport;
	}
	public void setUpport(String upport) {
		this.upport = upport;
	}
	public String getUpavg() {
		return upavg;
	}
	public void setUpavg(String upavg) {
		this.upavg = upavg;
	}
	public String getUppeak() {
		return uppeak;
	}
	public void setUppeak(String uppeak) {
		this.uppeak = uppeak;
	}
	public String getDownavg() {
		return downavg;
	}
	public void setDownavg(String downavg) {
		this.downavg = downavg;
	}
	public String getDownpeak() {
		return downpeak;
	}
	public void setDownpeak(String downpeak) {
		this.downpeak = downpeak;
	}
	public String getImporttime() {
		return importtime;
	}
	public void setImporttime(String importtime) {
		this.importtime = importtime;
	}
}
