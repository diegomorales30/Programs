package edu.nmsu.cs.webserver;

import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * Web worker: an object of this class executes in its own new thread to receive and respond to a
 * single HTTP request. After the constructor the object executes on its "run" method, and leaves
 * when it is done.
 *
 * One WebWorker object is only responsible for one client connection. This code uses Java threads
 * to parallelize the handling of clients: each WebWorker runs in its own thread. This means that
 * you can essentially just think about what is happening on one client at a time, ignoring the fact
 * that the entirety of the webserver execution might be handling other clients, too.
 *
 * This WebWorker class (i.e., an object of this class) is where all the client interaction is done.
 * The "run()" method is the beginning -- think of it as the "main()" for a client interaction. It
 * does three things in a row, invoking three methods in this class: it reads the incoming HTTP
 * request; it writes out an HTTP header to begin its response, and then it writes out some HTML
 * content for the response content. HTTP requests and responses are just lines of text (in a very
 * particular format).
 * 
 * @author Jon Cook, Ph.D.
 *
 **/


public class WebWorker implements Runnable
{

	private Socket socket;
	private String extentionPath;

	/**
	 * Constructor: must have a valid open socket
	 **/
	public WebWorker(Socket s)
	{
		socket = s;
	}

	/**
	 * @return path
	 * 		This is a string of the path
	 * Worker thread starting point. Each worker handles just one HTTP request and then returns, which
	 * destroys the thread. This method assumes that whoever created the worker created it with a
	 * valid open socket object.
	 **/
	public void run()
	{
		System.err.println("Handling connection...");
		try
		{
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			// method returns the path of the file
			String pathOf = readHTTPRequest(is);
			// this method returns a file
			File file = checkFile(pathOf);
			String fileType = checkFileType(extentionPath);
			writeHTTPHeader(os,fileType,file);
			writeContent(os,file,fileType);
			os.flush();
			socket.close();
		}
		catch (Exception e)
		{
			System.err.println("Output error: " + e);
		}
		System.err.println("Done handling connection.");
		return;
	}

	/**
	 * Read the HTTP request header.
	 * This mehtod parses the string Get into an array. If the key word GET is first then
	 * it will get the length of the path and turn it into a string. Then this method will
	 * return a string called path.
	 **/
	private String readHTTPRequest(InputStream is)
	{
		String line;
		String path = "";
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		while (true)
		{
			try
			{
				while (!r.ready())
					Thread.sleep(1);
				line = r.readLine();
				//checking the path by spliting it into an array
				String [] checkPath = line.split(" ");
				System.err.println("Request line: (" + line + ")");
			
				if (line.length() == 0)
					break;
				// checks to see if the the first element in the array is get 
				if(checkPath[0].equals("GET")){
					path = "www/" + path + checkPath[1];
					extentionPath = path.substring(path.indexOf('.') + 1);
				}
					
	
			}
			catch (Exception e)
			{
				System.err.println("Request error: " + e);
				break;
			}
		}

		// returns the string of the path
		return path;
	}

	/**
	 * 
	 * @param filePath
	 * 	is the string of the path
	 * @return 
	 * 		A new file if it exist and if the file does not exist then the return turn type is null
	 * 
	 *	This method checks the file length to see if it is the original / if it is it set the file
	 *	name to index.html if the length is greater than one then it creats a new file with that name
	 *  If the file name does not exist it returns null. If the file path exist it will return the file 
	 */
	private File checkFile(String filePath){
		File fName = null;
		String nFilePath = "";
		
		// 
		/*if(filePath.length() <= 1){
			fName = new File("index.html");
			if(fName.exists())
				return fName;
		}*/
		// this block checks for any size of length getter than 1
		// forward slash followed by other text
		
			nFilePath = filePath.substring(0);
			fName = new File(nFilePath);
			if(fName.exists()){
				return fName;
			}
			return null;
		
	
		//return fName;
	}

	private String checkFileType(String extentionPath){
		String empty = null;
		if(extentionPath.equals("html")){
			return "text/html";
		}
		else if(extentionPath.equals("jpg")){
			return "image/jpg";
		}

		else if(extentionPath.equals("png")){
			return "image/png";
		}

		else if(extentionPath.equals("gif")){
			return "image/gif";
		}
		return empty;
	}

	/**
	 * Write the HTTP header lines to the client network connection.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param contentType
	 *          is the string MIME content type (e.g. "text/html")
	 * @param filePath
	 * 			This is the parametor that hold a file can either be null or a file
	 * 
	 * If the file does not exist it writes out 404 not found. If the file is not null
	 * then it writes out the HTTP 200 ok.
	 **/
	private void writeHTTPHeader(OutputStream os, String contentType,File filePath) throws Exception
	{
		
		Date d = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		// if the file does not exist then write out 404 not found
		if(filePath == null && contentType == null)
			os.write("HTTP/1.1 404 NotFound\n".getBytes());
		// the file exist
		else
			os.write("HTTP/1.1 200 OK\n".getBytes());
		os.write("Date: ".getBytes());
		os.write((df.format(d)).getBytes());
		os.write("\n".getBytes());
		os.write("Server: Jon's very own server\n".getBytes());
		// os.write("Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n".getBytes());
		// os.write("Content-Length: 438\n".getBytes());
		os.write("Connection: close\n".getBytes());
		os.write("Content-Type: ".getBytes());
		os.write(contentType.getBytes());
		os.write("\n\n".getBytes()); // HTTP header ends with 2 newlines
		return;
	}

	/**
	 * Write the data content to the client network connection. This MUST be done after the HTTP
	 * header has been written out.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param file
	 * 			This is the new file
	 * This method checks to see if the file path is null if it is the the html code
	 * is 404 not found. If the file exist then it will write out the html code to
	 * the webpage.
	 **/
	private void writeContent(OutputStream os,File file,String contentType) throws Exception
	{

			// if the file does not exist then write out 404 not found
			if(file == null && contentType == null){
				os.write("<html><head></head><body>\n".getBytes());
				os.write("<h3>404 Not Found</h3>\n".getBytes());
				os.write("</body></html>\n".getBytes());
				return;
			}
	
			else{
				// if the file exist then write out the contant
				String strLine;
				Date dateTime = new Date();
				BufferedReader bRead = new BufferedReader(new FileReader(file));
				if(contentType.equals("text/html")){
					while((strLine = bRead.readLine()) != null){
						strLine = strLine
							.replaceAll("<cs371date>", dateTime.toString())
							.replaceAll("<cs371server>", "Diego Websever");
						os.write(strLine.getBytes());
					
					}
					bRead.close();
				}
				else if(contentType.equals("image/jpg")){
					BufferedImage imageProcces = null;
					ByteArrayOutputStream imageHolder = new ByteArrayOutputStream();
					try {
						imageProcces = ImageIO.read(file);
					} catch (Exception e) {
						System.out.println("Image not found" + e);
					}
					ImageIO.write(imageProcces,"jpg",imageHolder);
					// bites of image in this array for the format of jpg
					byte[] imageBytes = imageHolder.toByteArray();
					os.write(imageBytes);
				}

				else if(contentType.equals("image/png")){
					BufferedImage imageProcces = null;
					ByteArrayOutputStream imageHolder = new ByteArrayOutputStream();
					try {
						imageProcces = ImageIO.read(file);
					} catch (Exception e) {
						System.out.println("Image not found" + e);
					}
					ImageIO.write(imageProcces,"png",imageHolder);
					// bites of image in this array for the format of jpg
					byte[] imageBytes = imageHolder.toByteArray();
					os.write(imageBytes);
				}

				else if(contentType.equals("image/gif")){
					BufferedImage imageProcces = null;
					ByteArrayOutputStream imageHolder = new ByteArrayOutputStream();
					try {
						imageProcces = ImageIO.read(file);
					} catch (Exception e) {
						System.out.println("Image not found" + e);
					}
					ImageIO.write(imageProcces,"gif",imageHolder);
					// bites of image in this array for the format of jpg
					byte[] imageBytes = imageHolder.toByteArray();
					os.write(imageBytes);
				}
				
			}
		
		
	}

} // end class
