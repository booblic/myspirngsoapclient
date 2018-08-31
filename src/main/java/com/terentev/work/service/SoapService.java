package com.terentev.work.service;

import com.terentev.work.file.GetCommerceMLRequest;
import com.terentev.work.file.GetCommerceMLResponse;
import com.terentev.work.file.GetFileRequest;
import com.terentev.work.file.GetFileResponse;
import com.terentev.work.system.SoapConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.support.MarshallingUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.transform.TransformerException;
import javax.xml.ws.handler.MessageContext;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class SoapService {

    @Autowired
    private SoapConnector soapConnector;

    private GetFileResponse response = null;

    private GetCommerceMLResponse commerceMLResponse = null;

    public GetFileResponse callFileWebService(String url, GetFileRequest request) {

        soapConnector.getWebServiceTemplate().sendAndReceive(url, new WebServiceMessageCallback() {

            @Override
            public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {

                MarshallingUtils.marshal(soapConnector.getMarshaller(), request, webServiceMessage);
            }
        }, new WebServiceMessageExtractor<DataHandler>() {
            @Override
            public DataHandler extractData(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
                Iterator attachmentIterator = ((SaajSoapMessage) webServiceMessage).getAttachments();
                Set<Attachment> attachmentSet = new HashSet<>();
                if (attachmentIterator != null) {

                    while (attachmentIterator.hasNext()) {

                        attachmentSet.add((Attachment) attachmentIterator.next());
                    }
                }
                //String filePath = extractFileFromAttachment(attachmentSet);

                response = (GetFileResponse) MarshallingUtils.unmarshal(soapConnector.getUnmarshaller(), webServiceMessage);
                //response.setName(filePath);

                return null;
            }
        });
        return response;
    }

    private String extractFileFromAttachment(Set<Attachment> attachmentSet) {

        File file = new File("test.xml");

        for (Attachment attachment : attachmentSet) {

            try (OutputStream outputStream = new FileOutputStream(file)) {

                InputStream inputStream = attachment.getInputStream();

                byte bytes[] = new byte[1024];
                int length;

                while ((length = inputStream.read(bytes)) != -1) {

                    outputStream.write(bytes, 0, length);
                }
                inputStream.close();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return file.getPath();
    }

    public GetCommerceMLResponse callCommerceMLImportService(String url, GetCommerceMLRequest request, File file) {

/*        commerceMLResponse = (GetCommerceMLResponse) soapConnector.
                getWebServiceTemplate().
                marshalSendAndReceive(
                        url,
                        request);

        System.out.println("---------------------------------------");
        System.out.println(commerceMLResponse.getName());
        System.out.println("---------------------------------------");
        return commerceMLResponse;*/
        soapConnector.
                getWebServiceTemplate().
                sendAndReceive(
                        url,
                        new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
                SaajSoapMessage soapMessage = (SaajSoapMessage) webServiceMessage;
                DataHandler dataHandler = new DataHandler(new FileDataSource("import.xml"));
                soapMessage.addAttachment("xml", dataHandler);

                MarshallingUtils.marshal(soapConnector.getMarshaller(), request, soapMessage);
            }
        }, new WebServiceMessageExtractor<DataHandler>() {
            @Override
            public DataHandler extractData(WebServiceMessage webServiceMessage) throws IOException, TransformerException {

                commerceMLResponse = (GetCommerceMLResponse) MarshallingUtils.unmarshal(soapConnector.getUnmarshaller(), webServiceMessage);
                return null;
            }
        });

        System.out.println("---------------------------------------");
        System.out.println(commerceMLResponse.getName());
        System.out.println("---------------------------------------");
        return commerceMLResponse;
    }

}
