package com.sonu.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.Tuple2;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootMainApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("word counter");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> inputFile = sparkContext.textFile("src\\main\\resources\\wordCount.txt");
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("3")).iterator());
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2<>(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
        countData.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> p) throws Exception {
                System.out.println(p);
            }
        }); //<-- skip shuffle and use cached mapped files
        System.out.println("Total words: " + countData.count());
        // countData.saveAsTextFile("outputpath");
        Thread.sleep(60000); // <-- to keep Spark UI up*/
    }
}
