import java.io.IOException;

import javax.naming.Context;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.w3c.dom.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper;
import is.mylinh.ItemInfo;
import is.mylinh.ItemStatistics;
public class utils {
    private utils() {}
    public static class ItemStatisticsMapper extends Mapper<Object, Text, Text, ItemInfo> {
    private Text itemKey = new Text();
    private ItemInfo itemInfo = new ItemInfo();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");
        if (fields.length < 4) return; 
        
        String date = fields[0]; 
        String itemName = fields[1]; 
        double price = Double.parseDouble(fields[2]); 
        
        itemKey.set(date + "," + itemName);
        itemInfo.setPrice(price);
        context.write(itemKey, itemInfo);
        
        itemKey.set(itemName);
        itemInfo.setPrice(price);
        context.write(itemKey, itemInfo);
    }
    }
    public static class ItemStatisticsReducer extends Reducer<Text, ItemInfo, Text, ItemStatistics> {
        private ItemStatistics itemStats = new ItemStatistics();
    
        @Override
        protected void reduce(Text key, Iterable<ItemInfo> values, Context context) throws IOException, InterruptedException {
            double sum = 0;
            double max = Double.MIN_VALUE;
            double min = Double.MAX_VALUE;
            int count = 0;
    
            for (ItemInfo info : values) {
                double price = info.getPrice();
                sum += price;
                count++;
                if (price > max) max = price;
                if (price < min) min = price;
            }
    
            double average = sum / count;
    
            itemStats.setAveragePrice(average);
            itemStats.setMaxPrice(max);
            itemStats.setMinPrice(min);
            
            context.write(key, itemStats);
        }
    }

}
