package boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {

    /** 主键 */
    private long id;

    /** 学生id */
    private int stuId;

    /** 姓名 */
    private String name;

    /** 年级 */
    private String grade;

    /** 班级 */
    private String class1;

    /** 分数 */
    private int score;

}
