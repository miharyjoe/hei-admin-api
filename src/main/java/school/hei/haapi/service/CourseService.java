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
    public List<Course> getAll(PageFromOne page, BoundedPageSize pageSize, String code, OrderBy creditsOrder, OrderBy codeOrder, String firstName, String lastName) {
        Pageable pageable = PageRequest.of(page.getValue()-1, pageSize.getValue(), getSort(codeOrder, creditsOrder));

        if (code != null && lastName != null && firstName == null) {
            return repository.findAllByCodeContainingIgnoreCaseOrMainTeacherLastNameContainingIgnoreCase(code, lastName, pageable).getContent();
        } else if (code != null && lastName == null && firstName == null) {
            return repository.findAllByCodeContainingIgnoreCase(code, pageable).getContent();
        } else if (code == null && lastName == null && firstName != null) {
            return repository.findAllByMainTeacherFirstNameContainingIgnoreCase(firstName, pageable).getContent();
        } else if (code == null && lastName != null && firstName == null) {
            return repository.findAllByMainTeacherLastNameContainingIgnoreCase(lastName, pageable).getContent();
        } else {
            if (code == null) {
                code = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            return repository.findAllByCodeContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase(code, lastName, pageable).getContent();
        }
    }

    public enum OrderBy {
        ASC,
        DESC
    }
    private Sort getSort(OrderBy codeOrder, OrderBy creditsOrder) {
        Sort.Order codeSort = new Sort.Order(codeOrder == OrderBy.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, "code");
        Sort.Order creditsSort = new Sort.Order(creditsOrder == OrderBy.DESC ? Sort.Direction.DESC : Sort.Direction.ASC, "credits");
        return Sort.by(codeSort, creditsSort);
    }

}
