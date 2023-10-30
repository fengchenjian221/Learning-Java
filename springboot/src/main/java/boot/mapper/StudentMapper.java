package boot.mapper;

import boot.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 查询学生列表
     * @param
     * @return
     */
    public List<Student> queryStudentList();

    /**
     *
     * 查询学生列表
     */

    public List<Student> selectStudentList(long id);

    /**
     * 查询学生信息
     * @param id
     * @return
     */
    public Student queryUserById(long id);

    /**
     * 增加学生信息
     * @param student
     * @return
     */
    public int addStudent(Student student);

    /**
     * 更新学生信息
     * @param student
     * @return
     */
    public Student updateStudent(Student student);

    /**
     * 删除学生信息
     * @param stuId
     * @return
     */
    public void deleteStudent(int stuId);
}
