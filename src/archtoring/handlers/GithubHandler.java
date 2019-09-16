package archtoring.handlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class GithubHandler {
	public static String[] output;
	public static int[] issuesCount;
	public static HashMap<String, int[]> fileIssuesCount;
	public static HashMap<String, List<String>> dependencies;

	public GithubHandler() {
		try {
			issuesCount = new int[18];
			fileIssuesCount = new HashMap<String, int[]>();
			dependencies = new HashMap<String, List<String>>();
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c",
					"git remote get-url origin && git rev-parse HEAD && git log -1 --pretty=format:%ae%n%aI");

			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			output = new String[4];
			int index = 0;
			while ((line = reader.readLine()) != null) {
				output[index] = line;
				index++;
			}
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void execute() {
		try {
			String link = GithubHandler.output[0];
			String repoName = link.split("/")[4];
			if (repoName.endsWith(".git"))
				repoName = repoName.substring(0, repoName.indexOf("."));
			URL url = new URL("http://archtoringbd.herokuapp.com/history/" + repoName);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("commitId", GithubHandler.output[1]);
			map.put("date", GithubHandler.output[3]);
			// map.put("loc", locs);
			map.put("issues", issuesCount);
			HashMap<String, HashMap<String, Object>> map2 = new HashMap<String, HashMap<String, Object>>();
			map2.put("data", map);
			String query = new Gson().toJson(map2);
			con.setRequestProperty("Content-Length", Integer.toString(query.length()));
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(query);
			out.flush();
			out.close();

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();
			System.out.println(status);
			
			ArrayList<HashMap<String, Object>> files = new ArrayList<HashMap<String, Object>>();
			for(int i = 0; i < fileIssuesCount.size(); i++) {
				HashMap<String, Object> x = new HashMap<String, Object>();
				x.put("name", fileIssuesCount.keySet().toArray()[i]);
				x.put("issues", fileIssuesCount.values().toArray()[i]);
				files.add(x);
			}
			
			URL url2 = new URL("http://archtoringbd.herokuapp.com/files/" + repoName);
			HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
			con2.setRequestMethod("PUT");
			con2.setDoOutput(true);
			con2.setRequestProperty("Content-Type", "application/json");
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("commitId", GithubHandler.output[1]);
			map3.put("date", GithubHandler.output[3]);
			map3.put("files", files);
			HashMap<String, HashMap<String, Object>> map4 = new HashMap<String, HashMap<String, Object>>();
			map4.put("data", map3);
			String query2 = new Gson().toJson(map4);
			con2.setRequestProperty("Content-Length", Integer.toString(query2.length()));
			DataOutputStream out2 = new DataOutputStream(con2.getOutputStream());
			out2.writeBytes(query2);
			out2.flush();
			out2.close();

			con2.setConnectTimeout(5000);
			con2.setReadTimeout(5000);

			status = con2.getResponseCode();
			System.out.println(status);
			
			
			ArrayList<HashMap<String, Object>> files2 = new ArrayList<HashMap<String, Object>>();
			for (Entry<String, List<String>> ee : dependencies.entrySet()) {
				HashMap<String, Object> x = new HashMap<String, Object>();
			    String key = ee.getKey();
			    List<String> values = ee.getValue();
			    x.put("name", key);
				x.put("dependencies", values);
				files2.add(x);
			}
			
			URL url3 = new URL("http://archtoringbd.herokuapp.com/files/" + repoName + "Dependencies");
			HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
			con3.setRequestMethod("PUT");
			con3.setDoOutput(true);
			con3.setRequestProperty("Content-Type", "application/json");
			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("commitId", GithubHandler.output[1]);
			map5.put("date", GithubHandler.output[3]);
			map5.put("files", files2);
			HashMap<String, HashMap<String, Object>> map6 = new HashMap<String, HashMap<String, Object>>();
			map6.put("data", map5);
			String query3 = new Gson().toJson(map6);
			con3.setRequestProperty("Content-Length", Integer.toString(query3.length()));
			DataOutputStream out3 = new DataOutputStream(con3.getOutputStream());
			out3.writeBytes(query3);
			out3.flush();
			out3.close();

			con3.setConnectTimeout(5000);
			con3.setReadTimeout(5000);

			status = con3.getResponseCode();
			System.out.println(status);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
