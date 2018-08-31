package com.terentev.work.system;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class SoapConnector extends WebServiceGatewaySupport {

    public SoapConnector(SaajSoapMessageFactory messageFactory) {
        super(messageFactory);
    }
}
