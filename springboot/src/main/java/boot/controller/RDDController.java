package boot.controller;

import org.apache.curator.shaded.com.google.common.base.Joiner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.Tuple2;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("rdd")
public class RDDController {

    SparkConf conf = new SparkConf()
            .setAppName("MyApp")
            .setMaster("local");

    JavaSparkContext sc = new JavaSparkContext(conf);

    private final String filePath = "src/main/java/com/example/file/TestFile.txt";

    @RequestMapping("wordCount1")
    public Object wordCount1() {
        // 获取本地文件 生成javaRDD
        JavaRDD<String> file = sc
                .textFile(filePath);

        // 按空格分解为数组 生成新的javaRDD
        JavaRDD<String> words = file.flatMap(
                line -> Arrays.asList(line.split(" ")).iterator());

        // 将结果转换为 list并返回
        return words;
    }

    @RequestMapping("wordCount2")
    public Object wordCount2() {
        JavaRDD<String> lines = sc.textFile(filePath).cache();

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(lines.count());

        JavaRDD<String> words = lines.flatMap(str -> Arrays.asList(str.split(" ")).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(str -> new Tuple2<String, Integer>(str, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((Integer i1, Integer i2) -> (i1 + i2));

        JavaPairRDD<Integer, String> temp = counts.mapToPair(tuple -> new Tuple2<Integer, String>(tuple._2, tuple._1));

        JavaPairRDD<String, Integer> sorted = temp.sortByKey(false)
                .mapToPair(tuple -> new Tuple2<String, Integer>(tuple._2, tuple._1));

        System.out.println(sorted.count());

        List<Tuple2<String, Integer>> output = sorted.collect();
        // List<Tuple2<String, Integer>> output = sorted.take(10);
        // List<Tuple2<String, Integer>> output = sorted.top(10);

        Map<String, Object> result = new HashMap<String, Object>();
        for (Tuple2<String, Integer> tuple : output) {
            result.put(tuple._1(), tuple._2());
        }

        return result;
    }

    @PostMapping("testAPI")
    public Object testAPI() {
        List<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        JavaRDD<Integer> rdd01 = sc.parallelize(data);
        rdd01 = rdd01.map(num -> {
            return num * num;
        });
        //data map:1,4,9,16,25,36
        System.out.println("data map: " + Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        rdd01 = rdd01.filter(x -> x < 6);
        //data filter: 1,4
        System.out.println("data filter: " + Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        rdd01 = rdd01.flatMap(x -> {
            Integer[] test = {x, x + 1, x + 2};
            return Arrays.asList(test).iterator();
        });
        //flatMap: 1,2,3,4,5,6
        System.out.println("flatMap : " + Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        JavaRDD<Integer> unionRdd = sc.parallelize(data);

        rdd01 = rdd01.union(unionRdd);
        //union: 1,2,3,4,5,6,1,2,3,4,5,6
        System.out.println("union : " + Joiner.on(",").skipNulls().join(rdd01.collect()).toString());

        List<Integer> result = new ArrayList<>();
        result.add(rdd01.reduce((Integer v1, Integer v2) -> {
            return v1 + v2;
        }));
        //reduce: 42
        System.out.println("reduce : " + Joiner.on(",").skipNulls().join(result).toString());
        result.forEach(System.out::print);

        JavaPairRDD<Integer, Iterable<Integer>> groupRdd = rdd01.groupBy(x -> {
            System.out.println("======grouby========： " + x);
            if (x > 10) return 0;
            else return 1;
        });

        List<Tuple2<Integer, Iterable<Integer>>> resul = groupRdd.collect();
        //group by==>  key: 1 value: 1,2,3,4,5,6,1,2,3,4,5,6
        resul.forEach(x -> {
            System.out.println("group by==> key: " + x._1 + " value: "
                    + Joiner.on(",").skipNulls().join(x._2).toString());
        });

        return null;
    }
}

