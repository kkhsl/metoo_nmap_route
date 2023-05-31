package com.metoo.nspm.core.utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 方案：
 * iText，生成PDF文档，还支持将XML、Html文件转化为PDF文件；
 * Apache PDFBox，生成、合并PDF文档；
 * docx4j，生成docx、pptx、xlsx文档，支持转换为PDF格式
 */
public class PdfUtils {

    @Test
    public void getResource() throws FileNotFoundException {
        String path1 = this.getClass().getResource("").getPath();

    }

    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        //创建文档对象
        Document document = new Document();

        FileOutputStream fps = new FileOutputStream("example.pdf");

        //设置输出流
        PdfWriter.getInstance(document, fps);

        //打开文档
        document.open();

        //向文档中添加内容
        document.add(new Paragraph("Hello World!"));

        //关闭文档
        document.close();
    }
}
