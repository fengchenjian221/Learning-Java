package boot.controller;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/streaming")
public class StreamingController {
    @Resource
    private transient JavaSparkContext javaSparkContext;

    SparkConf conf = new SparkConf()
            .setAppName("MyApp")
            .setMaster("local");

    JavaSparkContext sc = new JavaSparkContext(conf);

    @PostMapping("/customReceiver")
    public String customReceiver() throws InterruptedException{
        // 第二个参数：批间隔时间（以此间隔时间分割数据流）
        JavaStreamingContext jsc = new JavaStreamingContext(sc, Durations.seconds(5));
//        JavaReceiverInputDStream<String> lines = jsc.receiverStream(new CustomReceiver(StorageLevel.MEMORY_AND_DISK_2()));
//        JavaDStream<Long> count =  lines.count();
//        count = count.map(x -> {
//            System.out.println("这批一共这些条数据：" + x);
//            return x;
//        });
//        System.out.println("count.print()");
//        count.print();
//        System.out.println("jsc.start()");
//        // 现在才是真正开始计算
//        jsc.start();
//        System.out.println("jsc.awaitTermination()");
//        // 等待计算结束
//        jsc.awaitTermination();
//        System.out.println("jsc.stop()");
//        jsc.stop();
        return "success";
    }
}

