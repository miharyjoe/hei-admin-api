package school.hei.haapi.endpoint.rest.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.UserMapper;
import school.hei.haapi.endpoint.rest.model.Course;
import school.hei.haapi.endpoint.rest.model.Student;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.model.User;
import school.hei.haapi.service.UserService;
import school.hei.haapi.endpoint.rest.model.CourseStatus;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@AllArgsConstructor
@Slf4j
public class StudentController {
  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping("/students/{id}")
  public Student getStudentById(@PathVariable String id) {
    return userMapper.toRestStudent(userService.getById(id));
  }

  @GetMapping("/students")
  public List<Student> getStudents(
      @RequestParam PageFromOne page, @RequestParam("page_size") BoundedPageSize pageSize,
      @RequestParam(value = "ref", required = false, defaultValue = "") String ref,
      @RequestParam(value = "first_name", required = false, defaultValue = "") String firstName,
      @RequestParam(value = "last_name", required = false, defaultValue = "") String lastName) {
    return userService.getByCriteria(User.Role.STUDENT, firstName, lastName, ref, page, pageSize
        ).stream()
        .map(userMapper::toRestStudent)
        .collect(toUnmodifiableList());
  }

  @PutMapping("/students")
  public List<Student> saveAll(@RequestBody List<Student> toWrite) {
    return userService
        .saveAll(toWrite
            .stream()
            .map(userMapper::toDomain)
            .collect(toUnmodifiableList()))
        .stream()
        .map(userMapper::toRestStudent)
        .collect(toUnmodifiableList());
  }

  @GetMapping("/students/{student_id}/courses")
  public List<Course> getCourse(
          @RequestParam(value = "status", required = false, defaultValue = "LINKED") CourseStatus status,
          @PathVariable String student_id) {
    return userService.getByIdAndStatus(student_id, status)
            .stream()
            .map(userMapper::toRestStudentCourse)
            .collect(toUnmodifiableList());
  }
}
