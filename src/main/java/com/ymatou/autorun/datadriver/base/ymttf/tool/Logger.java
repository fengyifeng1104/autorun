package com.ymatou.autorun.datadriver.base.ymttf.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.ymatou.autorun.datadriver.base.utils.FileUtil;

/************************************************************************************
 * 用于处理日志
 * 
 * @File name : Logger.java
 * @Author : zhouyi
 * @Date : 2015年3月11日
 * @Copyright : 洋码头
 ************************************************************************************/
@SuppressWarnings("deprecation")
public class Logger {

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();
    private static boolean debug = true;
    static boolean flag = true;
    static String message = "";

    private static void buildDocument(Document document) {
        Map<String, Object> map = threadLocal.get();
        map.put("doc", document);
    }

    private static Document getDocument() {
        Map<String, Object> map = threadLocal.get();
        if (map == null)
            return null;
        return (Document) map.get("doc");
    }

    private static void buildXMLWriter(XMLWriter writer) {
        Map<String, Object> map = threadLocal.get();
        map.put("writer", writer);
    }

    private static XMLWriter getXMLWriter() {
        Map<String, Object> map = threadLocal.get();
        return (XMLWriter) map.get("writer");
    }

    private static void buildElementTestsuite(Element testSuiteElement) {
        Map<String, Object> map = threadLocal.get();
        map.put("testSuiteElement", testSuiteElement);
    }

    private static Element getElementTestsuite() {
        Map<String, Object> map = threadLocal.get();
        return (Element) map.get("testSuiteElement");
    }

    private static void buildElementTest(Element testCaseElement) {
        Map<String, Object> map = threadLocal.get();
        map.put("testCaseElement", testCaseElement);
    }

    private static Element getElementTest() {
        Map<String, Object> map = threadLocal.get();
        if (map == null)
            return null;
        return (Element) map.get("testCaseElement");
    }

    /**
     * 最后把日志写入文件，关闭流<br>
     * 建议在AfterClass调用
     */
    public static void generateResult(String apiName) {
        try {
            getDocument().getRootElement().addAttribute("url", apiName);
            getXMLWriter().write(getDocument());
            getXMLWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 最后把日志写入文件，关闭流<br>
     * 建议在AfterClass调用
     */
    @Deprecated
    public static void generateResult() {
        try {
            getXMLWriter().write(getDocument());
            getXMLWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建日志文件<br>
     * 建议在BeforeClass时调用
     * 
     * @param description 该日志对应描述，此参数暂时没有作用
     */
    public static void createResultFile(String folderName,String suiteName) {
        threadLocal.set(new HashMap<String, Object>());
        Document doc = DocumentHelper.createDocument();
        // 将Document 对象跟当前线程绑定
        buildDocument(doc);
        Element testsuite = getDocument().addElement("Testsuite");
        // 将 Element testsuite 对象跟当前线程绑定
        buildElementTestsuite(testsuite);
        //getElementTestsuite().addAttribute("description", description);
        try {
        	
        	System.out.println(System.getProperty("user.dir"));
        	FileUtil.createFolderIfNotExist("./Results/" + folderName);
        	XMLWriter writer =
                     new XMLWriter(new OutputStreamWriter(new FileOutputStream("./Results/" + folderName + "/" + suiteName
                             + ".xml"), "UTF-8"));
        	
        	
           /* XMLWriter writer =
                    new XMLWriter(new OutputStreamWriter(new FileOutputStream("./Results/" + folder + "/" + suitename
                            + ".xml"), "UTF-8"));*/
            // 将Document 对象跟当前线程绑定
            buildXMLWriter(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        debug(getTestsuiteName() + " " + getTestName());
       // debug(description);
    }

    /**
     * 添加一个comment
     * 
     * @param message comment信息
     */
    public static void comment(String message) {
        Element comment = null;
        if (getDocument() != null && getElementTest() != null) {
            comment = getElementTest().addElement("Comment");
            comment.addAttribute("timestamp", getNow());
            comment.addText(message);
        }
        debug(message);
    }


    /**
     * 打印到控制台 --debug为true时输出 System.err.println
     * 
     * @param message
     */
    public static void debug(Object message) {
        if (debug) {
            String name = Thread.currentThread().getName();
            /*
             * System.out.println("Thread:" + name + " " + getNow() + " " + getTestsuiteName() + " "
             * + getTestName() + " " + getLineNum());
             */
            System.out.println("Thread:" + name + " " + getNow() + " " + message.toString());
        }
    }

    /**
     * 开始记录Testcase，在case开始时调用
     * 
     * @param type 正反例 正例true
     * @param description Testcase描述
     * 
     */
    public static void start(boolean type, Integer caseId,String description) {
        flag = true;
        message = "";
        
        //testname 就是testid
        String testname = String.valueOf(caseId);
        Element test = getElementTestsuite().addElement("Testcase");
        // 将 Element test 对象跟当前线程绑定
        buildElementTest(test);
        getElementTest().addAttribute("name", testname);
        getElementTest().addAttribute("description", description);
        getElementTest().addAttribute("type", String.valueOf(type ? "正例" : "反例"));
        getElementTest().addAttribute("result", "0");
        Element comment = getElementTest().addElement("Comment");
        comment.addText("开始执行测试用例:" + testname);
        comment.addAttribute("timestamp", getNow());
        debug("开始执行测试用例:" + testname);
    }

    /**
     * 开始记录Testcase，在case开始时调用
     * 
     * @param description Testcase描述
     * @deprecated
     */
    public static void start(String description) {
        flag = true;
        message = "";
        String testname = getTestName();
        Element test = getElementTestsuite().addElement("Testcase");
        // 将 Element test 对象跟当前线程绑定
        buildElementTest(test);
        getElementTest().addAttribute("name", testname);
        getElementTest().addAttribute("description", description);
        getElementTest().addAttribute("result", "0");
        Element comment = getElementTest().addElement("Comment");
        comment.addText("开始执行测试用例:" + testname);
        debug("开始执行测试用例:" + testname);
    }

    /**
     * 结束case，在After调用
     */
    public static void end() {
        if (!flag) {
            org.junit.Assert.fail(message);
            debug(message);
        }

    }

    /**
     * 描述case失败
     * 
     * @param exception 失败异常
     */
    public static void fail(Exception exception) {
        exception.printStackTrace();
        Element error = getElementTest().addElement("Error");
        error.addAttribute("timestamp", getNow());
        StackTraceElement exmsg = exception.getStackTrace()[0];
        message += new SimpleDateFormat("yyyyMMddhhmmssSSS").format(Calendar.getInstance().getTime()) + ":" + "\r\n";
        error.addText("错误:" + exmsg.toString());
        message += "错误:" + exmsg.toString() + ":" + "\r\n";
        error.addText("+++++错误信息:" + exception.getMessage());
        message += "+++++错误信息:" + exception.getMessage() + ":" + "\r\n";
        getElementTest().setAttributeValue("result", "1");
        debug("Error:" + exmsg.toString() + "+++++ExceptionMsg:"
                + exception.getMessage());
        org.junit.Assert.fail();
    }

    /**
     * 描述case设置阶段日志
     * 
     * @param msg setup信息
     * @param pass setup是否成功 0成功 其他失败
     */
    public static void setup(String msg, int pass) {
        Element setup = getElementTestsuite().addElement("Setup");
        setup.addText(msg);
        if (pass == 0) {
            setup.addAttribute("result", "0");
            debug(msg + ":pass");
        } else {
            setup.addAttribute("result", "1");
            debug(msg + ":failed");
        }
    }

    /**
     * 描述case Cleanup阶段日志
     * 
     * @param msg Cleanup信息
     * @param pass Cleanup是否成功 0成功 其他失败
     */
    public static void cleanup(String msg, int pass) {
        Element setup = getElementTestsuite().addElement("Cleanup");
        setup.addText(msg);
        if (pass == 0) {
            setup.addAttribute("result", "0");
            debug(msg + ":pass");
        } else {
            setup.addAttribute("result", "1");
            debug(msg + ":failed");
        }
    }

    /**
     * 断言期望和实际相等
     * 
     * @param expected 期望
     * @param actual 实际
     * @param Description 断言描述
     */
    public static boolean verifyEquals(Object expected, Object actual, String Description) {
    	boolean vflag=true;
        Element checkpoint = getElementTest().addElement("Checkpoint");
        checkpoint.addAttribute("timestamp", getNow());
        // checkpoint.addAttribute("description", Description);
        if (expected == null || actual == null) {
            if (expected == actual) {
                checkpoint.addAttribute("result", "0");
                checkpoint.addText(Description + ":" + "Expected:" + null + "; Actual:" + null);
            } else {
                checkpoint.addAttribute("result", "1");
                checkpoint.addText(Description + ":" + "Expected:" + expected + "; Actual: " + actual);
                checkpoint.setAttributeValue("result", "1");
                getElementTest().setAttributeValue("result", "1");
                flag = false;
                vflag=false;
                message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
                message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
                message += "\n" + Description + "\t期望：" + expected + " 实际：" + actual;
            }
        } else if (expected.equals(actual)) {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
        } else {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
            checkpoint.setAttributeValue("result", "1");
            getElementTest().setAttributeValue("result", "1");
            flag = false;
            vflag=false;
            message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
            message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
            message += "\n" + Description + "\t期望：" + expected.toString() + " 实际：" + actual.toString();
            // assertEquals(expected, actual);
            /*
             * if (debug) System.out.println(getNow() + " " + message);
             */
            debug(message);
        }
        
        return vflag;
    }

    /**
     * 断言期望和实际不相等
     * 
     * @param expected 期望
     * @param actual 实际
     * @param Description 断言描述
     */
    public static boolean verifyNotEquals(Object expected, Object actual, String Description) {
    	boolean vflag=true;
        Element checkpoint = getElementTest().addElement("Checkpoint");
        // checkpoint.addAttribute("description", Description);
        checkpoint.addAttribute("timestamp", getNow());
        if (!expected.equals(actual)) {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            if (actual == null)
                actual = "";
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
        } else {
            checkpoint.addAttribute("result", "1");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
            checkpoint.setAttributeValue("result", "1");
            getElementTest().setAttributeValue("result", "1");
            flag = false;
            vflag=false;
            message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
            message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
            message += "\n" + "期望：" + expected.toString() + " 不等于 实际：" + actual.toString();
            // assertEquals(expected, actual);
            /*
             * if (debug) System.out.println(getNow() + " " + message);
             */
            debug(message);
        }
        return vflag;
    }

    /**
     * 断言期望对象非空
     * 
     * @param obj 期望对象
     * @param Description 断言描述
     */
    public static boolean verifyNotNull(Object obj, String Description) {
    	boolean vflag=true;
        Element checkpoint = getElementTest().addElement("Checkpoint");
        checkpoint.addAttribute("timestamp", getNow());
        checkpoint.addText("非空验证：");
        // checkpoint.addAttribute("description", Description);
        if (obj != "null" && obj != null && obj.toString().length() != 0) {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":Expected: NotNull  Actual: NotNull:" + obj.toString());
        } else {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":Expected:Not null" + "; Actual:Null");
            checkpoint.setAttributeValue("result", "1");
            getElementTest().setAttributeValue("result", "1");
            // assertNotNull("Assert object not null", obj);
            flag = false;
            vflag=false;
            message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
            message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
            message += "\n" + "Expected：NotNull  Actual：Null";
            message += "	" + Description;
            /*
             * if (debug) System.out.println(getNow() + " " + message);
             */

            debug(message);
        }
        return vflag;
    }

    /**
     * 断言期望对象为空
     * 
     * @param obj 期望对象
     * @param Description 断言描述
     */
    public static boolean verifyIsNull(Object obj, String Description) {
    	boolean vflag=true;
        Element checkpoint = getElementTest().addElement("Checkpoint");
        checkpoint.addAttribute("timestamp", getNow());
        // checkpoint.addAttribute("description", Description);
        if (obj == null || obj == "null" || obj.toString().length() == 0) {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":Expected:Null" + "; Actual:Null");
        } else {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":Expected:null" + "; Actual:Not Null");
            checkpoint.setAttributeValue("result", "1");
            getElementTest().setAttributeValue("result", "1");
            // assertNull("Assert value is null or empty ", obj);
            flag = false;
            vflag=false;
            message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
            message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
            message += "\n" + "期望：Null  实际：" + obj.toString();
            message += "	" + Description;
            /*
             * if (debug) System.out.println(getNow() + " " + message);
             */
            debug(message);
        }
        return vflag;
    }

    /**
     * 断言实际内容中包含期望信息
     * 
     * @param expected 期望
     * @param actual 实际
     * @param Description 断言描述
     */
    public static boolean verifyIsContains(Object expected, Object actual, String Description) {
    	boolean vflag=true;
        Element checkpoint = getElementTest().addElement("Checkpoint");
        checkpoint.addAttribute("timestamp", getNow());
        // checkpoint.addAttribute("description", Description);
        if (expected == null || actual == null) {
            if (expected.toString().contains(actual.toString()) && expected.toString().length() > 0
                    && actual.toString().length() > 0) {
                checkpoint.addAttribute("result", "0");
                checkpoint.addText(Description + ":" + "Expected:" + null + "; Actual:" + null);
            } else {
                checkpoint.addAttribute("result", "1");
                checkpoint.addText(Description + ":" + "Expected:" + expected + "; Actual: " + actual);
                checkpoint.setAttributeValue("result", "1");
                getElementTest().setAttributeValue("result", "1");
                flag = false;
                vflag=false;
                message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
                message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
                message += "\n" + Description + "\t期望：" + expected + " 实际：" + actual;
            }
        } else if (actual.toString().contains(expected.toString())) {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
        } else {
            checkpoint.addAttribute("result", "0");
            // Element msg = checkpoint.addElement("Message");
            checkpoint.addText(Description + ":" + "Expected:" + expected.toString() + "; Actual:" + actual.toString());
            checkpoint.setAttributeValue("result", "1");
            getElementTest().setAttributeValue("result", "1");
            flag = false;
            vflag=false;
            message += "\nClassName:" + Thread.currentThread().getStackTrace()[2].getClassName();
            message += "\nLine:" + Thread.currentThread().getStackTrace()[2].getLineNumber();
            message += "\n" + Description + "\t期望：" + expected.toString() + " 实际：" + actual.toString();
            // assertEquals(expected, actual);
            /*
             * if (debug) System.out.println(getNow() + " " + message);
             */
            debug(message);

        }
        return vflag;
    }

    /**
     * 获取 getTestName
     * 
     * @return 调用日志的测试方法的MethodName
     */
    private static String getTestName() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    /**
     * 获取 getLineNum
     * 
     * @return 调用日志的测试方法的getLineNumber
     */
    private static int getLineNum() {
        return Thread.currentThread().getStackTrace()[3].getLineNumber();
    }

    /**
     * 获取 getTestsuiteName
     * 
     * @return 调用日志的测试类的ClassName
     */
    private static String getTestsuiteName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    /**
     * 获取当前日志是否为debug模式
     * 
     * @return
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * 设置当前日志是否为debug模式
     * 
     * @param debug
     */
    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    /**
     * 获取当前时间
     * 
     * @return {yyyy-MM-dd HH:mm:ss.SSS}
     */
    private static String getNow() {
        Calendar calendar = Calendar.getInstance();
        String strSysTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(calendar.getTime());
        return strSysTime;
    }
}
