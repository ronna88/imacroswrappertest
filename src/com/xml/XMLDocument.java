/**
 * @脚本名 XMLDocument.java
 * @功能 XML文档处理器
 * @作者 周忆 zymaxs@126.com
 * @版本 v1.0
 * @最后修改时间 10-11-4 15:00
 */
package com.xml;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * XML Document
 * @author
 */
public final class XMLDocument {

	private XMLBuilder  xmlBuilder  =  null;
	
	
	private XMLParser   xmlParser   =  null;
	
	
	public static XMLDocument  xmlDocument  =  null;
	
	/**
	 * 
	 * @param xmlBuilder   
	 * @param xmlParser    
	 */
	protected XMLDocument( final XMLBuilder xmlBuilder , final XMLParser xmlParser ){
		this.xmlBuilder  =  xmlBuilder;
		this.xmlParser   =  xmlParser;
	}
	
	/**
	 * 获取XML文档 
	 * @param xmlBuilder
	 * @return
	 * @throws Exception
	 */
	public static synchronized XMLDocument getDocument(  final XMLBuilder xmlBuilder ) throws Exception {
		
		
		XMLParser   xmlParser   =  new XMLParser();
		
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		
		factory.setNamespaceAware(false);
		
		
		factory.setValidating(false);
	
		
		SAXParser parser = factory.newSAXParser();
		
		parser.parse(xmlBuilder.getXmlFactory().getXmlFile().getAbsoluteFile(),xmlParser);
		
		xmlDocument  =  new XMLDocument( xmlBuilder, xmlParser );
		
		return xmlDocument;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized static XMLDocument getInstance(){
		return xmlDocument;
	}

	public XMLBuilder getXmlBuilder() {
		return xmlBuilder;
	}
	
	/**
	 * 获取对应XML节点的值
	 * @param name 节点名
	 * @return 节点值
	 */
	public Object getElementByTaName( final String name ){
		LinkedList<Object>  list  =  xmlParser.values.get(name);
		if( list != null ){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取一组对应XML节点的值
	 * @param name 节点名
	 * @return 节点值
	 */
	public Object[] getElementByTaNames( final String name ){
		LinkedList<Object>  list  =  xmlParser.values.get(name);
		if( list != null ){
			return list.toArray();
		}
		return new Object[0];
	}
	
	public void close(){
		if( xmlBuilder != null ){
			xmlBuilder.close();
		}
		xmlBuilder  =  null;
	}
}

/**
 * XML解析器
 * 
 */
class XMLParser extends DefaultHandler{
	

	protected LinkedHashMap<String,LinkedList<Object>>  values  =  null;
	

	private StringBuffer currentValue = new StringBuffer();
	
	public XMLParser() {
		this.values = new LinkedHashMap<String,LinkedList<Object>>();
	}
	

	public void startElement(String uri, String localName, String qName, Attributes attributes)
	throws SAXException {
		currentValue.delete(0, currentValue.length());
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		currentValue.append(ch, start, length);
	}
	

	public void endElement(String uri, String localName, String qName) throws SAXException {
		LinkedList<Object>  list  =  values.get(qName);
		if( list == null ){
			list  =  new  LinkedList<Object>();
			values.put(qName, list);
		}
		list.add(currentValue.toString());
		//System.err.println("qName: "+qName+" value: "+currentValue);
	}
}
