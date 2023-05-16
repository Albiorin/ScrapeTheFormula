package com.albiorin;

import com.albiorin.data.DriverStanding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



@SpringBootApplication
public class Main {
    public static Document doc;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        try{
            doc = Jsoup.connect("https://www.espn.com/f1/standings")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get();
            System.out.println("Connected to www.espn.com");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        // Web scraper
        List<DriverStanding> driverStandings = new ArrayList<>();
        Elements rows = doc.select("tr");
        for (Element row : rows){
            Element driverCell = row.select("td:nth-child(1)").first();
            Element pointsCell = row.select("td:nth-child(2)").first();
            if (driverCell != null && pointsCell !=null){
                DriverStanding driverStanding = new DriverStanding();
                driverStanding.setPosition(driverCell.select("span.team-position.ml2.pr3").text());
                driverStanding.setName(driverCell.select("span.hide-mobile").text());
                driverStanding.setPoints(pointsCell.select("span.stat-cell").text());
                driverStandings.add(driverStanding);
            } else{
                System.out.println("Failed to fetch data from www.espn.com");
            }
        }
        System.out.println("Successfully fetched data from www.espn.com");
        int startIndex = 19;
        if (driverStandings.size() > startIndex + 1) {
            driverStandings.subList(startIndex + 1, driverStandings.size()).clear();
        }
        // JSON Serializer
        String path = "/Users/doruk/IdeaProjects/ScrapeTheFormula/src/main/resources/static/data.json";
        try(PrintWriter out = new PrintWriter(new FileWriter(path))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            out.write(gson.toJson(driverStandings));
            System.out.print("Successfully wrote data to: \n" + path);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}