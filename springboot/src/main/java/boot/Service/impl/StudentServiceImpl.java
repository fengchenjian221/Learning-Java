package boot.Service.impl;

import boot.Service.StudentService;
import boot.entity.Student;
import boot.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public List<Student> queryStudentList() {
        return studentMapper.queryStudentList();
    }

    @Override
    public int addStudent(Student student) {
        return studentMapper.addStudent(student);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public void deleteStudent(int stuId) {
        studentMapper.deleteStudent(stuId);
    }
}
