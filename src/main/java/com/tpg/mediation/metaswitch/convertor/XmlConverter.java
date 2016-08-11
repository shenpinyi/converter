package com.tpg.mediation.metaswitch.convertor;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public abstract class XmlConverter extends Converter {
	
	Logger logger = LoggerFactory.getLogger(XmlConverter.class);
	
	File xsdFile;
	
	@Override
	protected boolean doCheckFile() {

		if (xsdFile == null) {
			xsdFile = doGetXsdFile();
		}
		
		if (xsdFile == null) {
			logger.error("Cannot find XSD file for checking");
			return false;
		}
		
		return checkXmlFile(this.currentInFile, xsdFile);
	}
	
	private boolean checkXmlFile(File inFile, File xsdFile) {
		
		Source xmlFile = new StreamSource(inFile);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema;
		try {
			schema = schemaFactory.newSchema(xsdFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			logger.info(xmlFile.getSystemId() + " is valid");
			return true;
		} catch (SAXException | IOException e) {
			logger.error(xmlFile.getSystemId() + " is NOT valid. Reason: " + 
		           e.getLocalizedMessage());
			return false;
		}
	}
	
	protected abstract File doGetXsdFile();
	
}
