package com.in28mins.soap.webservices.soapcoursemanagement.exceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * Created by Bojan on 1/3/2022.
 */
@SoapFault(faultCode = FaultCode.CLIENT)
public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(String message) {
        super(message);
    }
}
