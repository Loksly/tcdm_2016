package cursohadoop.simplereducesidejoin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TaggedText implements WritableComparable<TaggedText> {

	public TaggedText() {
		set(new Text(), new Text());
	}
	
	public TaggedText(String first, String second) {
		set(new Text(first), new Text(second));
	}
	
	public TaggedText(Text first, Text second) {
		set(first, second);
	}
	
	public void set(Text tag, Text value) {
		this.tag = tag;
		this.value = value;
	}
	
	public Text getTag() {
		return tag;
	}
	
	public Text getValue() {
		return value;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		tag.readFields(in);
		value.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		tag.write(out);
		value.write(out);
	}
	
	@Override
	public int hashCode() {
		return tag.hashCode() * 163 + value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TaggedText) {
			TaggedText tt = (TaggedText) o;
			return tag.equals(tt.tag) && value.equals(tt.value);
		}
		return false;
	}
	
	@Override
	public int compareTo(TaggedText tt) {
		int cmp = tag.compareTo(tt.tag);
		if (cmp != 0) {
			return cmp;
		}
		return value.compareTo(tt.value);
	}
	
	@Override
	public String toString() {
		return tag+" ,"+value;
	}
	
	private Text tag;
	private Text value;

}
