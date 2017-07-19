package amazon.mws.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import amazon.db.model.Product;

public class InventoryReportParser {
	private static final int SHEET_INDEX = 0;
	private static final int ROW_INDEX_DATA_FROM = 1;
	private static final int COL_INDEX_SKU = 0;
	private static final int COL_INDEX_ASIN = 1;
	private static final int COL_INDEX_PRICE = 2;

	public static List<Product> parse(String fileName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		List<Product> products = new ArrayList<Product>();
		Product product;

		// create workbook
		File getFile = new File(fileName);
		Workbook workbook = WorkbookFactory.create(getFile);
		// Workbook workbook = new XSSFWorkbook(getFile);

		// get sheet
		Sheet sheet = workbook.getSheetAt(SHEET_INDEX);

		// get row numbers
		int rowNum = sheet.getLastRowNum() + 1;
		for (int rowIndex = ROW_INDEX_DATA_FROM; rowIndex < rowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			product = new Product();
			product.setSku(row.getCell(COL_INDEX_SKU).getStringCellValue());
			product.setAsin(row.getCell(COL_INDEX_ASIN).getStringCellValue());
			product.setPrice(row.getCell(COL_INDEX_PRICE).getNumericCellValue());

			products.add(product);
		}
		workbook.close();

		return products;
	}
}