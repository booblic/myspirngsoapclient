package com.terentev.work.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import com.terentev.work.service.SoapService;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

@Configuration
public class ClientConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.terentev.work.file");
        marshaller.setMtomEnabled(true);
        return marshaller;
    }

    @Bean
    public SoapConnector soapConnector(Jaxb2Marshaller marshaller) {
        SoapConnector soapConnector = new SoapConnector(getSaajSoapMessageFactory());
        soapConnector.setMarshaller(marshaller);
        soapConnector.setUnmarshaller(marshaller);
        return soapConnector;
    }

    @Bean
    public SaajSoapMessageFactory getSaajSoapMessageFactory() {
        SaajSoapMessageFactory factory = null;
        try {
            factory = new SaajSoapMessageFactory(MessageFactory.newInstance());
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return factory;
    }
}
