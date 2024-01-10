package com.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.Serializable;

@ManagedBean
@SessionScoped
@Named(value = "pdfUtil")
public class PdfUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static StreamedContent generarPDF(String nombreArchivo, String contenido) throws DocumentException, IOException {
        byte[] contenidoPDF = cargarPDF(contenido);

        // Convierte el arreglo de bytes en un objeto StreamedContent de PrimeFaces.
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contenidoPDF);
        return DefaultStreamedContent.builder()
                .name(nombreArchivo+".pdf")
                .contentType("application/pdf")
                .stream(() -> inputStream)
                .build();
    }

	public static byte[] cargarPDF(String contenido) throws DocumentException, IOException {
        // Crear un nuevo documento PDF
        Document document = new Document();

        // Crear un flujo de salida en memoria para el PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        // Abrir el documento para escribir contenido
        document.open();

        // Agregar contenido al PDF
        document.add(new Paragraph(contenido));

        // Cerrar el documento
        document.close();

        // Convertir el flujo de salida a un arreglo de bytes
        byte[] pdfBytes = baos.toByteArray();

        return pdfBytes;
    }
}
	
