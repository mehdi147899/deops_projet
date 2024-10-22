package tn.esprit.spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private ICourseServices courseServices;

    @Test
    void testAddCourse() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.price", is(100.0)));

        verify(courseServices, times(1)).addCourse(any(Course.class)); // Verify service method was called
    }

    @Test
    void testGetAllCourses() throws Exception {
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

        when(courseServices.retrieveAllCourses()).thenReturn(List.of(course1, course2));

        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numCourse", is(1)))
                .andExpect(jsonPath("$[1].numCourse", is(2)));

        verify(courseServices, times(1)).retrieveAllCourses(); // Verify service method was called
    }

    @Test
    void testUpdateCourse() throws Exception {
        Course updatedCourse = new Course();
        updatedCourse.setNumCourse(1L);
        updatedCourse.setLevel(1);
        updatedCourse.setTypeCourse(TypeCourse.INDIVIDUAL);
        updatedCourse.setSupport(Support.SNOWBOARD);
        updatedCourse.setPrice(150.0f);
        updatedCourse.setTimeSlot(3);

        when(courseServices.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"INDIVIDUAL\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.price", is(150.0)))
                .andExpect(jsonPath("$.typeCourse", is("INDIVIDUAL")))
                .andExpect(jsonPath("$.support", is("SNOWBOARD")));

        verify(courseServices, times(1)).updateCourse(any(Course.class)); // Verify service method was called
    }

    @Test
    void testGetById() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.retrieveCourse(1L)).thenReturn(course);

        mockMvc.perform(get("/course/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.typeCourse", is("COLLECTIVE_ADULT")))
                .andExpect(jsonPath("$.support", is("SKI")))
                .andExpect(jsonPath("$.price", is(100.0)))
                .andExpect(jsonPath("$.timeSlot", is(2)));

        verify(courseServices, times(1)).retrieveCourse(1L); // Verify service method was called
    }

    @Test
    void testGetAllCoursesEmptyList() throws Exception {
        when(courseServices.retrieveAllCourses()).thenReturn(List.of());

        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Check if the size of the list is 0

        verify(courseServices, times(1)).retrieveAllCourses(); // Verify service method was called
    }

    @Test
    void testUpdateCourseWithInvalidData() throws Exception {
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":-1,\"typeCourse\":\"INVALID_TYPE\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}"))
                .andExpect(status().isBadRequest()); // Expect a 400 Bad Request
    }

    // New test to handle exception
    @Test
    void testGetCourseNotFound() throws Exception {
        when(courseServices.retrieveCourse(99L)).thenThrow(new RuntimeException("Course not found"));

        mockMvc.perform(get("/course/get/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Expect a 404 Not Found
                .andExpect(jsonPath("$.error", is("Course not found"))); // Adjust this based on your ErrorResponse structure

        verify(courseServices, times(1)).retrieveCourse(99L); // Verify service method was called
    }

}
