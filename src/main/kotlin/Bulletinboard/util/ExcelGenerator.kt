package Bulletinboard.util

import Bulletinboard.DTO.PostListDTO
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelGenerator {
	
	companion object {
		fun customerPDFReport(posts: List<PostListDTO> ) : ByteArrayInputStream {
			val columns = arrayOf<String>("UserName", "Title", "Description")
			
			val workbook = XSSFWorkbook()
		 
			val sheet = workbook.createSheet("Posts")
		 
			val headerFont = workbook.createFont()
			headerFont.bold = true
			headerFont.color = IndexedColors.BLUE.getIndex()
		 
			val headerCellStyle = workbook.createCellStyle()
			headerCellStyle.setFont(headerFont)
		 
			// Row for Header
			val headerRow = sheet.createRow(0)
		 
			// Header
			for (col in columns.indices) {
				val cell = headerRow.createCell(col)
				cell.setCellValue(columns[col])
				cell.cellStyle = headerCellStyle
			}
			
			var rowId = 1
			for (post in posts) {
				val row = sheet.createRow(rowId++)
				row.createCell(0).setCellValue(post.createdUsername)
				row.createCell(1).setCellValue(post.title)
				row.createCell(2).setCellValue(post.description)
			}
			
			var out = ByteArrayOutputStream()	
			workbook.write(out)
			workbook.close()
			
			return ByteArrayInputStream(out.toByteArray());
		}
	}
}