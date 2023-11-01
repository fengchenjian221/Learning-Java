package boot.controller;

import boot.entity.Student;
import boot.module.ResultBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/serializable")
public class SerializableController {

    /**
     * 序列化
     * @param student
     * @return
     */
    @PostMapping("/SendObject")
    public ResultBody SendObject(@RequestBody Student student){
        ObjectOutputStream oos = null;
        try {
            // 创建java对象
            // new Student
            // 序列化
            oos = new ObjectOutputStream(new FileOutputStream("students"));
            // 序列化对象
            oos.writeObject(student);
            // 刷新
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(oos != null){
                // 关闭
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Student student1 = ReadObject();
        return ResultBody.ok(student1);
    }


    public Student ReadObject() {
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("students"));
            // 开始反序列化，读
            obj = ois.readObject();
            // 反序列化回来是一个学生对象，所以会调用学生对象的toString方法。
            //System.out.println(obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (Student) obj;
        }
    }

}
