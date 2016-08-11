package com.tpg.mediation.metaswitch.convertor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Component
public class CfsXmlSaxParser extends XmlParser {
	
	Logger logger = LoggerFactory.getLogger(CfsXmlSaxParser.class);

	// use it for stack to store the tags
	LinkedList<String> callTags;
	LinkedList<String> headerTags;
	LinkedList<String> footerTags;

	CfsParsedFile parsedFile;


	private String getFieldName(List<String> fields) {
		StringBuffer buff = new StringBuffer();

		for (int i = fields.size() - 1; i > 0; i--) {
			buff.append(fields.get(i) + "_");
		}

		buff.append(fields.get(0));

		return buff.toString();
	}

	@Override
	public ParsedFile parse(File inFile) {
		
		
		parsedFile = new CfsParsedFile();
		parsedFile.setOriginalName(inFile.getName().replace("\\.xml|\\.XML", ""));

		Map<String, String> headers = new HashMap<>();
		parsedFile.setHeader(headers);

		Map<String, String> footers = new HashMap<>();
		parsedFile.setFooter(footers);

		List<Map<String, String>> calls = new ArrayList<>();
		parsedFile.setCalls(calls);
		
		Map<String, Integer> arrayTags = new HashMap<>();
		
		arrayTags.put("ReleaseReason", 0);
		arrayTags.put("UDA", 0);
		arrayTags.put("NPInfo", 0);

		callTags = new LinkedList<>();
		headerTags = new LinkedList<>();
		footerTags = new LinkedList<>();

		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				int recordType = 0; // 0: not sure; 1: header; 2:call; 3: footer
				LinkedList<String> currentTags;
				Map<String, String> currentFields;
				String tagName = null;
				

				@Override
				public void characters(char[] ch, int start, int length) throws SAXException {
					boolean processTag = false;

					if (tagName != null) {

						for (int i = start; i < (start + length); i++) {
							if (!Character.isWhitespace(ch[i])) {
								processTag = true;
								break;
							}
						}
						
						if (processTag) {
							currentFields.put(getFieldName(currentTags).toLowerCase().replaceAll("\\W", ""), 
									new String(ch, start, length));
							tagName = null;
						}

					}
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					switch (qName.toUpperCase()) {
					case "FILEHEADER":
						recordType = 1;
						currentTags = headerTags;
						currentFields = parsedFile.getHeader();
						break;
					case "CALL":
						recordType = 2;
						currentTags = callTags;
						currentFields = new HashMap <> ();
						parsedFile.getCalls().add(currentFields);
						break;
					case "FILEFOOTER":
						recordType = 3;
						currentTags = footerTags;
						currentFields = parsedFile.getFooter();
						break;
					default:
						;
					}

					if (recordType != 0) {
						tagName = qName;
						if (arrayTags.containsKey(qName)) {
							arrayTags.put(qName, arrayTags.get(qName) + 1);
							tagName = qName + "_" + arrayTags.get(qName);
						}
						currentTags.push(tagName);
						for (int i = 0; i < attributes.getLength(); i++) {
							currentFields.put(getFieldName(currentTags).toLowerCase() + "_" + 
						             attributes.getQName(i).toLowerCase().replaceAll("\\W", ""), attributes.getValue(i));
						}
					}

				}

				@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {

					if (recordType != 0) {
						currentTags.pop();
					}

					switch (qName.toUpperCase()) {
					case "FILEHEADER":
					case "CALL":
					case "FILEFOOTER":
						recordType = 0;
						arrayTags.put("ReleaseReason", 0);
						arrayTags.put("UDA", 0);
						arrayTags.put("NPInfo", 0);
						break;
					default:
						;
					}
				}

				@Override
				public void endDocument() throws SAXException {
					logger.debug(parsedFile.getHeader().toString());
					logger.debug(parsedFile.getCalls().toString());
					logger.debug(parsedFile.getFooter().toString());
				}

			};

			saxParser.parse(inFile.getAbsolutePath(), handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parsedFile;
	}

}
