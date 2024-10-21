package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        // Optionally, validate course data here
        Course createdCourse = courseServices.addCourse(course);
        return ResponseEntity.status(201).body(createdCourse); // Return 201 Created
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseServices.retrieveAllCourses();
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        if (!courseServices.exists(course.getNumCourse())) { // Check if the course exists
            return ResponseEntity.status(404).build(); // Return 404 Not Found
        }
        Course updatedCourse = courseServices.updateCourse(course);
        return ResponseEntity.ok(updatedCourse); // Return 200 OK
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public ResponseEntity<Course> getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        if (course == null) {
            return ResponseEntity.status(404).build(); // Return 404 Not Found
        }
        return ResponseEntity.ok(course); // Return 200 OK
    }
}
