/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;
import Bean.TexttoSpeechConnector;
import Bean.ObjectStorageConnector;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import java.awt.Component;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.util.ResponseUtil;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import javax.servlet.annotation.WebServlet;

// import for object storage

import java.io.*;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;

import java.util.List;


@WebServlet(name = "TextToSpeechObjectStorage", urlPatterns = {"/TextToSpeechObjectStorage"})

public class TextToSpeechObjectStorage extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TextToSpeechObjectStorage</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TextToSpeechObjectStorage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            TexttoSpeechConnector connector = new TexttoSpeechConnector();      
  			TextToSpeech service = new TextToSpeech();
			
  			service.setUsernameAndPassword(connector.getUsername(),connector.getPassword());
  			//service.setUsernameAndPassword(username,password);
        
        	String text = request.getParameter("inputText");
        	String format = "audio/wav";

  			InputStream speech = service.synthesize(text, format);
            OutputStream output = response.getOutputStream();

		    byte[] buf = new byte[2046];
				int len;
				while ((len = speech.read(buf)) > 0) {
					output.write(buf, 0, len);
				}
                        
//              response.setContentType("audio/wav"); 
//				response.setHeader("Content-disposition","attachment;filename=output.wav");  
 
//				OutputStream os = output;   
                                
//				os.flush();  
//				os.close();  
				
//        processRequest(request, response);		
		
		// upload to object storage
		
		ObjectStorageConnector connect = new ObjectStorageConnector();
		
		String filename = null;
		Payload upfile = null;
		
//		connect.uploadFile("sample", "output.wav", os);
//		if (ServletFileUpload.isMultiPartContent(request)) {
//		if (ServletFileUpload.isMultiPartContent(response)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
//			ServletFileUpload upload = new ServletFileUpload(factory);
			
			try{
				
				List<FileItem> fields = upload.parseRequest(request);
//				List<FileItem> fields = upload.parseRequest(response);
				Iterator<FileItem> it = fields.iterator();
				while (it.hasNext()){
					FileItem fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if (isFormField){
						
					}else{
						filename = fileItem.getName();
//						upfile = Payloads.create(fileItem.getInputStream());
						upfile = Payloads.create(request.getInputStream());
					
					}
				}
				
				if (!filename.isEmpty() && !(upfile == null)){
					connect.uploadFile("sample", filename, upfile);
				}
			} catch(Exception e){
				System.out.println("ERROR::::");
			}
//		}

		
		response.sendRedirect("convert.jsp");
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
