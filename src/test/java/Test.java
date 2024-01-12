import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setNamespaceAware(false); // 忽略命名空间
        //factory.setValidating(false); // 不验证DTD
        //factory.setExpandEntityReferences(false); // 不展开实体引用
        DocumentBuilder builder  = factory.newDocumentBuilder();
        // 解析XML文件并获取Document对象
        File xmlFile =  new ClassPathResource("/reportconfig_def/wabacus.cfg.xml").getFile();
        Document document = builder.parse(xmlFile);
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());
        Element system =  (Element)root.getElementsByTagName("system").item(0);
        System.out.println(system.getNodeName());
        NodeList items  = system.getChildNodes();
        for (int i=0 ; i < items.getLength() ;i++){
            Node node =  items.item(i);
            if(node instanceof  Element){
                Element item =(Element) node;
                System.out.println(item.getAttribute("name"));
                System.out.println(item.getAttribute("value"));
            }

        }


        //assertEquals("Hello,World!",new TestController().index());

    }

}
