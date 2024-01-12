package com.skynet.robin.wabacus;

import com.wabacus.WabacusServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Configuration
public class WabacusAutoConfiguration implements WebMvcConfigurer {

    private static final String DEF_CONFIG_FILE_NAME = "wabacus.cfg.xml";
    private static final String DEF_CONFIG_PATH = "classpath:reportconfig";
    private static final String DEF_URL = "/report.wx";

    @Value("${wabacus.configpath:"+DEF_CONFIG_PATH+"}")
    private  String CONFIG_PATH ;



    private String getWabacusServletUrl(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false); // 忽略命名空间
            factory.setValidating(false); // 不验证DTD
            //factory.setExpandEntityReferences(false); // 不展开实体引用
            DocumentBuilder builder  = factory.newDocumentBuilder();
            // 解析XML文件并获取Document对象
            File xmlFile = null;
            if("classpath".equalsIgnoreCase(CONFIG_PATH)){
                xmlFile =  ResourceUtils.getFile("classpath:" + DEF_CONFIG_FILE_NAME);
            }else{
                xmlFile =  ResourceUtils.getFile(CONFIG_PATH + "/" + DEF_CONFIG_FILE_NAME);
            }
            Document document = builder.parse(xmlFile);
            Element root = document.getDocumentElement();
            Element system =  (Element)root.getElementsByTagName("system").item(0);
            NodeList items  = system.getChildNodes();
            for (int i=0 ; i < items.getLength() ;i++){
                Node node =  items.item(i);
                if(node instanceof  Element){
                    Element item =(Element) node;
                    if("showreport-url".equalsIgnoreCase(item.getAttribute("name"))){
                        return item.getAttribute("value");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return DEF_URL;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        //将其他资源打包在starter里面，用户无需关系其他资源的引入情况，只需要指定wabacus.cfg.xml路径即可
        String url = getWabacusServletUrl();
        return new ServletRegistrationBean(new WabacusServlet(),url);
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                //<param-name>configpath</param-name>
                //<param-value>classpath{/reportconfig}</param-value>
                //将classpath格式处理成wabacus认识的格式
                String configpath = CONFIG_PATH.replace(":","{/") + "}";
                if("classpath".equalsIgnoreCase(CONFIG_PATH)){
                    configpath = "classpath{/}";
                } else if(CONFIG_PATH.startsWith("classpath:")){
                    configpath = CONFIG_PATH.replace(":","{/") + "}";
                }else{
                    configpath = CONFIG_PATH;
                }
                servletContext.setInitParameter("configpath", configpath);
            }
        };
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean (){
        FilterRegistrationBean bean = new FilterRegistrationBean(new SetCharacterEncodingFilter());
        return bean;
    }

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean(new WabacusServlet());
        return bean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * wabacus自带的资源放在starter包中的/reportconfig下，通过web接口暴露webresources静态资源,使用的时候无需关心
         */
        registry.addResourceHandler("/webresources/**")
                .addResourceLocations("classpath:/reportconfig/webresources/");
    }

}
