package com.aws.codestar.projecttemplates.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class HelloController {

	@RequestMapping("/")
	public String index() {
		return "ID Extract Health Check!";
	}
	
    @PostMapping("/uploadFile")
    public IDFileResponse uploadFile(@RequestParam("type") String idType, @RequestParam("file") MultipartFile file) throws IOException, Exception {
    	
    	DocumentExtractor documentExtractor = new DocumentExtractor();
    	IDFileResponse idExtract = documentExtractor.AnalyzeDocument( idType, ByteBuffer.wrap(file.getBytes()));

        return idExtract;
    }	

}
