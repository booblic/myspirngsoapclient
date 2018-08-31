package service;

import file.GetFileRequest;
import file.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.*;

@Service
public class SOAPConnector extends WebServiceGatewaySupport {

    @Autowired
    public SOAPConnector(SaajSoapMessageFactory messageFactory) {
        super(messageFactory);
    }

    private GetFileResponse response = null;

    private Set<GetFileResponse> fileResponseSet = new HashSet<>();

    public GetFileResponse callFileWebService(String url, GetFileRequest request) {

        getWebServiceTemplate().sendAndReceive(url, webServiceMessage -> MarshallingUtils.marshal(getMarshaller(), request, webServiceMessage), (WebServiceMessageExtractor<DataHandler>) webServiceMessage -> {

            Iterator attachmentIterator = ((SaajSoapMessage) webServiceMessage).getAttachments();
            Set<Attachment> attachmentSet = new HashSet<>();
            if (attachmentIterator != null) {

                while (attachmentIterator.hasNext()) {

                    attachmentSet.add((Attachment) attachmentIterator.next());
                }
            }
            String filePath = extractFileFromAttachment(attachmentSet);

            response = (GetFileResponse) MarshallingUtils.unmarshal(getUnmarshaller(), webServiceMessage);
            response.setName(filePath);

            return null;
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
}
