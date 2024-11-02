package org.enissay.osu;

import org.enissay.osu.data.OsuHitObject;
import org.enissay.osu.data.SliderNode;
import org.enissay.osu.data.TimingPoint;
import org.enissay.osu.data.curves.CurveType;
import org.enissay.osu.data.curves.HitObjectType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

    private static String extractValue(String line, String key, String currentValue) {
        String pattern = "^" + key + ":(.*)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return currentValue; // Return the current value if key not found
        }
    }

    private static double extractDoubleValue(String line, String key, double currentValue) {
        String pattern = "^" + key + ":(.*)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);

        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1).trim());
            } catch (NumberFormatException e) {
                // If parsing fails, return the current value
                System.err.println("Error: Invalid double format for key: " + key);
                return currentValue;
            }
        } else {
            return currentValue; // Return the current value if the key is not found
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
                        double sliderMulti = 1;

                        for(String line; (line = br.readLine()) != null; ) {
                            // Extract key-value pairs from each line
                            title = extractValue(line, "Title", title);
                            artist = extractValue(line, "Artist", artist);
                            diff = extractValue(line, "Version", diff);
                            mapper = extractValue(line, "Creator", mapper);
                            sliderMulti = extractDoubleValue(line, "SliderMultiplier", sliderMulti);
                        }
                        if (title != null && artist != null && diff != null && mapper != null) {
                            beatmap = new Beatmap(path, title, diff, artist, mapper, sliderMulti);
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
                                String[] args = line.split(",");
                                int x = Integer.parseInt(args[0]);
                                int y = Integer.parseInt(args[1]);
                                int time = Integer.parseInt(args[2]);
                                int type = Integer.parseInt(args[3]);
                                String objectParams = args[5];
                                CurveType curveType = Arrays.stream(CurveType.values())
                                        .filter(ct -> ct.getSymbol() == objectParams.charAt(0)).findFirst().orElse(null);
                                OsuHitObject object = new OsuHitObject(x, y, time, type);
                                if (curveType != null && object.getType() == HitObjectType.SLIDER) {
                                    String[] commas = line.split(",");
                                    int slides = 0;
                                    double length = 0;
                                    if (commas.length > 7) {
                                        slides = Integer.parseInt(commas[6]);
                                        length = Double.parseDouble(commas[7]);
                                    }
                                    object = new OsuHitObject(x, y, time, type, curveType, slides, length);
                                    String[] positions = line.split("\\|");

                                    for (int i = 0; i < positions.length; i++) {
                                        if (positions[i].contains(",")) {
                                            positions[i] = positions[i].split(",")[0];
                                        }
                                        String[] xy = positions[i].split(":");

                                        if (xy.length == 2) {
                                            try {
                                                Integer xPos = Integer.parseInt(xy[0]);
                                                Integer yPos = Integer.parseInt(xy[1]);

                                                object.addSliderNodes(new SliderNode(xPos, yPos));
                                            } catch (NumberFormatException e) {
                                                System.err.println("Error parsing position: " + Arrays.toString(xy) + " for: " + line);
                                            }
                                        }
                                    }
                                }

                                beatmap.addHitObject(object);

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