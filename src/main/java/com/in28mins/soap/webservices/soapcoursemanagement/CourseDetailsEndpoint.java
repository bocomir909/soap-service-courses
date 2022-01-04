package com.in28mins.soap.webservices.soapcoursemanagement;

import com.in28mins.courses.*;
import com.in28mins.soap.webservices.soapcoursemanagement.beans.Course;
import com.in28mins.soap.webservices.soapcoursemanagement.exceptions.CourseNotFoundException;
import com.in28mins.soap.webservices.soapcoursemanagement.services.CourseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Bojan on 12/30/2021.
 */
@Endpoint
public class CourseDetailsEndpoint {

    private static final String NAMESPACE_URI = "http://www.test.com/courses";

    @Autowired
    private CourseDetailsService courseDetailsService;

    @PayloadRoot(namespace = NAMESPACE_URI,localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public GetCourseDetailsResponse processRequest(@RequestPayload GetCourseDetailsRequest request){
        Course course = courseDetailsService.findById(request.getId().intValue());
        if(course == null){
            throw new CourseNotFoundException("Course not found");
        }
        return mapCourse(course);
    }

    @PayloadRoot(namespace = NAMESPACE_URI,localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processAllRequest(@RequestPayload GetAllCourseDetailsRequest request){
        List<Course> courses = courseDetailsService.findAll();
        return mapAllCourse(courses);
    }

    @PayloadRoot(namespace = NAMESPACE_URI,localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse processAllRequest(@RequestPayload DeleteCourseDetailsRequest request){
        CourseDetailsService.Status status = courseDetailsService.deleteById(request.getId().intValue());
        DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
        response.setStatus(mapStatus(status));
        return response;
    }

    private Status mapStatus(CourseDetailsService.Status status){
        if(status == CourseDetailsService.Status.SUCCESS){
            return Status.SUCCESS;
        }
        return Status.FAILURE;
    }

    private GetCourseDetailsResponse mapCourse(Course course) {
        GetCourseDetailsResponse responseType = new GetCourseDetailsResponse();
        CourseDetails detailsType = getCourseDetails(course);
        responseType.setCourseDetails(detailsType);
        return responseType;
    }

    private GetAllCourseDetailsResponse mapAllCourse(List<Course> course) {
        GetAllCourseDetailsResponse responseType = new GetAllCourseDetailsResponse();
        course.forEach(c->{
            CourseDetails courseDetails = getCourseDetails(c);
            responseType.getCourseDetails().add(courseDetails);
        });

        return responseType;
    }

    private CourseDetails getCourseDetails(Course course) {
        CourseDetails detailsType = new CourseDetails();
        detailsType.setId(BigInteger.valueOf(course.getId()));
        detailsType.setName(course.getName());
        detailsType.setDescription(course.getDescription());
        return detailsType;
    }


}
