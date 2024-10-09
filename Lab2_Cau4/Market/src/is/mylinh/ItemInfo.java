package is.mylinh;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
public class ItemInfo implements Writable {
    private double price;

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        price = in.readDouble();
    }
}
