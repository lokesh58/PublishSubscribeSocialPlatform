import java.util.*;

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
		subscriptions = new TreeSet<Sub>();
		posts = new TreeSet<Text>();
	}

	public int uid() {
		return uid;
	}

	public void post(Text t) {
		posts.add(t);
	}

	public void updated(int t) {
		upTime = t;
	}

	public boolean hasPosted(int tid) {
		for (Text post : posts) {
			if (post.tid() == tid) {
				return true;
			}
		}
		return false;
	}

	public boolean hasReplied(int tid, int t) {
		for (Text post : posts) {
			if (!post.type().equals("NEW") && post.type().substring(0,5).equals("REPLY")) {
				int id = Integer.parseInt(post.type().substring(6, post.type().length()-1));
				if (tid == id) {
					return post.postTime() < t;
				}
			}
		}
		return false;
	}

	public boolean hasSubscribed(User u) {
		return subscriptions.contains(new Sub(u, 0));
	}

	public void subscribe(User u, int subTime) throws Exception {
		if (hasSubscribed(u)) {
			throw new Exception("User "+uid+" has already subscribed user "+u.uid);
		} else {
			subscriptions.add(new Sub(u, subTime));
		}
	}

	public void unsubscribe(User u) throws Exception {
		if (hasSubscribed(u)) {
			subscriptions.remove(new Sub(u, 0));
		} else {
			throw new Exception("User "+uid+" has not subscribed user "+u.uid);
		}
	}

	public Set<Text> getUpdates(User u, int t) {
		boolean isSub = u.hasSubscribed(this);
		int t0 = u.upTime;
		if (isSub) {
			for (Sub s : u.subscriptions) {
				if (s.user.uid == uid) {
					t0 = Math.max(t0, s.subTime);
					break;
				}
			}
		}
		Set<Text> updates = new TreeSet<Text>();
		for (Text post : posts) {
			if (post.postTime() >= t0 && post.postTime() < t) {
				if (isSub||(!post.type().equals("NEW")&&post.type().substring(0,5).equals("REPLY")&&u.hasPosted(Integer.parseInt(post.type().substring(6,post.type().length()-1))))) {
					if (!u.hasReplied(post.tid(), t)) {
						updates.add(post);
					}
				}
			}
		}
		return updates;
	}

	public Text findText(int tid) {
		for (Text t : posts) {
			if (t.tid() == tid) {
				return t;
			}
		}
		return null;
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
