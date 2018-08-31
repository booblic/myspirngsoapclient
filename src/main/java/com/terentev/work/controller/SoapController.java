package com.terentev.work.controller;

import com.terentev.work.file.GetCommerceMLRequest;
import com.terentev.work.file.GetCommerceMLResponse;
import com.terentev.work.file.GetFileRequest;
import com.terentev.work.file.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.terentev.work.service.SoapService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class SoapController {

    @Autowired
    private SoapService soapService;

    @GetMapping(value = "/file")
    public String setRequest() {
        return "setrequest";
    }

    @PostMapping(value = "/getfile")
    public String getFile(@RequestParam String name, Model model) {

        System.out.println("---------------------------------------");
        System.out.println(name);
        System.out.println("---------------------------------------");

        GetFileRequest request = new GetFileRequest();
        request.setName(name);

        GetFileResponse response = soapService.callFileWebService("http://localhost:8080/ws", request);

        System.out.println("---------------------------------------");
        System.out.println(response.getName());
        System.out.println("---------------------------------------");

        return "fileinfo";
    }

    @GetMapping(value = "/commerceml")
    public String getImport() {
        return "commerceml";
    }

    @PostMapping(value = "/doimport")
    public String doimpert(@RequestParam String name, Model model) {

        return "commercemlinfo";
    }

    @GetMapping(value = "/setcommerceml")
    public String setFile() {
        return "setcommerceml";
    }

    @PostMapping(value = "/loadcommerceml")
    public String loadcommerceml(@RequestParam MultipartFile multipartFile, Model model) {

        File file = new File("import.xml");

        /*try (OutputStream outputStream = new FileOutputStream(file)) {

            InputStream inputStream = multipartFile.getInputStream();

            byte bytes[] = new byte[1024];
            int length;

            while ((length = inputStream.read(bytes)) != -1) {

                outputStream.write(bytes, 0, length);
            }
            inputStream.close();

        } catch (IOException e) {
            System.out.println(e);
        }*/

        GetCommerceMLRequest commerceMLRequest = new GetCommerceMLRequest();

        commerceMLRequest.setName("message");

        GetCommerceMLResponse response = soapService.callCommerceMLImportService("http://localhost:8080/ws", commerceMLRequest, file);

        return "fileinfo";
    }

}
