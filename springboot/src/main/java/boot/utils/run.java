package boot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.hadoop.conf.Configuration;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class run {
    public static void main(String[] args) {

        List<Map> listMap = new ArrayList<Map>();

        Map map1 = new HashMap();
        map1.put("小明","员工");
        map1.put("小军","主管");
        String jsonString1= JSON.toJSONString(map1);
        System.out.println(jsonString1);

        Map map2 = new HashMap();
        map2.put("小王", "员工");
        map2.put("小红", "主管");


        listMap.add(map1);
        listMap.add(map2);
        String jsonString2= JSON.toJSONString(listMap);
        System.out.println(jsonString2);

//        输出：
//        jsonString1转化后：{"小明":"员工","小军":"主管"}
//        jsonString2转化后：[{"小明":"员工","小军":"主管"},{"小王":"员工","小红":"主管"}]

        Jedis jedis = new Jedis("127.0.0.1",6379);


        String vo = jedis.get("mynum");
        jedis.close();
        System.out.println(vo);
    }
}
