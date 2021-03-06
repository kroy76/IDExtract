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

	@RequestMapping("/ID")
	public String index() {
		return "ID Extract Health Check!";
	}
	
    @PostMapping("/uploadFile")
    public IDFileResponse uploadFile(@RequestParam("key") String key, @RequestParam("type") String idType, @RequestParam("file") MultipartFile file) throws IOException, Exception {
    	IDFileResponse idExtract = new IDFileResponse();
    	if(key.equals("8c4024e5-a1c8-473a-802c-d633030f6829")) {
	    	DocumentExtractor documentExtractor = new DocumentExtractor();
	    	 idExtract = documentExtractor.AnalyzeDocument( idType, ByteBuffer.wrap(file.getBytes()));
    	}
        return idExtract;
    }	

}
