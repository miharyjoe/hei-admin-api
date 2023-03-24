package school.hei.haapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.haapi.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findAllByOrderByCodeAscIdAsc();
    List<Course> findAllByOrderByCodeDescIdDesc();
    List<Course> findAllByOrderByCreditsAscIdAsc();
    List<Course> findAllByOrderByCreditsDescIdDDesc();
    List<Course> findByMainTeacherFirstNameContainingIgnoreCase(String firstName);
    List<Course> findByMainTeacherLastNameContainingIgnoreCase(String lastName);

}