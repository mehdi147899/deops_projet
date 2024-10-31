package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private static final String ERROR_KEY = "error";
    private static final String COURSE_NUMBER_CANNOT_BE_NULL = "Course number cannot be null";
    private static final String COURSE_NOT_FOUND = "Course not found";
    private static final String COURSE_HAS_DEPENDENCIES = "Course cannot be deleted as it has dependencies";

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public ResponseEntity<Object> addCourse(@RequestBody @Valid Course course) {
        if (course.getNumCourse() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, COURSE_NUMBER_CANNOT_BE_NULL);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Course addedCourse = courseServices.addCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCourse);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseServices.retrieveAllCourses();
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        try {
            Course updatedCourse = courseServices.updateCourse(course);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long numCourse) {
        try {
            Course course = courseServices.retrieveCourse(numCourse);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, COURSE_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(description = "Delete Course by Id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable("id") Long numCourse) {
        try {
            courseServices.deleteCourse(numCourse);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (COURSE_HAS_DEPENDENCIES.equals(errorMessage)) {
                status = HttpStatus.CONFLICT;
            }
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, errorMessage);
            return ResponseEntity.status(status).body(errorResponse);
        }
    }
}
