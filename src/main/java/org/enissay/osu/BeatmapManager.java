package org.enissay.osu;

import org.enissay.osu.data.TimingPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeatmapManager {

    private static LinkedList<Beatmap> beatmaps = new LinkedList<>();

    public static void addBeatmap(Beatmap beatmap) {
        if (beatmaps.contains(beatmap)) beatmaps.add(beatmap);
    }

    public static Beatmap getBeatmap(String name) {
        return beatmaps.stream().filter(beatmap -> beatmap.getTitle().contains(name)).findAny().orElse(null);
    }

    public static Beatmap getBeatmapByDiff(String diffName) {
        return beatmaps.stream().filter(beatmap -> beatmap.getDiffName().contains(diffName)).findAny().orElse(null);
    }

    public static String extractValue(String line, String key, String currentValue) {
        String pattern = "^" + key + ":(.*)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return currentValue; // Return the current value if key not found
        }
    }

    public static Beatmap detectBeatmap(String path, String diffName) {
        Beatmap beatmap = null;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
                //System.out.println("Found: " + filename[0] + "." + filename[1]);
                if(filename[0].contains("[" + diffName + "]") && filename[1].equals("osu")) {
                    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String title = null;
                        String artist = null;
                        String diff = null;
                        String mapper = null;

                        for(String line; (line = br.readLine()) != null; ) {
                            // Extract key-value pairs from each line
                            title = extractValue(line, "Title", title);
                            artist = extractValue(line, "Artist", artist);
                            diff = extractValue(line, "Version", diff);
                            mapper = extractValue(line, "Creator", mapper);
                        }
                        if (title != null && artist != null && diff != null && mapper != null) {
                            beatmap = new Beatmap(path, title, diff, artist, mapper);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //FOR TIMING POINTS
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        boolean timingPointsSectionReached = false;
                        boolean hitObjectsSectionReached = false;
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (timingPointsSectionReached && !hitObjectsSectionReached) {
                                if (!line.trim().equals("[HitObjects]")) {
                                    //HERE
                                    if (beatmap != null) {
                                        String[] args = line.split(",");
                                        if (args.length == 8) {
                                            //System.out.println(line);
                                            beatmap.addTimingPoint(new TimingPoint(Double.parseDouble(args[0]), Double.parseDouble(args[1]),
                                                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[6]),
                                                    (Integer.parseInt(args[6]) == 1), Integer.parseInt(args[7])));
                                        }
                                    }
                                    //System.out.println(line);
                                } else {
                                    hitObjectsSectionReached = true;
                                }
                            } else if (line.trim().equals("[TimingPoints]")) {
                                timingPointsSectionReached = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //FOR HITOBJECTS
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        boolean hitObjectsSectionReached = false;
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (hitObjectsSectionReached) {
                                //HERE
                                //System.out.println(line);
                            } else if (line.trim().equals("[HitObjects]")) {
                                hitObjectsSectionReached = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (beatmap != null) {
            addBeatmap(beatmap);
        }
        return beatmap;
    }


    public static LinkedList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}