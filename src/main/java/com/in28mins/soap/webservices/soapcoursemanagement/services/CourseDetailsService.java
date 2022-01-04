package com.in28mins.soap.webservices.soapcoursemanagement.services;

import com.in28mins.soap.webservices.soapcoursemanagement.beans.Course;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Bojan on 12/31/2021.
 */
@Component
public class CourseDetailsService {

    public enum Status{
        SUCCESS,
        FAILURE
    }
    //in memory DB
    private static List<Course> courses = new ArrayList<>();
    //executed only once when the class is loaded
    static {
        Course course1 = new Course(123,"course1,","desc1");
        Course course2 = new Course(456,"course2","desc2");
        Course course3 = new Course(789,"course3,","desc3");

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
    }

    public Course findById(int id){
        Course course = courses.stream()
                .filter(c -> c.getId()==id)
                .findFirst().orElse(null);
        return course;
    }

    public List<Course> findAll(){
        return courses;
    }

    public Status deleteById(int id){
        Optional<Course> courseOptional = courses.stream()
                .filter(c -> c.getId()==id)
                .findFirst();

        if(courseOptional.isPresent()){
            courses.remove(courseOptional.get());
            return Status.SUCCESS;
        }
        return Status.FAILURE;
    }
}
