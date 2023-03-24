package school.hei.haapi.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.haapi.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findAllByCodeContainingIgnoreCaseOrMainTeacherLastNameContainingIgnoreCase(String code, String lastname, Sort sort);
    List<Course> findAllByCodeContainingIgnoreCase(String code,Sort sort);
    List<Course> findAllByMainTeacherFirstNameContainingIgnoreCase(String firstname,Sort sort);
    List<Course> findAllByMainTeacherLastNameContainingIgnoreCase(String lastname,Sort sort);
    List<Course> findAllByCodeContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase(String code, String lastname,Sort sort);
}
