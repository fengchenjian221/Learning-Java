package boot.Service;

import boot.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    List<Student> queryStudentList();

    List<Student> selectStudentList(long id);

    int addStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(int deleteStudent);
}
