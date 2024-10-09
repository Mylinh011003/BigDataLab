import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

public class utils {
 private utils(){}
 public class ConditionalProbabilityMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text pair = new Text();
    
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] items = value.toString().trim().split(" ");
        int length = items.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    pair.set(items[i] + "," + items[j]); // (A,B)
                    context.write(pair, one);
                }
            }
        }

        for (String item : items) {
            pair.set(item + ","); // A,
            context.write(pair, one);
        }
    }
 }

 public class ConditionalProbabilityReducer extends Reducer<Text, IntWritable, Text, Double> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int countAB = 0; // Đếm số lần xuất hiện của cặp (A,B)
        int countA = 0; // Đếm số lần xuất hiện của A

        String[] parts = key.toString().split(",");

        if (parts.length == 2) {
            // Nếu là cặp (A,B)
            countAB = sumValues(values);
            context.getCounter("Counts", "AB").increment(countAB);
        } else {
            // Nếu là A,
            countA = sumValues(values);
            context.getCounter("Counts", "A").increment(countA);
        }

        // Tính xác suất có điều kiện prob(B|A) = count(A,B) / count(A)
        if (countA > 0 && countAB > 0) {
            double probability = (double) countAB / countA;
            context.write(new Text(parts[0]), probability);
        }
    }

    private int sumValues(Iterable<IntWritable> values) {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        return sum;
    }
 }
}

