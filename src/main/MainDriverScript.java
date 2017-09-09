package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import GUI.MTAppLibrary;
import utils.CommonLib;
import utils.Constants;

public class MainDriverScript extends MTAppLibrary {

	public static Workbook oWorkBook;
	public static Sheet oWorkSheet,oWorkTCSheet;
	public static String sTCID, sRunFlag, sTSID, sTSShtTCID, sKeyWord, sData,
			sRunStatus, sOutPutFile;
	public static Object[] oData;
	public static CellStyle oStyle;

	public static void main(String[] args) throws IOException {
		
		/*
		 *  Go to Login Sheet
		 *  Read the No. of Rows
		 *  Start Iterating eeach row,
		 *  Read the Data required to the test cases
		 *  Call the methods
		 *  Execute the test cases'
		 */

		
		MainDriverScript oTest=new MainDriverScript();
		loadExcelFile();
		
		oWorkTCSheet=oWorkBook.getSheet(Constants.XLLoginSht);
		int iRowCnt=oWorkTCSheet.getPhysicalNumberOfRows();
		for(int iRow=1;iRow<iRowCnt;iRow++)
		{
			String sBrowser=(String) getCellData(Constants.XLLoginSht,iRow,Constants.iBrowserCol);
			String sURL=(String) getCellData(Constants.XLLoginSht, iRow, Constants.iURLCol);
			String sUserName=(String) getCellData(Constants.XLLoginSht, iRow, Constants.iUserNameCol);
			String sPassword=(String) getCellData(Constants.XLLoginSht, iRow, Constants.iPasswordCol);
			
			openBrowser(sBrowser);
			navigateToURL(sURL);
			if(oTest.loginToMT(sUserName, sPassword).equalsIgnoreCase("pass"))
			{
				String sPassNo=(String) getCellData(Constants.XLSearchFlightsSht,iRow,Constants.iPassNo);
				String sDepart=(String) getCellData(Constants.XLSearchFlightsSht, iRow, Constants.iDeptCol);
				String sTrvlDate=(String) getCellData(Constants.XLSearchFlightsSht, iRow, Constants.iTrvlDateCol);
				String sArrivalCol=(String) getCellData(Constants.XLSearchFlightsSht, iRow, Constants.iArrivalCol);
				String sRtnDate=(String) getCellData(Constants.XLSearchFlightsSht, iRow, Constants.iRtnDateCol);
				String sAirlines=(String) getCellData(Constants.XLSearchFlightsSht, iRow, Constants.iAirlinesCol);
				if(oTest.findFlights(sPassNo, sDepart, sTrvlDate, sArrivalCol, sRtnDate, sAirlines).equalsIgnoreCase("pass"))
				{
					String sSrcAirlines=(String) getCellData(Constants.XLSelFlightsSht,iRow,Constants.iSrcAirlineCol);
					String sRtnAirlines=(String) getCellData(Constants.XLSelFlightsSht, iRow, Constants.iRtnAirlineCol);
					if(oTest.selectFlightsForRoundTrip(sSrcAirlines, sRtnAirlines).equalsIgnoreCase("pass"))
					{
						String sPassFn=(String) getCellData(Constants.XLBookFlightsSht,iRow,Constants.iFirstNameCol);
						String sPassLN=(String) getCellData(Constants.XLBookFlightsSht,iRow,Constants.iLastNameCol);
						String sPassCC=(String) getCellData(Constants.XLBookFlightsSht,iRow,Constants.iCreditNo);
						if(oTest.enterPassengerDetails(sPassFn, sPassLN, sPassCC).equalsIgnoreCase("pass"))
						{
							if(oTest.logOutFromMercuryTours().equalsIgnoreCase("pass"))
							{
								closeBrowser();
							}
						}
						
					}
					
				}
				
			}
			
			
			
			
		}
	
	
	}

	public static void loadExcelFile() throws IOException {
		try {
			FileInputStream oFile = new FileInputStream(Constants.XLPath);
			if (Constants.XLPath.endsWith(".xlsx")) {
				oWorkBook = new XSSFWorkbook(oFile);
			} else {
				oWorkBook = new HSSFWorkbook(oFile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Object getCellData(String XLShtName,int iRow,int iCol)
	{
		oWorkSheet=oWorkBook.getSheet(XLShtName);
		DecimalFormat decimalFormat = new DecimalFormat("0.#######");
		Cell oCellData=oWorkSheet.getRow(iRow).getCell(iCol);
		Object oData=oCellData.toString();
		Iterator<Row> iterator = oWorkSheet.iterator();
        
        /*while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
             
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                 
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	oData=cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                    	oData=cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                    	oData=cell.getNumericCellValue();
                        break;
                }
                System.out.print(" - ");
            }
            System.out.println();
        }
		*/
		return oData;

	}

}


