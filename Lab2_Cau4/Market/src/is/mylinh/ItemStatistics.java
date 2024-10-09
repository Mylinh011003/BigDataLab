package is.mylinh;
import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ItemStatistics implements Writable {
    private double averagePrice;
    private double maxPrice;
    private double minPrice;

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(averagePrice);
        out.writeDouble(maxPrice);
        out.writeDouble(minPrice);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        averagePrice = in.readDouble();
        maxPrice = in.readDouble();
        minPrice = in.readDouble();
    }
}
