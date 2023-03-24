package school.hei.haapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.haapi.model.Course;


@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course> findAllByCodeContainingIgnoreCaseOrMainTeacherLastNameContainingIgnoreCase(String code, String lastname, Pageable pageable);
    Page<Course> findAllByCodeContainingIgnoreCase(String code,Pageable pageable);
    Page<Course> findAllByMainTeacherFirstNameContainingIgnoreCase(String firstname,Pageable pageable);
    Page<Course> findAllByMainTeacherLastNameContainingIgnoreCase(String lastname,Pageable pageable);
    Page<Course> findAllByCodeContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase(String code, String lastname, Pageable pageable);

}
