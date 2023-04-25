package com.tarladalal.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {

	private static ExcelManager manager;
	private XSSFWorkbook wbook;
private ExcelManager()
{
	try {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		//InputStream  fs=loader.getResourceAsStream("data.xlsx");
		InputStream  fs= new FileInputStream("C:\\NumpyScraping\\data.xlsx");
		wbook=new XSSFWorkbook(fs);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static List<String[]> getRows(String sheetName)
{
	if(manager==null)
	{
		manager=new ExcelManager();
	}
	List<String[]> list=new ArrayList<String[]>();
	XSSFSheet sheet=manager.wbook.getSheet(sheetName);
	int lastrow=sheet.getLastRowNum()+1;

	for (int i=0;i<lastrow;i++)
	{
		XSSFRow row = sheet.getRow(i);
		int lastcellnum = row.getLastCellNum();
		String[] srow = new String[lastcellnum]; 
		for (int j=0; j<lastcellnum; j++)
		{
			//row.add(getCell(column-1).getStringCellValue());	
			srow[j] = row.getCell(j).getStringCellValue();
		}
		list.add(srow);
			
	}
	return list;
	
}

public static List<String> getColumn(String sheetName,int column)
{
	if(manager==null)
	{
		manager=new ExcelManager();
	}
	List<String> list=new ArrayList<String>();
	XSSFSheet sheet=manager.wbook.getSheet(sheetName);
	int lastrow=sheet.getLastRowNum()+1;

	for (int i=0;i<lastrow;i++)
	{
			list.add(sheet.getRow(i).getCell(column-1).getStringCellValue());
	}
	return list;
	
}

public static Map<String,String> getMap(String sheetName)
{
	if(manager==null)
	{
		manager=new ExcelManager();
	}
	Map<String,String> map=new HashMap<String,String>();
	XSSFSheet sheet=manager.wbook.getSheet(sheetName);
	int lastrow=sheet.getLastRowNum()+1;

	for (int i=0;i<lastrow;i++)
	{
		map.put(sheet.getRow(i).getCell(0).getStringCellValue(),sheet.getRow(i).getCell(1).getStringCellValue());
	}
	return map;
}

public static  void setData (String sheetName, Object[] row)
{
	if(manager==null)
	{
		manager=new ExcelManager();
	}
	
	XSSFSheet sheet=manager.wbook.getSheet(sheetName);
	int rownum = sheet.getLastRowNum()+1;
	XSSFRow newrow = sheet.createRow(rownum);
	
	for (int i=0;i < row.length ;i++)
	{
		XSSFCell cell = newrow.createCell(i);
		cell.setCellValue((String)row[i]);
	}
	
	
}

public static void writeFile()

{
	if(manager==null)
	{
		manager=new ExcelManager();
	}
	FileOutputStream fos;
	try {
		fos = new FileOutputStream("c:\\NumpyScraping\\data.xlsx");
		manager.wbook.write(fos);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
}

/*
 * public static void main(String arg[]) {
 * System.out.println(ExcelManager.getColumn("allergy", 1));
 * System.out.println(ExcelManager.getColumn("Hypothyroidism_eliminate", 1));
 * System.out.println(ExcelManager.getColumn("Hypothyroidism_toadd", 1));
 * System.out.println(ExcelManager.getMap("foodcategory"));
 * System.out.println(ExcelManager.getMap("recipecategory"));
 * System.out.println(ExcelManager.getRows("completelist")); }
 */

}
