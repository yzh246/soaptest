package com.ourlead.service;

import com.ourlead.utils.ParseXML;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * @author Salvatore
 * @date 2023/3/31
 */
@Service
public class SOAPService {

  public static String sendSOAPRequestAndParseResponse(String endpoint, String soapRequest) throws Exception {
    // create connection
    URL url = new URL(endpoint);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
    connection.setDoOutput(true);

    // send request
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    writer.write(soapRequest);
    writer.flush();
    writer.close();

    // parse response
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(connection.getInputStream());

    // extract data from response
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();
    xPath.setNamespaceContext(new NamespaceContext() {
      @Override
      public String getNamespaceURI(String prefix) {
        if ("soap".equals(prefix)) {
          return "http://www.w3.org/2003/05/soap-envelope";
        }
        else {
          return null;
        }
      }

      @Override
      public String getPrefix(String namespaceURI) {
        return null;
      }

      @Override
      public Iterator getPrefixes(String namespaceURI) {
        return null;
      }
    });
    XPathExpression expr = xPath.compile("//soap:Envelope/soap:Body/LoginVerifyServiceResponse/LoginVerifyServiceResult/ReturnStatus/text()");
    String returnStatus = (String) expr.evaluate(doc, XPathConstants.STRING);

    return returnStatus;
  }

  public String connect(String username, String password) {
    try {
      // 创建SOAP请求报文
      MessageFactory factory = MessageFactory.newInstance(javax.xml.soap.SOAPConstants.SOAP_1_2_PROTOCOL);
      SOAPMessage message = factory.createMessage();
      SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
      envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      envelope.addNamespaceDeclaration("soap12", "http://www.w3.org/2003/05/soap-envelope");
      SOAPBody body = envelope.getBody();
      body.addChildElement(envelope.createName("LoginVerifyService", "", "http://CORP.NOVOCORP.NET/")).addChildElement("pUserName").addTextNode(username).getParentElement().addChildElement("pPassword").addTextNode(password);

      // 设置SOAP请求报文头
      URL url = new URL("https://appcntj037.corp.novocorp.net/SmartInterface.asmx?op=LoginVerifyService");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
      connection.setRequestProperty("SOAPAction", "CORP.NOVOCORP.NET/LoginVerifyService");
      connection.setDoOutput(true);

      // 发送SOAP请求报文并接收响应报文
      message.writeTo(connection.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      StringBuilder responseBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        responseBuilder.append(line);
      }
      String response = responseBuilder.toString();
      System.out.println("----------SOAP Resopnse------------- \n" + response);
      ParseXML.testXML(response);
      return response;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
