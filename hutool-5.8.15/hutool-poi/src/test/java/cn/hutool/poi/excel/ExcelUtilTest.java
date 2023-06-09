package cn.hutool.poi.excel;

import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelUtilTest {

	@Test
	public void indexToColNameTest() {
		Assert.assertEquals("A", ExcelUtil.indexToColName(0));
		Assert.assertEquals("B", ExcelUtil.indexToColName(1));
		Assert.assertEquals("C", ExcelUtil.indexToColName(2));

		Assert.assertEquals("AA", ExcelUtil.indexToColName(26));
		Assert.assertEquals("AB", ExcelUtil.indexToColName(27));
		Assert.assertEquals("AC", ExcelUtil.indexToColName(28));

		Assert.assertEquals("AAA", ExcelUtil.indexToColName(702));
		Assert.assertEquals("AAB", ExcelUtil.indexToColName(703));
		Assert.assertEquals("AAC", ExcelUtil.indexToColName(704));
	}

	@Test
	public void colNameToIndexTest() {
		Assert.assertEquals(704, ExcelUtil.colNameToIndex("AAC"));
		Assert.assertEquals(703, ExcelUtil.colNameToIndex("AAB"));
		Assert.assertEquals(702, ExcelUtil.colNameToIndex("AAA"));

		Assert.assertEquals(28, ExcelUtil.colNameToIndex("AC"));
		Assert.assertEquals(27, ExcelUtil.colNameToIndex("AB"));
		Assert.assertEquals(26, ExcelUtil.colNameToIndex("AA"));

		Assert.assertEquals(2, ExcelUtil.colNameToIndex("C"));
		Assert.assertEquals(1, ExcelUtil.colNameToIndex("B"));
		Assert.assertEquals(0, ExcelUtil.colNameToIndex("A"));
	}

	@Test
	public void toLocationTest() {
		final CellLocation a11 = ExcelUtil.toLocation("A11");
		Assert.assertEquals(0, a11.getX());
		Assert.assertEquals(10, a11.getY());
	}

	@Test
	public void readAndWriteTest() {
		final ExcelReader reader = ExcelUtil.getReader("aaa.xlsx");
		final ExcelWriter writer = reader.getWriter();
		writer.writeCellValue(1, 2, "设置值");
		writer.close();
	}

	@Test
	public void getReaderByBookFilePathAndSheetNameTest() {
		final ExcelReader reader = ExcelUtil.getReader("aaa.xlsx", "12");
		final List<Map<String, Object>> list = reader.readAll();
		reader.close();
		Assert.assertEquals(1L, list.get(1).get("鞋码"));
	}

	@Test
	public void doAfterAllAnalysedTest() {
		final String path = "readBySax.xls";
		final AtomicInteger doAfterAllAnalysedTime = new AtomicInteger(0);
		ExcelUtil.readBySax(path, -1, new RowHandler() {
			@Override
			public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {
				//Console.log("sheetIndex={};rowIndex={},rowCells={}",sheetIndex,rowIndex,rowCells);
			}

			@Override
			public void doAfterAllAnalysed() {
				doAfterAllAnalysedTime.addAndGet(1);
			}
		});
		//总共2个sheet页，读取所有sheet时，一共执行doAfterAllAnalysed2次。
		Assert.assertEquals(2, doAfterAllAnalysedTime.intValue());
	}

}
