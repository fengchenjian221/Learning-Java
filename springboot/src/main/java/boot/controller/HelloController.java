package boot.controller;

import boot.MyAnnotation.MyLogAnnotation;
import boot.entity.Student;
import boot.module.ResultBody;
import boot.utils.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;

@RestController
public class HelloController {

    @Autowired
    RedisTemplateUtil redisUtil;

    @GetMapping("/hello")
    public ResultBody hello() {
        return ResultBody.ok("hello world");
    }

    /**
     * 返回手机号码
     * @return tel
     */
    @GetMapping("/phoneNumber")
    public ResultBody phoneNumber(){
        int [] arr = new int[]{8,2,1,0,3,5,7,9,6};
        int [] index = new int[]{2,5,4,3,0,7,6,7,3,1,8};
        String tel = "";
        for(int i = 0;i < index.length;i++){
            tel += arr[index[i]];
        }
        return ResultBody.ok(tel);
    }

    /**
     * 反转列表
     * @param array1
     * @return
     */
    @PostMapping("/ReverseArray")
    public ResultBody ReverseArray(@RequestBody ArrayList<Integer> array1){
        for(int i = 0;i < array1.size() /2; i++){
            int temp = array1.get(i);
            array1.set(i, array1.get(array1.size() - i - 1));
            array1.set(array1.size() - i - 1, temp);
        }
        return ResultBody.ok(array1);
    }

    /**
     * 存进redis
     * @param text
     * @return
     */
    @GetMapping("/SaveRedis")
    public ResultBody SaveRedis(@RequestParam String text){
        redisUtil.set("1",text);
        Object redis = redisUtil.get("1");
        return ResultBody.ok(redis);
    }


    @RequestMapping("helloAnnotation")
    //标有这个注解的方法会被增强
    @MyLogAnnotation(desc = "@Annotation")
    public Object helloAnnotation() {
        return "hello annotation";
    }

}