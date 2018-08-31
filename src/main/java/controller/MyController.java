package controller;

import file.GetFileRequest;
import file.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.SOAPConnector;

@Controller
public class MyController {

    @Autowired
    private SOAPConnector soapConnector;

    @GetMapping(value = "/file")
    public String setRequest() {
        return "setrequest";
    }

    @PostMapping(value = "/getfile")
    public String getFile(@RequestParam String name, Model model) {

        GetFileRequest request = new GetFileRequest();
        request.setName(name);

        GetFileResponse response = soapConnector.callFileWebService("http://localhost:8080/ws", request);

        return "fileinfo";
    }
}

