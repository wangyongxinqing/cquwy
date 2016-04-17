package yidong.nanan.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import yidong.nan.file.DownOLTWidthExcel;
import yidong.nan.file.UpOLTWidthExcel;

@SuppressWarnings(value={"serial","unused","deprecation","unchecked"})
public class DealOLTDownWidthUpload extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		//定义上载文件的最大字节
		int MAX_SIZE = 102400 * 102400;
		// 创建根路径的保存变量
		String rootPath;
		//声明文件读入类
		DataInputStream in = null;
		FileOutputStream fileOut = null;
		String classid = "";
		//取得客户端的网络地址
		String remoteAddr = request.getRemoteAddr();
		//获得服务器的名字
		String serverName = request.getServerName();
		//取得互联网程序的绝对地址
		String realPath = request.getRealPath(serverName);
		System.out.println(realPath);
		//创建文件的保存目录
		rootPath = realPath + "/file/";
		System.out.println(rootPath);
		//取得客户端上传的数据类型
		String contentType = request.getContentType();

		File uploadPath = new File(rootPath);//上传文件目录
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		// 临时文件目录
		File tempPathFile = new File(rootPath + "/temp");
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}

		try {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
			factory.setRepository(tempPathFile);//设置缓冲区目录

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB

			List<FileItem> items = upload.parseRequest(request);//得到所有的文件
			Iterator<FileItem> i = items.iterator();
			String fileName = "";
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				fileName = fi.getName();
				String fieldName = fi.getFieldName();
				if (fieldName.equals("classid")) {
					classid = fi.getString();
				}
				if (fileName != null) {
					File fullFile = new File(fi.getName());
					File savedFile = new File(uploadPath,
							fullFile.getName());
					fi.write(savedFile);
				}
			}
			//这里用一个文件模拟输入流
			String filepath = rootPath +  fileName;
			System.out.println(filepath);
			
			DownOLTWidthExcel downOLTWidthExcel = new DownOLTWidthExcel();
			
			if(downOLTWidthExcel.updateOLTDownWidth(filepath)){
			out.print("<script>alert('文件导入成功');window.location='index.jsp';</script>");
			}else{
				out.print("<script>alert('文件导入失败');window.location='index.jsp';</script>");
			}
			out.flush();
			out.close();
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

}
