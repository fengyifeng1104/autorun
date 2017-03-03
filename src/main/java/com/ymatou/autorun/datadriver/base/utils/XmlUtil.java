package com.ymatou.autorun.datadriver.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/************************************************************************************
 * 用于处理xml格式
 * 
 * @File name : XmlUtil.java
 * @Author : zhouyi
 * @Date : 2015年3月11日
 * @Copyright : 洋码头
 ************************************************************************************/
// TODO 目前暂时没有用而且以后可能考虑重做xml报文的处理方式
public class XmlUtil {
	private SAXReader reader = new SAXReader();
	private InputStream in = null;
	private Document doc = null;
	private Element root = null;

	/**
	 * 功能说明：使用xml文件初始化
	 * 
	 * @param path
	 *            文件路径
	 * @throws FileNotFoundException 
	 * @throws DocumentException 
	 */
	public XmlUtil(String path) throws FileNotFoundException, DocumentException {
		in = new FileInputStream(new File(path));
		doc = reader.read(in);
		root = doc.getRootElement();
	}

	/**
	 * 功能说明：使用InputStream初始化
	 * 
	 * @param ins
	 * @throws DocumentException
	 */
	public XmlUtil(InputStream ins) throws DocumentException {

		doc = reader.read(ins);

		root = doc.getRootElement();

	}

	/**
	 * 功能说明：更新xml字段
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 * @param value
	 *            需要更新的值
	 */
	public void updateElementValue(String element, String value) {
		String[] path = element.split("//");
		Element n = root;
		if (root != null) {
			try {
				for (String s : path) {
					n = n.element(s);
				}
				if (value != null) {
					n.setText(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能说明：删除xml节点
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 */
	public void deleteElement(String element) {
		String[] path = element.split("//");
		Element n = root;
		try {
			for (String s : path) {
				n = n.element(s);
			}
			n.getParent().remove(n);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能说明：获取节点的值
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 * @return 对应的值
	 */
	public String getElementValue(String element) {
		String[] path = element.split("//");
		Element n = root;
		String text = "";
		try {
			for (String s : path) {
				n = n.element(s);
				if (n == null)
					return null;
			}
			text = n.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return text;
	}

	/**
	 * 功能说明：以xml格式获取节点下全部数据
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 * @return xml格式节点数据
	 */
	public String getElementXml(String element) {
		String[] path = element.split("//");
		Element n = root;
		String text = "";
		try {
			for (String s : path) {
				n = n.element(s);
			}
			text = n.asXML();
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}

		return text;
	}

	/**
	 * 功能说明：以xml格式获取节点下全部数据
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 * @param num
	 *            第几个相同名节点
	 * @return xml格式节点数据
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getElementList(String element) {
		String[] path = element.split("//");
		Element n = root;
		List<Element> ns1 = null;
		try {
			for (int i = 0; i < path.length; i++) {
				if (i < path.length - 1) {
					n = n.element(path[i]);
				} else {
					ns1 = n.elements(path[i]);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}

		return ns1;
	}

	/**
	 * 功能说明：新增节点
	 * 
	 * @param element
	 *            节点路径 例如Body//customerRegister//arg0//channel
	 * @param Value
	 *            要设置的值
	 */
	public void addElement(String element, String Value) {
		String[] path = element.split("//");
		Element n = root;
		for (String s : path) {

			if (null != n.element(s)) {
				n = n.element(s);
			} else {
				n.addElement(s);
				n = n.element(s);
			}
		}
		n.setText(Value);
	}

	/**
	 * 功能说明：获取整个xml
	 * 
	 * @return xml
	 */
	public String getXML() {
		return root.asXML();
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	
}
