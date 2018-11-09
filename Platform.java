import java.util.*;

public class Platform {
	private Set<User> users;

	Platform() {
		users = new TreeSet<User>();
	}

	private User findUser(int uid) {
		for (User u : users) {
			if (u.uid() == uid) {
				return u;
			}
		}
		return null;
	}

	private Text findText(int tid) {
		for (User u : users) {
			Text t = u.findText(tid);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	public void performAction(String actionMsg) {
		try {
			String tokens[] = actionMsg.split(",");
			String ans = actionMsg, text, type;
			int t = Integer.parseInt(tokens[1]);
			int uid = Integer.parseInt(tokens[2]), tid, uid2;
			User u, v;
			Text tmp;
			u = findUser(uid);
			if (u == null) {
				u = new User(uid);
				users.add(u);
			}
			switch (tokens[0]) {
				case "PUBLISH":
					type = tokens[3];
					if (tokens[3].equals("NEW")) {
						tid = Integer.parseInt(tokens[5]);
						text = tokens[4];
					} else if (tokens[3].substring(0,5).equals("REPLY")) {
						tid = Integer.parseInt(tokens[5]);
						text = tokens[4];
						int replyToId = Integer.parseInt(tokens[3].substring(6, tokens[3].length()-1));
						if (findText(replyToId) == null) {
							throw new Exception("Text with tid "+replyToId+" doesn't exist");
						}
					} else if (tokens[3].substring(0,6).equals("REPOST")) {
						tid = Integer.parseInt(tokens[4]);
						int repostOfId = Integer.parseInt(tokens[3].substring(7, tokens[3].length()-1));
						tmp = findText(repostOfId);
						if (tmp == null) {
							throw new Exception("Text with tid "+repostOfId+" doesn't exist");
						} else {
							text = tmp.text();
						}
					} else {
						throw new Exception("Invalid publish format");
					}
					if (findText(tid) != null) {
						throw new Exception("Text with "+tid+" already exists");
					}
					u.post(new Text(tid, t, type, u, text));
					break;
				case "SUBSCRIBE":
					uid2 = Integer.parseInt(tokens[3]);
					v = findUser(uid2);
					if (v == null) {
						v = new User(uid2);
						users.add(v);
					}
					u.subscribe(v, t);
					break;
				case "UNSUBSCRIBE":
					uid2 = Integer.parseInt(tokens[3]);
					v = findUser(uid2);
					if (v == null) {
						v = new User(uid2);
						users.add(v);
					}
					u.unsubscribe(v);
					break;
				case "READ":
					Set<Text> updates = new TreeSet<Text>();
					for (User user : users) {
						if (user.uid() == u.uid()) continue;
						updates.addAll(user.getUpdates(u, t));
					}
					if (updates.size() == 0) {
						throw new Exception("User "+uid+" has no updates available");
					} else {
						ans += ",[";
						Iterator<Text> it = updates.iterator();
						while (it.hasNext()) {
							ans += it.next().text();
							if (it.hasNext()) {
								ans += ", ";
							} else {
								ans += "]";
							}
						}
					}
					break;
				default:
					throw new Exception("Unknown query");
			}
			System.out.println(ans);
		} catch (Exception e) {
			System.out.println(actionMsg + ": " + e.getMessage());
		}
	}
}
