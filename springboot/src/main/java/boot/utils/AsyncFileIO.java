package boot.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncFileIO {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        File file = new File("file.txt");

        // 异步写入文件
        Future<Void> writeResult = executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Hello World");
                }
                return null;
            }
        });

        // 异步读取文件
        Future<String> readResult = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                StringBuilder sb = new StringBuilder();
                try (FileReader reader = new FileReader(file)) {
                    int c;
                    while ((c = reader.read()) != -1) {
                        sb.append((char) c);
                    }
                }
                return sb.toString();
            }
        });

        try {
            // 等待写入完成
            writeResult.get();

            // 读取文件内容
            System.out.println(readResult.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}

