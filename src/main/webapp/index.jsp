<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Text To Speech Speech To Text</title>
		
    </head>
    <body>
        <div align="center">
            <form action ="TextToSpeechObjectStorage" method="get">
                <input type="text" name="inputText">
                <input type="SUBMIT" value="Save" />
            </form>
			
			<%
				if (request.getAttribute("outputText") != null){
					out.println("<h1>"+request.getAttribute("outputText")+"</h1>");
				}
				
			%>
        </div>
    </body>
</html>