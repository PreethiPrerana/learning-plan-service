package com.thbs.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.thbs.lms.dto.CourseDTO;
import com.thbs.lms.model.Course;
import com.thbs.lms.service.BulkUploadService;
import com.thbs.lms.service.CourseService;

/**
 * The {@code CourseController} class handles HTTP requests related to courses.
 * It provides endpoints for adding, retrieving, updating, and deleting courses.
 */
@RestController
@RequestMapping("/course")
// @CrossOrigin("172.18.4.186:5173, 172.18.4.113:5173, 172.18.4.30:5173,
// 172.18.4.195:5173")
public class CourseController {

    /**
     * The service responsible for handling business logic related to courses.
     */
    private final CourseService courseService;

    /**
     * The service responsible for handling bulk upload functionality.
     */
    private final BulkUploadService bulkUploadService;

    /**
     * Constructs a new {@code CourseController} with the specified
     * {@code CourseService}.
     *
     * @param courseService     the course service
     * @param bulkUploadService the bulk upload service
     */
    @Autowired
    public CourseController(CourseService courseService, BulkUploadService bulkUploadService) {
        this.courseService = courseService;
        this.bulkUploadService = bulkUploadService;
    }

    /**
     * Adds a single course.
     *
     * @param course the course to add
     * @return a response entity containing the added course
     */
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course addedCourse = courseService.saveCourse(course);
        return ResponseEntity.ok().body(addedCourse);
    }

    /**
     * Handles bulk upload functionality.
     *
     * @param file the file to upload
     * @return a response entity indicating the success of the upload operation
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        bulkUploadService.uploadFile(file);
        return ResponseEntity.ok().body("File uploaded successfully.");
    }

    /**
     * Retrieves all courses.
     *
     * @return a response entity containing a list of all courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok().body(courses);
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId the ID of the course to retrieve
     * @return a response entity containing the course with the specified ID
     */
    @GetMapping("/id/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return ResponseEntity.ok().body(course);
    }

    /**
     * Retrieves courses at a particular level (Beginner, Intermediate, Difficult).
     *
     * @param level the level of courses to retrieve
     * @return a response entity containing a list of courses at the specified level
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Course>> getCoursesByLevel(@PathVariable String level) {
        List<Course> courses = courseService.getCoursesByLevel(level);
        return ResponseEntity.ok().body(courses);
    }

    /**
     * Retrieves all course DTOs.
     *
     * @return a response entity containing a list of all course DTOs
     */
    @GetMapping("/dto")
    public ResponseEntity<List<CourseDTO>> getAllCourseDTOs() {
        List<CourseDTO> courseDTOs = courseService.getAllCourseDTOs();
        return ResponseEntity.ok().body(courseDTOs);
    }

    /**
     * Updates the name of a course.
     *
     * @param courseId      the ID of the course to update
     * @param newCourseName the new name of the course
     * @return a response entity containing the updated course
     */
    @PatchMapping("/name/{courseId}")
    public ResponseEntity<Course> updateCourseName(@PathVariable Long courseId, @RequestBody String newCourseName) {
        Course updatedCourse = courseService.updateCourseName(courseId, newCourseName);
        return ResponseEntity.ok().body(updatedCourse);
    }

    /**
     * Deletes a course by its ID.
     *
     * @param courseId the ID of the course to delete
     * @return a response entity indicating the success of the deletion operation
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourseById(courseId);
        return ResponseEntity.ok().body("Course deleted successfully");
    }

    /**
     * Deletes multiple courses.
     *
     * @param courses the list of courses to delete
     * @return a response entity indicating the success of the deletion operation
     */
    @DeleteMapping("/multiple")
    public ResponseEntity<String> deleteCourses(@RequestBody List<Course> courses) {
        courseService.deleteMultipleCourses(courses);
        return ResponseEntity.ok().body("Courses deleted successfully");
    }
}
