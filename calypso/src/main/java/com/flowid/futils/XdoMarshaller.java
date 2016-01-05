package com.flowid.futils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowid.xdo.cmn.Param;

/**
 * Marshal and unmarshal XML structures. Wrapper around the JAXB marshaller.
 * 
 * The marshal/unmarshall in general are not thread-safe, but the methods exposed by this class
 * (fromXml and toXml) are thread safe -
 * 
 * @see http://jaxb.java.net/guide/Performance_and_thread_safety.html for a description of the
 *      issues.
 */
public class XdoMarshaller {
    private static Logger logger = LoggerFactory.getLogger(XdoMarshaller.class);
    public static String DEFAULT_NAMESPACE = "urn:flowid.com:cmn:v1";
    public static String DEFAULT_PACKAGE_NAME = Param.class.getPackage().getName();

    private JAXBContext jc = null;
    private String packageName = null;
    private String namespace = null;

    public XdoMarshaller() {
        this(DEFAULT_NAMESPACE, DEFAULT_PACKAGE_NAME);
    }

    public XdoMarshaller(String namespace, Package pkg) {
        this.namespace = namespace;
        this.packageName = pkg.getName();
    }

    public XdoMarshaller(String namespace, String packageName) {
        this.namespace = namespace;
        this.packageName = packageName;
    }

    private JAXBContext getJAXBContext() throws JAXBException {
        if (jc == null) {
            // System.setProperty("com.sun.xml.bind.v2.runtime.JAXBContextImpl.fastBoot", "true");
            // PerfTimer timer = new PerfTimer(MarshallerUtil.class, "getJAXBContext");
            // timer.start();
            jc = JAXBContext.newInstance(packageName);
            // timer.stop();
        }
        return jc;
    }

    public void setProperty(String name, Object value) {
        try {
            getMarshaller().setProperty(name, value);
        }
        catch (Exception e) {
            logger.error("While setting the property " + name + " to " + value + ": " + e.getMessage());
        }
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        return getJAXBContext().createUnmarshaller();
    }

    private Marshaller getMarshaller() throws JAXBException {
        Marshaller marshaller = getJAXBContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        // marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        return marshaller;
    }

    public <T> void toXml(T o, File f) throws JAXBException, IOException {
        logger.debug("Saving to file: " + f.getPath().replace("\\", "/"));
        try (FileOutputStream fos = new FileOutputStream(f)) {
            toXml(o, fos);
        }
    }

    public <T> void toXml(T o, OutputStream os) throws JAXBException {
        /**
         * XSD Complex types cannot be marshalled because they don't have XmlRoot declaration.
         * To avoid this issue, we force the root element on the output XML to be the same as
         * the class name without the suffix "Type"
         */
        JAXBElement<T> ce = new JAXBElement<T>(new QName(namespace, getRootName(o)), (Class<T>) o.getClass(), o);
        getMarshaller().marshal(ce, os);
    }

    public <T> T fromXml(File f) throws JAXBException, IOException {
        InputStream is = new FileInputStream(f);
        try {
            T t = fromXml(is);
            is.close();
            return t;
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public <T> T fromXml(InputStream is) throws JAXBException {
        Unmarshaller um = getUnmarshaller();
        Object o = um.unmarshal(is);
        T t = null;
        /**
         * To understand why we need this casting see:
         * http://weblogs.java.net/blog/kohsuke/archive/2006/03/why_does_jaxb_p.html
         */
        if (o.getClass().getName().endsWith("JAXBElement")) {
            t = ((JAXBElement<T>) o).getValue();
        }
        else {
            t = (T) o;
        }
        return t;
    }

    public <T> T clone(T o) throws JAXBException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        toXml(o, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        T clone = fromXml(is);
        return clone;
    }

    static private String getRootName(Object o) {
        String root = o.getClass().getSimpleName();
        if (root.endsWith("Type")) {
            root = root.substring(0, root.length() - 4);
        }
        return root;
    }

    public <T> String toXmlString(T o) {
        if (o == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            toXml(o, baos);
            String s = new String(baos.toByteArray(), "UTF8");
            return s;
        }
        catch (JAXBException | UnsupportedEncodingException e) {
            logger.error("Exception while serializing the object to string " + e.getMessage());
            logger.debug("Details: ", e);
        }
        return null;
    }

    static public String escapeXml(String xml) {
        String es = xml;
        es = es.replace("&", "amp;");
        es = es.replace(">", "&gt;");
        es = es.replace("<", "&lt;");
        es = es.replace("'", "&apos;");
        es = es.replace("\"", "&quot;");
        es = es.replace("\n", "<br>");
        // es = es.replace(" ", "&emsp;");
        return es;
    }
}
