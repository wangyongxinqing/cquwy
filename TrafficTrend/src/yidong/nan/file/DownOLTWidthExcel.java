package yidong.nan.file;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import yidong.nanan.dao.OLTTrafficDao;

public class DownOLTWidthExcel {

	private ArrayList<String> batchUpdateOLTDownWidthSqlList;

	public boolean updateOLTDownWidth(String filepath) {
        Sheet sheet;
        Workbook book;
		try { 
			File excel = new File(filepath);
			if(!excel.exists()){
				System.out.println("");
				return false;
			}
			
            book= new HSSFWorkbook(new FileInputStream(excel));
            sheet=book.getSheetAt(0);

            int rows = sheet.getLastRowNum();	
            batchUpdateOLTDownWidthSqlList=new ArrayList<String>();
            for(int i=1;i<=rows;i++){
            	
            	Row row = sheet.getRow(i);
            	String devicename=parseExcel(row.getCell(1));
            	String downWidth=parseExcel(row.getCell(2));
            	updateOLTUpWidth(devicename,downWidth);
            }
            book.close();
            OLTTrafficDao.batchOLTSql(batchUpdateOLTDownWidthSqlList);
        }catch(Exception e)  {
        	e.printStackTrace();
        	return false;
        } 
		
		return true;
	}
	
	private void updateOLTUpWidth(String devicename,String downWidth) {
		String sql="update t_olt_traffic set oltdownwidth='"+downWidth+"' where devicename='"+devicename+"'";
		batchUpdateOLTDownWidthSqlList.add(sql);
	}

	private String parseExcel(Cell cell) {
		String result = new String();
		if(cell==null)
			return "";
		int type = cell.getCellType();
		switch (type) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			short format = cell.getCellStyle().getDataFormat();
			SimpleDateFormat sdf = null;
			if(format == 14 || format == 31 || format == 57 || format == 58){
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			}else if (format == 20 || format == 32) {
				sdf = new SimpleDateFormat("HH:mm:ss");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			}else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat dformat = new DecimalFormat();
				String temp = style.getDataFormatString();
				if (temp.equals("General")) {
					dformat.applyPattern("#");
				}
				result = dformat.format(value);
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:// 
			result = cell.getRichStringCellValue().toString();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
		default:
			result = "";
			break;
		}
		return result;
	}
}
