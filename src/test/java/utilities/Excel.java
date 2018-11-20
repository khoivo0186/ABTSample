package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class Excel {
	public XSSFSheet ExcelWSheet;
	private XSSFWorkbook ExcelWBook;
	private XSSFCell Cell;
	private XSSFRow Row;
	public Method method;

	// This method is to set the File path and to open the Excel file, Pass
	// Excel Path and Sheetname as Arguments to this method
	public void setExcelFile(String Path, String SheetName) throws Exception {
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(Path);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

	// This method is used to get the total rows in data Excel file
	public int getNumrow() throws Exception {
		try {
			int numrow = ExcelWSheet.getLastRowNum() - ExcelWSheet.getFirstRowNum();
			return numrow;
		} catch (Exception e) {
			return 0;
		}
	}

	// This method is used to get the total column in data Excel file
	public int getLastColumnIndex() throws Exception {
		try {
			int cont = 1;
			int column_index = 0;
			while (cont == 1) {
				if (getCellData(ExcelWSheet.getFirstRowNum(), column_index) != "") {
					column_index++;
				} else {
					cont = 0;
				}
			}
			return column_index;

		} catch (Exception e) {
			return 0;
		}
	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num
	public String getCellData(int RowNum, int ColNum) {
		try {
			String CellData = "";
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			if (Cell.getCellType() == 1) {
				CellData = Cell.getStringCellValue().toString();
			} else if (Cell.getCellType() == 0) {
				CellData = String.valueOf(Cell.getNumericCellValue());
			}

			return CellData;
		} catch (Exception e) {
			return "";

		}
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters
	public void setCellData(String filepath, String Result, int RowNum, int ColNum) throws Exception {
		try {
			Row = ExcelWSheet.getRow(RowNum);
			if (Row == null) {
				Row = ExcelWSheet.createRow(RowNum);
			}
			Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters
	public void setRowData(String filepath, String[] arrResult, int RowNum, int ColNum) throws Exception {
		try {
			Row = ExcelWSheet.getRow(RowNum);
			if (Row == null) {
				Row = ExcelWSheet.createRow(RowNum);
			}
			for (int i = 0; i < arrResult.length; i++) {
				Cell = Row.getCell(ColNum + i, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
				if (Cell == null) {
					Cell = Row.createCell(ColNum + i);
					Cell.setCellValue(arrResult[i]);
				} else {
					Cell.setCellValue(arrResult[i]);
				}
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * Retrieve column index by row and value
	 * @param rownum row index
	 * @param value look-up value
	 * @return index of column
	 */
	public int getColumnIndex(Integer rownum, String value) {
		int columnIndex = -1;
		try {
			int totalColumn = getLastColumnIndex();
			for (int i = 0; i < totalColumn; i++) {
				Cell = ExcelWSheet.getRow(rownum).getCell(i);
				String CellData = Cell.getStringCellValue().trim();
				if (CellData.equals(value)) {
					columnIndex = i;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return columnIndex;
	}
	
	/**
	 * Retrieve data from first sheet
	 * @param filePath path to excel file
	 * @return a 2-dimensional array for dataset 
	 */
	public Object[][] getTableArray(String filePath) {

		String[][] tabArray = null;
		int filterColumnIndex;

		try {
			// -Get column header that is used to filter
			String filterValue = "";
			if (method != null) {
				Test tstAnnotation = method.getAnnotation(Test.class);
				String[] groups = null;
				if (tstAnnotation != null) {
					groups = tstAnnotation.groups();
				}
				if (groups != null) {
					if (groups.length > 0) {
						for (String group : groups) {
							if (group.trim().indexOf("Filter:") >= 0) {
								filterValue = group.trim().substring(7);
							}
						}

					}
				}
			}

			FileInputStream ExcelFile = new FileInputStream(filePath);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheetAt(0);

			filterColumnIndex = getColumnIndex(0, "Filter");

			int startRow = 1;
			int startCol = 0;
			int ci, cj;
			int totalRows = ExcelWSheet.getLastRowNum() - ExcelWSheet.getFirstRowNum();

			int totalCols = 0;
			try {
				totalCols = getLastColumnIndex();
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Count total row match with filter value
			int filterRows = 0;
			for (int i = startRow; i <= totalRows; i++) {
				if (filterColumnIndex == -1 | getCellData(i, filterColumnIndex).equals(filterValue)
						| filterValue == "") {
					filterRows++;
				}
			}

			int filterColumns = 0;
			if (filterColumnIndex > -1) {
				filterColumns = totalCols - 1;
			} else {
				filterColumns = totalCols;
			}

			tabArray = new String[filterRows][filterColumns];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++) {
				cj = 0;

				if (filterColumnIndex == -1 | getCellData(i, filterColumnIndex).equals(filterValue)
						| filterValue == "") {
					for (int j = startCol; j < totalCols; j++) {
						try {
							if (j != filterColumnIndex) {
								tabArray[ci][cj] = getCellData(i, j).toString();
								cj = cj + 1;
							}

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
					ci = ci + 1;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return removeNullRow(tabArray);
	}
	
	/**
	 * Remove null rows in array
	 * @param origin
	 * @return
	 */
	private Object[][] removeNullRow(String[][] origin) {
		List<String[]> temp = new ArrayList<String[]>();
		for (String[] row: origin) {
			String rowString = "";
			for (String cell: row) {
				rowString = rowString + cell;
			}
			
			if (!rowString.equalsIgnoreCase("") && rowString != null) {
				temp.add(row);
			}
		}
		
		String[][] array = new String[temp.size()][];
		for (int i = 0; i < temp.size(); i++) {
		    array[i] = temp.get(i);
		}
		
		return array;
	}
	
	/**
	 * Retrieve data from specific excel file and sheet name
	 * @param filePath path to excel file
	 * @param sheetName sheet name
	 * @return a 2-dimensional array for dataset 
	 */
	public Object[][] getTableArray(String filePath, String sheetName) {

		String[][] tabArray = null;
		int filterColumnIndex;

		try {
			// -Get column header that is used to filter
			String filterValue = "";
			if (method != null) {
				Test tstAnnotation = method.getAnnotation(Test.class);
				String[] groups = null;
				if (tstAnnotation != null) {
					groups = tstAnnotation.groups();
				}
				if (groups != null) {
					if (groups.length > 0) {
						for (String group : groups) {
							if (group.trim().indexOf("Filter:") >= 0) {
								filterValue = group.trim().substring(7);
							}
						}

					}
				}
			}

			FileInputStream ExcelFile = new FileInputStream(filePath);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);

			filterColumnIndex = getColumnIndex(0, "Filter");

			int startRow = 1;
			int startCol = 0;
			int ci, cj;
			int totalRows = ExcelWSheet.getLastRowNum() - ExcelWSheet.getFirstRowNum();

			int totalCols = 0;
			try {
				totalCols = getLastColumnIndex();
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Count total row match with filter value
			int filterRows = 0;
			for (int i = startRow; i <= totalRows; i++) {
				if (filterColumnIndex == -1 | getCellData(i, filterColumnIndex).equals(filterValue)
						| filterValue == "") {
					filterRows++;
				}
			}

			int filterColumns = 0;
			if (filterColumnIndex > -1) {
				filterColumns = totalCols - 1;
			} else {
				filterColumns = totalCols;
			}

			tabArray = new String[filterRows][filterColumns];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++) {
				cj = 0;

				if (filterColumnIndex == -1 | getCellData(i, filterColumnIndex).equals(filterValue)
						| filterValue == "") {
					for (int j = startCol; j < totalCols; j++) {
						try {
							if (j != filterColumnIndex) {
								tabArray[ci][cj] = getCellData(i, j).toString();
								cj = cj + 1;
							}

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
					ci = ci + 1;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return removeNullRow(tabArray);
	}

	/**
	 * Read specific sheet start at row and column in excel file
	 * 
	 * @param filePath  excel file path
	 * @param sheetName
	 * @param startRow
	 * @param startCol
	 * @return
	 */
	public Object[][] getExcelSheet(String filePath, String sheetName, int startRow, int startCol) {

		String[][] tabArray = null;

		try {
			FileInputStream ExcelFile = new FileInputStream(filePath);

			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);

			// int startRow = 1;
			// int startCol = 0;
			// int totalRows = ExcelWSheet.getLastRowNum() - ExcelWSheet.getFirstRowNum();
			int totalRows = ExcelWSheet.getLastRowNum() + 1 - startRow;
			int totalCols = 0;
			try {
				totalCols = getLastColumnIndex();
			} catch (Exception e) {

				e.printStackTrace();
			}

			tabArray = new String[totalRows][totalCols];
			int ci = 0;
			int cj = 0;
			for (int i = startRow; i <= totalRows; i++) {
				for (int j = startCol; j < totalCols; j++) {
					try {
						tabArray[ci][cj++] = getCellData(i, j).toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				ci++;
				cj = 0;
			}
			// close file
			ExcelFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return removeNullRow(tabArray);
	}
}
