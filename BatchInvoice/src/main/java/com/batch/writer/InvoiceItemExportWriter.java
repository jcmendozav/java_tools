package com.batch.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.batch.config.InvoiceProperties;
import com.batch.model.InvoiceExpDTO;


@Component
@Configuration
public class InvoiceItemExportWriter implements ItemStreamWriter<InvoiceExpDTO> {

	
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemExportWriter.class);

	@Autowired
	private InvoiceProperties invoiceProperties;
	
	private Workbook wb;
	
	private int row;
	
	private FileSystemResource resource;
	private String dateFormat;
	private String fileTemplatePath;
	private String outputPath;

	private CellStyle dateCellStyle;

	private CellStyle numericCellStyle;

	private int rowStart;
	
//	public void setRowStart(int rowStart) {
//		this.rowStart = rowStart;
//	}
	
//	public void setFileTemplatePath(String fileTemplatePath) {
//		this.fileTemplatePath = fileTemplatePath;
//	}
	
//	public void setOutputPath(String outputPath) {
//		this.outputPath = outputPath;
//	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@BeforeStep
	public void init() {
		this.outputPath=invoiceProperties.exportData.getOutputPath();
		this.fileTemplatePath=invoiceProperties.exportData.getFileTemplatePath();
		this.rowStart = invoiceProperties.exportData.getRowStart();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
	    //HSSFPalette palette = wb.getCustomPalette();
	    //HSSFSheet s = wb.createSheet();
	 
	    File fileTemplate = new File(fileTemplatePath);
        String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());
//        System.out.println("Copying: "+fileTemplate.getName());
        String newFileName=
        		fileTemplate.getName().split("\\.")[0]+
        		"_"+
        		timeStamp+
        		"."+
        		fileTemplate.getName().split("\\.")[1];
        
    	File newFile=new File(outputPath+"/"+newFileName);
    	
//    	try {
//			copyFile(fileTemplate.getAbsolutePath(), newFile.getAbsolutePath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        try {
        		
			if(Files.copy(
					fileTemplate.toPath()
					, newFile.toPath()
							) != null) {
				log.info("Sucessfull "
						+ "copy of:{}"
						+ ",to:{}"
						,fileTemplate.getAbsolutePath() 
						,newFile.getAbsolutePath()
						);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        resource=new FileSystemResource(newFile);
	    row = rowStart;
	    try {
			wb = WorkbookFactory.create(new FileInputStream(newFile));

			XSSFCreationHelper createHelper = (XSSFCreationHelper) wb.getCreationHelper();
			dateCellStyle = wb.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd")); 
			
			numericCellStyle = wb.createCellStyle();
			numericCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.00"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws ItemStreamException {
		
	    if (wb == null) {
	        return;
	    }
	    try (BufferedOutputStream bos = new BufferedOutputStream(resource.getOutputStream())) {
	        wb.write(bos);
	        bos.flush();
	        wb.close();
	    } catch (IOException ex) {
	        throw new ItemStreamException("Error writing to output file", ex);
	    }
	    row = 0;
		
	}

	@Override
	public void write(List<? extends InvoiceExpDTO> items) throws Exception {
	    XSSFSheet s = (XSSFSheet) wb.getSheetAt(1);
	    Cell c = null;
	    /*
	 * Comp code	1
     * Doc Date	2
     * Posting Date	3
     * Period	4
     * Doc Type	5
     * Curr	6 
     * Exch	7 -- Empty
     * Ref No	8
     * Doc Hdr txt	9
     * Calc tax	10 -- Empty
     * Post Key	11
     * Account	12
     * G/L ind.	13 -- Empty
     * Amnt Doc Curr	14 
     * Amnt Loc Curr	15 -- Empty
     * Amnt Loc Curr2	16 -- Empty
     * Paym trm	17
     * Base Date	18
     * Paym meth	19  -- Empty
     * CCntr	20
     * Order No	21 -- Empty
     * Sales ord No	22 -- Empty
     * Sales ord itm	23 -- Empty
     * Sales ord sch	24 -- Empty
     * WBS	25 -- Empty
     * Network No	26 -- Empty
     * Network act	27 -- Empty
     * Profit ctr	28 -- Empty
     * Assign. No	29
     * Itm txt	30
     * Id trad prtn	31 -- Empty
     * Tax code 32


	     * */
	    for (InvoiceExpDTO item : items) {
	    	
	        Row r = s.createRow(row++);

		// Transacton code	0
	        c = r.createCell(0);
	        c.setCellValue(item.getTxCode());	    	
		 // Comp code	1
	        c = r.createCell(1);
	        c.setCellValue(item.getCompCode());
	     // Doc Date	2
	        c = r.createCell(2);
	        c.setCellStyle(dateCellStyle);
	        c.setCellValue(item.getDocDate());	        
		 // Posting Date	3
	        c = r.createCell(3);
	        c.setCellStyle(dateCellStyle);
	        c.setCellValue(item.getPostingDate());	
	      // Period	4
	        
	        c = r.createCell(4);
	        c.setCellValue(item.getPeriod());	
		 // Doc Type	5
	        c = r.createCell(5);
	        c.setCellValue(item.getDocType());	
		 // Curr	6 
	        c = r.createCell(6);
	        c.setCellValue(item.getCurrencyCode());	
		 // Ref No	8
	        c = r.createCell(8);
	        c.setCellValue(item.getRefNo());	
		 // Doc Hdr txt	9
	        c = r.createCell(9);
	        c.setCellValue(item.getDocHdr());	
		 // Post Key	11
	        
	        c = r.createCell(11);
	        c.setCellValue(item.getPostKey());	
		 // Account	12
	        c = r.createCell(12);
	        c.setCellValue(item.getAccount());	
		 // Amnt Doc Curr	14 
	        c = r.createCell(14);
	        c.setCellStyle(numericCellStyle);
	        c.setCellValue(item.getAmntDocCurr());	
		 // Paym trm	17
	        c = r.createCell(17);
	        c.setCellValue(item.getPaymTrm());	
		 // Base Date	18
	        c = r.createCell(18);
	        c.setCellStyle(dateCellStyle);
	        c.setCellValue(item.getBaseDate());	
		 // CCntr	20
	        c = r.createCell(20);
	        c.setCellValue(item.getCcntr());	
		 // Assign. No	29
	        c = r.createCell(29);
	        c.setCellValue(item.getAssignNo());	
		 // Itm txt	30
	        c = r.createCell(30);
	        c.setCellValue(item.getItmTxt());	
	        // tax code 32
	        c = r.createCell(32);
	        c.setCellValue(item.getTaxCode());	

		}
	}
	
	
	
	public void copyFile(String sourcePath, String destinationPath) throws IOException {
	    FileInputStream excelFile = new FileInputStream(new File(sourcePath));
	    Workbook workbook = new XSSFWorkbook(excelFile);
	    FileOutputStream outputStream = new FileOutputStream(destinationPath);
	    workbook.write(outputStream);
	    workbook.close();
	}

}
