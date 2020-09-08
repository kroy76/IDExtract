package com.aws.codestar.projecttemplates.controller;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.AnalyzeDocumentRequest;
import com.amazonaws.services.textract.model.AnalyzeDocumentResult;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.BoundingBox;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.Point;
import com.amazonaws.services.textract.model.Relationship;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;


public class DocumentExtractor {
	
	EndpointConfiguration endpoint;
	AmazonTextract client;
	
	public DocumentExtractor() {
        endpoint = new EndpointConfiguration(
                "https://textract.us-east-1.amazonaws.com", "us-east-1");
        client = AmazonTextractClientBuilder.standard()
                .withEndpointConfiguration(endpoint).build();
	}
	
    public IDFileResponse  AnalyzeDocument(String idType, ByteBuffer byteBuffer) throws Exception {

        AnalyzeDocumentRequest request = new AnalyzeDocumentRequest()
                .withFeatureTypes("TABLES","FORMS")
                 .withDocument(new Document().withBytes(byteBuffer) );
        
        AnalyzeDocumentResult result = client.analyzeDocument(request);
        IDFileResponse returnValue = this.ExtractFields(idType, result);
        
		return returnValue;

    }

    private IDFileResponse ExtractFields(String idType, AnalyzeDocumentResult resultAWS) {
    	IDFileResponse result = new IDFileResponse();
    	
    	List<Block> blocks = resultAWS.getBlocks();
    	ArrayList<Block> arrayBlocks = new ArrayList<Block>(blocks);
    	
    	int lineNo =0;
    	
    	for(int i=0; i<arrayBlocks.size();i++) {
    		Block block = arrayBlocks.get(i);
    		
    		
    		if(idType.equalsIgnoreCase("passport")){
	    		switch(block.getBlockType()) {
	    		
	    		case "KEY_VALUE_SET":
	    		case "TABLE":
	    		case "CELL":
	    		case "SELECTION_ELEMENT":
	    				break;
	    		case "PAGE":
	    				break;
	    		case "LINE":
	    			
	    			
	    			if(block.getText().equals("P") && arrayBlocks.get(i+1).getText().equals("USA")) {
	    				if(arrayBlocks.get(i+2).getText().matches("[0-9]+")) {
	    					result.getValues().put("IDNumber",arrayBlocks.get(i+2).getText() );
	    				}
	    			}
	    			if(block.getText().contains("Surname") || block.getText().contains("Apellidos")) {
	    				result.getValues().put("Surname",arrayBlocks.get(i+1).getText() );
	    			}
	    			if(block.getText().contains("Given Names") || block.getText().contains("Prenoms")) {
	    				if(arrayBlocks.get(i+1).getText().length()>2) {
	    					result.getValues().put("FirstName",arrayBlocks.get(i+1).getText() );
	    				} else {
	    					result.getValues().put("FirstName",arrayBlocks.get(i+2).getText() );
	    				}
	    			}
	    			if(block.getText().contains("Nationality") || block.getText().contains("Nationalite")) {
	    				result.getValues().put("Nationality",arrayBlocks.get(i+1).getText() );
	    			}
	    			if(block.getText().contains("Date of birth") || block.getText().contains("Date de naissance")) {
	    				result.getValues().put("DOB",arrayBlocks.get(i+1).getText() );
	    			}
	    			if(block.getText().contains("Place of birth") || block.getText().contains("Lieu de naissance")) {
	    				result.getValues().put("BirthPlace",arrayBlocks.get(i+2).getText() );
	    			}

	    			if(block.getText().contains("Sexe") || block.getText().contains("Sexo")) {
	    				result.getValues().put("Sex",arrayBlocks.get(i+2).getText() );
	    			}
	    			if(block.getText().contains("Authority") ) {
	    				result.getValues().put("IssueDate",arrayBlocks.get(i+1).getText() );
	    			}
	    			if(block.getText().contains("Department of State"))  {
	    				result.getValues().put("ExpiryDate",arrayBlocks.get(i+1).getText() );
	    			}
	    			case "WORD":
	    		}
    		}
    		if(idType.equalsIgnoreCase("driverLicense")){
	    		switch(block.getBlockType()) {
	    		
	    		case "KEY_VALUE_SET":
	    		case "TABLE":
	    		case "CELL":
	    		case "SELECTION_ELEMENT":
	    				break;
	    		case "PAGE":
	    				break;
	    		case "LINE":
	    			lineNo++;
	    			if(lineNo==3) {
	    				result.getValues().put("Nationality",arrayBlocks.get(i).getText() );	    				
	    			}
	    			if(block.getText().contains("Licencia") || block.getText().contains("License")) {
	    				result.getValues().put("IDNumber",arrayBlocks.get(i+1).getText().split(" ")[1] );
	    				result.getValues().put("FirstName",arrayBlocks.get(i+2).getText() );
	    				result.getValues().put("Surname",arrayBlocks.get(i+5).getText() );
	    				result.getValues().put("AddressLine1",arrayBlocks.get(i+6).getText() );
	    				result.getValues().put("AddressLine2",arrayBlocks.get(i+7).getText() );
	    				result.getValues().put("AddressLine3",arrayBlocks.get(i+8).getText() );

	    			}
	    			if(block.getText().contains("SEX") || block.getText().contains("HGT/EST")) {
	    				result.getValues().put("Nationality",arrayBlocks.get(i+1).getText().substring(0, 1) );
	    			}
	    			if(block.getText().contains("DOB") || block.getText().contains("NAC")) {
	    				String dateVal = arrayBlocks.get(i).getText().split(" ")[1];
	    				dateVal = dateVal.substring(0,2) + " " + dateVal.substring(2,5) + " " + dateVal.substring(5);
	    				result.getValues().put("DOB", dateVal );
	    			}
	    			if(block.getText().contains("EXP")) {
	    				String dateVal = arrayBlocks.get(i).getText().split(" ")[1];
	    				dateVal = dateVal.substring(0,2) + " " + dateVal.substring(2,5) + " " + dateVal.substring(5);
	    				result.getValues().put("EXP", dateVal );
	    			}
	    			break;
	    			case "WORD":
	    		}
    		}
    		
    	}
    	
    	return result;
    }
    
}
