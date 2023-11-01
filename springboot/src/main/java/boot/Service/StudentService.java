package boot.Service;

import boot.entity.Student;

import java.util.List;


public interface StudentService {

    List<Student> queryStudentList();

    int addStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(int deleteStudent);
}
