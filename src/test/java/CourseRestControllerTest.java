import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseRestControllerTest {

    @InjectMocks
    private CourseRestController courseRestController;

    @Mock
    private ICourseServices courseServices;

    private MockMvc mockMvc;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();
        course = new Course(1L, 3, TypeCourse.COLLECTIVE_CHILDREN, null, 100.0f, 2, null);
    }

    @Test
    void testAddCourse() throws Exception {
        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(course.getNumCourse()));

        verify(courseServices, times(1)).addCourse(any(Course.class));
    }

    @Test
    void testAddCourse_InvalidData() throws Exception {
        Course invalidCourse = new Course(null, -1, null, null, -50.0f, -1, null); // Invalid data

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCourse)))
                .andExpect(status().isBadRequest()); // Assuming the controller validates and responds with 400

        verify(courseServices, times(0)).addCourse(any(Course.class)); // Ensure service is not called
    }

    @Test
    void testGetAllCourses() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numCourse").value(course.getNumCourse()));

        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    void testUpdateCourse() throws Exception {
        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(course.getNumCourse()));

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }

    @Test
    void testUpdateCourse_NotFound() throws Exception {
        Course updatedCourse = new Course(999L, 3, TypeCourse.COLLECTIVE_CHILDREN, null, 100.0f, 2, null); // Non-existing course

        when(courseServices.updateCourse(any(Course.class))).thenReturn(null); // Simulate not found

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCourse)))
                .andExpect(status().isNotFound()); // Assuming 404 for not found

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }

    @Test
    void testGetCourseById() throws Exception {
        when(courseServices.retrieveCourse(1L)).thenReturn(course);

        mockMvc.perform(get("/course/get/{id-course}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(course.getNumCourse()));

        verify(courseServices, times(1)).retrieveCourse(1L);
    }

    @Test
    void testGetCourseById_NotFound() throws Exception {
        when(courseServices.retrieveCourse(999L)).thenReturn(null); // Simulate not found

        mockMvc.perform(get("/course/get/{id-course}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Assuming 404 for not found

        verify(courseServices, times(1)).retrieveCourse(999L);
    }
}
