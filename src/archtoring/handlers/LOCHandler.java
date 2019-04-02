package archtoring.handlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class LOCHandler {
	public void execute() {
		try {
			// Get LOC from Sonarqube on same server
			URL url = new URL("http://localhost:9000/api/measures/component");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes("metricKeys=ncloc&component=" + ModelHandler.key);
			out.flush();
			out.close();
			
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			JsonElement jelement = new JsonParser().parse(content.toString());
			JsonObject jobject = jelement.getAsJsonObject();
			jobject = jobject.getAsJsonObject("component");
			JsonArray measures = jobject.getAsJsonArray("measures");
			jobject = measures.get(0).getAsJsonObject();
			JsonPrimitive value = jobject.getAsJsonPrimitive("value");
			int loc = Integer.parseInt(value.getAsString());
			
			// Post LOC to Archtoring DB
			String link = GithubHandler.output[0];
			String repoName = link.split("/")[4];
			if(repoName.endsWith(".git"))
				repoName = repoName.substring(0, repoName.indexOf("."));
			URL url2 = new URL("http://archtoringbd.herokuapp.com/locs/" + repoName);
			HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
			con2.setRequestMethod("PUT");
			con2.setDoOutput(true);
			con2.setRequestProperty("Content-Type", "application/json");
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("locs", loc);
			String query = new Gson().toJson(map);
			con2.setRequestProperty("Content-Length", Integer.toString(query.length()));
			DataOutputStream out2 = new DataOutputStream(con2.getOutputStream());
			out2.writeBytes(query);
			out2.flush();
			out2.close();

			con2.setConnectTimeout(5000);
			con2.setReadTimeout(5000);

			status = con2.getResponseCode();
			System.out.println(status);
			
			String date = GithubHandler.output[3];
			URL url3 = new URL("http://archtoringbd.herokuapp.com/dates/" + repoName);
			HttpURLConnection con3 = (HttpURLConnection) url3.openConnection();
			con3.setRequestMethod("PUT");
			con3.setDoOutput(true);
			con3.setRequestProperty("Content-Type", "application/json");
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("dates", date);
			String query2 = new Gson().toJson(map2);
			con3.setRequestProperty("Content-Length", Integer.toString(query2.length()));
			DataOutputStream out3 = new DataOutputStream(con3.getOutputStream());
			out3.writeBytes(query2);
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
