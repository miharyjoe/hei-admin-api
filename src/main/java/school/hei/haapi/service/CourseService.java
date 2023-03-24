package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.*;
import school.hei.haapi.endpoint.rest.model.CourseStatus;
import school.hei.haapi.repository.CourseRepository;
import school.hei.haapi.repository.CourseStudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository repository;
    private final CourseStudentRepository courseStudentRepository;
    private final UserService userService;
    public List<Course> getAll(PageFromOne page , BoundedPageSize pageSize){
        Pageable pageable =
                PageRequest.of(page.getValue(), pageSize.getValue());
        return repository.findAll(pageable).toList();
    }

  public List<Course> saveAll(List<Course> courses) {
    return repository.saveAll(courses);
  }
    public List<Course> findCoursesByStudent(String studentId) {
        User student = userService.getById(studentId);
        return courseStudentRepository.findAllByStudent(student).stream()
                .map(CourseStudent::getCourse)
                .collect(Collectors.toList());
    }
    public void updateCourseStudentStatus(String studentId, String courseId, CourseStatus newStatus) {
        User student = userService.getById(studentId);
        Course course = repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course id: " + courseId));
        CourseStudent courseStudent = courseStudentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new IllegalArgumentException("No course student found for student " + studentId + " and course " + courseId));
        courseStudent.setStatus(newStatus);
        courseStudentRepository.save(courseStudent);
    }

    public List<Course> getAllCourses(String code, String lastName, String creditsOrder, String codeOrder, String firstName) {
        Sort sort;
        if ("ASC".equalsIgnoreCase(creditsOrder)) {
            sort = Sort.by(Sort.Direction.ASC, "credits", "id");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "credits", "id");
        }
        if ("ASC".equalsIgnoreCase(codeOrder)) {
            sort = sort.and(Sort.by(Sort.Direction.ASC, "code", "id"));
        } else {
            sort = sort.and(Sort.by(Sort.Direction.DESC, "code", "id"));
        }
        if (code != null && lastName != null && firstName == null) {
            return repository.findAllByCodeContainingIgnoreCaseOrMainTeacherLastNameContainingIgnoreCase(code, lastName, sort);
        } else if (code != null && lastName == null && firstName == null) {
            return repository.findAllByCodeContainingIgnoreCase(code, sort);
        } else if (code == null && lastName == null && firstName != null) {
            return repository.findAllByMainTeacherFirstNameContainingIgnoreCase(firstName, sort);
        } else if (code == null && lastName != null && firstName == null) {
            return repository.findAllByMainTeacherLastNameContainingIgnoreCase(lastName, sort);
        } else {
            if (code == null) {
                code = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            return repository.findAllByCodeContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase(code, lastName, sort);
        }
    }
}
