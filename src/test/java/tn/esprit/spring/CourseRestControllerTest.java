package tn.esprit.spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Import MockBean
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@WebMvcTest(CourseRestController.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use MockBean to create a mock instance of ICourseServices
    private ICourseServices courseServices;
    @Test
    void testUpdateCourseNotFound() throws Exception {
        // Mock the service to throw an exception for a non-existent course
        when(courseServices.updateCourse(any(Course.class))).thenThrow(new RuntimeException("Course not found"));

        // Perform the PUT request
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":999,\"level\":1,\"typeCourse\":\"INDIVIDUAL\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}"))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        // Mock the service to throw an exception for a non-existent course
        when(courseServices.retrieveCourse(999L)).thenThrow(new RuntimeException("Course not found"));

        // Perform the GET request
        mockMvc.perform(get("/course/get/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Course not found"))); // Verify error message
    }

    @Test
    void testAddCourse() throws Exception {
        // Create a sample course object
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        // Define behavior for the mocked service
        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}"))
                .andExpect(status().isCreated()) // Expecting a 201 Created status
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.price", is(100.0)));
    }

    @Test
    void testGetAllCourses() throws Exception {
        // Create sample Course objects
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);
        course1.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course1.setSupport(Support.SKI);
        course1.setPrice(100.0f);
        course1.setTimeSlot(2);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setTypeCourse(TypeCourse.INDIVIDUAL);
        course2.setSupport(Support.SNOWBOARD);
        course2.setPrice(150.0f);
        course2.setTimeSlot(3);

        // Mock the service method to return the list of courses
        when(courseServices.retrieveAllCourses()).thenReturn(List.of(course1, course2));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Check if the size of the list is 2
                .andExpect(jsonPath("$[0].numCourse", is(1))) // Check the first course's numCourse
                .andExpect(jsonPath("$[1].numCourse", is(2))); // Check the second course's numCourse
    }
    @Test
    void testUpdateCourse() throws Exception {
        // Define the expected updated course
        Course updatedCourse = new Course();
        updatedCourse.setNumCourse(1L);
        updatedCourse.setLevel(1);
        updatedCourse.setTypeCourse(TypeCourse.INDIVIDUAL);
        updatedCourse.setSupport(Support.SNOWBOARD);
        updatedCourse.setPrice(150.0f);
        updatedCourse.setTimeSlot(3);

        // Mock the service method to return the updated course
        when(courseServices.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        // Perform the PUT request and verify the response
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"INDIVIDUAL\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.price", is(150.0)))
                .andExpect(jsonPath("$.typeCourse", is("INDIVIDUAL")))
                .andExpect(jsonPath("$.support", is("SNOWBOARD")));
    }
    @Test
    void testGetById() throws Exception {
        // Define a sample Course object to return
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        // Mock the service method to return the course for the given ID
        when(courseServices.retrieveCourse(1L)).thenReturn(course);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/course/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.typeCourse", is("COLLECTIVE_ADULT")))
                .andExpect(jsonPath("$.support", is("SKI")))
                .andExpect(jsonPath("$.price", is(100.0)))
                .andExpect(jsonPath("$.timeSlot", is(2)));
    }
    @Test
    void testGetAllCoursesEmptyList() throws Exception {
        // Mock the service method to return an empty list
        when(courseServices.retrieveAllCourses()).thenReturn(List.of());

        // Perform the GET request and verify the response
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Check if the size of the list is 0
    }
    @Test
    void testUpdateCourseWithInvalidData() throws Exception {
        // Perform the PUT request with invalid data
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":-1,\"typeCourse\":\"INVALID_TYPE\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}")) // Invalid level
                .andExpect(status().isBadRequest()); // Expect a 400 Bad Request
    }

}
