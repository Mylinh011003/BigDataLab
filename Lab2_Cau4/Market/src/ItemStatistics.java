import org.w3c.dom.Text;

import is.mylinh.ItemInfo;
import is.mylinh.utils.ItemStatisticsMapper;
import is.mylinh.utils.ItemStatisticsReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class ItemStatistics {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "item statistics");
        job.setJarByClass(ItemStatistics.class);
        job.setMapperClass(ItemStatisticsMapper.class);
        job.setReducerClass(ItemStatisticsReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(ItemInfo.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
