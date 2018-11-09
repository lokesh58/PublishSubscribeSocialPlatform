import java.util.Set;

public class User implements Comparable<User> {
	private int uid;
	private int upTime;
	class Sub implements Comparable<Sub> {
		User user;
		int subTime;
		Sub(User u, int t) {
			user = u;
			subTime = t;
		}
		public int compareTo(Sub s) {
			return user.compareTo(s.user);
		}
	}
	private Set<Sub> subscriptions;
	private Set<Text> posts;
	
	User(int uid) {
		this.uid = uid;
		upTime = -1;
		subscriptions = new TreeSet<Sub<();
		posts = new TreeSet<Sub>();
	}

	public int compareTo(User u) {
		if (uid < u.uid) {
			return -1;
		} else if (uid > u.uid) {
			return 1;
		} else {
			return 0;
		}
	}
}
