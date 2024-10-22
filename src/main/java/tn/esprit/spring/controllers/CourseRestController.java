package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        return courseServices.addCourse(course);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseServices.retrieveAllCourses();
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course) {
        return courseServices.updateCourse(course);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long numCourse) {
        try {
            Course course = courseServices.retrieveCourse(numCourse);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            // Return 404 Not Found with error message as a Map
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
