package com.in28mins.soap.webservices.soapcoursemanagement;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Collections;
import java.util.List;

/**
 * Created by Bojan on 12/30/2021.
 */

//Enable web service
@EnableWs
//spring configuration
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "courses")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CoursePort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.test.com/courses");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    //courses.wsdl via URL: /ws/courses.wsdl
    @Bean
    public XsdSchema coursesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("courses.xsd"));
    }

    //WS Security Config
    //XwsSecurityInterceptor
    //callback handler
    //Interceptors.add -> XwsSecurityInterceptor
    @Bean
    public XwsSecurityInterceptor securityInterceptor(){
        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
        securityInterceptor.setCallbackHandler(callbackHandler());
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
        return securityInterceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor());
    }

    @Bean
    public SimplePasswordValidationCallbackHandler callbackHandler(){
        SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
        callbackHandler.setUsersMap(Collections.singletonMap("user","password"));
        return callbackHandler;
    }
}
