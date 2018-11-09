import java.util.*;

public class Text implements Comparable<Text> {
	private int tid;
	private int postTime;
	private String type;
	private User publisher;
	private String text;

	Text(int tid, int postTime, String type, User publisher, String text) {
		this.tid = tid;
		this.postTime = postTime;
		this.type = type;
		this.publisher = publisher;
		this.text = text;
	}

	public int compareTo(Text t) {
		if (postTime < t.postTime) {
			return -1;
		} else if (postTime > t.postTime) {
			return 1;
		} else if (tid < t.tid) {
			return -1;
		} else if (tid > t.tid) {
			return 1;
		} else {
			return 0;
		}
	}
}
