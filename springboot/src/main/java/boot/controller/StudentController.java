package boot.controller;

import boot.Service.StudentService;
import boot.entity.Student;
import boot.module.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/add")
    public ResultBody addStudent(@RequestBody Student student){
        studentService.addStudent(student);
        return ResultBody.ok();
    }

    @GetMapping("/get")
    public ResultBody selectStudent(){
        List<Student> stuId = studentService.queryStudentList();
        return ResultBody.success(stuId);
    }

    @PostMapping("/update")
    public ResultBody updateStudent(@RequestBody Student student){
        Student student1 = studentService.updateStudent(student);
        return ResultBody.success(student1);
    }

    @PostMapping("/delete")
    public ResultBody deleteStudent(@RequestBody int stuId){
        studentService.deleteStudent(stuId);
        return ResultBody.ok();
    }
}
