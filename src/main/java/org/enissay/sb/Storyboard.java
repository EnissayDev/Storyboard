package org.enissay.sb;

import org.enissay.osu.Beatmap;
import org.enissay.osu.BeatmapManager;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.SBObject;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class Storyboard{

    /**
     * STRUCTURE OF SPRITE
     *
     * -> Basic image
     * Sprite,(layer),(origin),"(filepath)",(x),(y)
     *
     * -> Moving image
     * Animation,(layer),(origin),"(filepath)",(x),(y),(frameCount),(frameDelay),(looptype)
     *
     * (frameCount) indicates how many frames the animation has. If we have "sample0.png" and "sample1.png", for instance, our frameCount = 2.
     * (frameDelay) indicates how many milliseconds should be in between each frame. For instance, if we wanted our animation to advance at 2 frames per second, frameDelay = 500.
     * (looptype) indicates if the animation should loop or not. Valid values are:
     * LoopForever (default if you leave this value off; the animation will return to the first frame after finishing the last frame)
     * LoopOnce (the animation will stop on the last frame and continue to display that last frame; useful for, like, an animation of someone turning around)
     *
     * -> Example
     * Sprite,Pass,Centre,"Sample.png",320,240
     *
     * Animation,Fail,BottomCentre,"Other\Play3\explosion.png",418,108,12,31,LoopForever
     */

    /**
     * STRUCTURE OF COMMAND
     * _(event),(easing),(starttime_of_first),(endtime_of_first),(value(s)_1),(value(s)_2),(value(s)_3),(value(s)_4)
     */

    /**
     *
     * EXAMPLE
     *
     * [Events]
     * //Background and Video events
     * //Storyboard Layer 0 (Background)
     * Sprite,Background,Centre,"bg.jpg",320,240
     *  S,0,157662,,0.7619048
     *  F,0,189965,190465,1,0
     * //Storyboard Layer 1 (Fail)
     * //Storyboard Layer 2 (Pass)
     * //Storyboard Layer 3 (Foreground)
     * Sprite,Foreground,Centre,"sb\particle2.png",0,0
     *  L,200595,28
     *   F,1,0,403,0,0.5279025
     *   M,0,0,2017,-24.2004,-160.9214,133.8683,376.307
     *   F,2,1613,2017,0.5279025,0
     *  R,0,200595,,1.284642
     *  C,0,200595,,173,173,173
     * //Storyboard Layer 4 (Overlay)
     * Sprite,Overlay,Centre,"sb\blood.png",320,240
     *  S,0,25420,,0.9876543
     *  F,0,41732,42232,0.5,0
     * //Storyboard Sound Samples
     */

    private String path, diffName;
    private Beatmap beatmap;
    private LinkedList<SBObject> objects;
    private LinkedList<String> texts;
    private LinkedList<Class<? extends Effect>> effects;

    public Storyboard(String path, LinkedList<SBObject> obj, String diffName) {
        this.path = path;
        this.objects = obj;
        this.beatmap = BeatmapManager.detectBeatmap(path, diffName);
    }

    public Storyboard(String path, String diffName) {
        this.path = path;
        this.objects = new LinkedList<>();
        this.beatmap = BeatmapManager.detectBeatmap(path, diffName);
        this.effects = new LinkedList<>();
        this.texts = new LinkedList<>();
        /*JavaClassFinder classFinder = new JavaClassFinder();
        List<Class<? extends Effect>> classes = classFinder.findAllMatchingTypes(Effect.class);
        classes.forEach(clazz -> {
            if (!clazz.getSimpleName().equals("Effect")) {
                try {
                    Method renderMethod = clazz.getMethod("render", Storyboard.class);
                    renderMethod.invoke(clazz.newInstance(), this);
                } catch (NoSuchMethodException e) {
                    System.err.println("No render method found in class " + clazz.getSimpleName());
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    System.err.println("Error invoking render method for class " + clazz.getSimpleName() + ": " + e.getMessage());
                }
            }
        });*/

    }

    public LinkedList<String> getTexts() {
        return texts;
    }

    public void addText(String text) {
        this.texts.add(text);
    }

    public String getPath() {
        return path;
    }

    public Storyboard addObject(final SBObject obj) {
        if (!objects.contains(obj)) objects.add(obj);
        return this;
    }

    public SBObject getSBObject(String name) {
        return objects.stream().filter(object -> object.getName().equals(name)).findAny().orElse(null);
    }

    public LinkedList<SBObject> getObjects() {
        return objects;
    }

    public List<SBObject> getObjects(final Layer layer) {
        return this.getObjects().stream().filter(sbObject -> sbObject.getLayer() == layer).collect(Collectors.toList());
    }

    public Storyboard addEffect(Class<? extends Effect> clazz, long startTime, long endTime, String[] params) {
        Map<Class<? extends Effect>, Object[]> map = new HashMap<>();
        map.put(clazz, new Object[]{startTime, endTime, params});
        addEffects(map);
        return this;
    }

    public void addEffects(Map<Class<? extends Effect>, Object[]> effects) {
        /*Arrays.asList(effects).forEach(effect -> {
            if (!this.effects.contains(effect)) this.effects.add(effect);
        });*/
        effects.keySet().forEach(clazz -> {
            if (!clazz.getSimpleName().equals("Effect")) {
                try {
                    if (!this.effects.contains(clazz)) this.effects.add(clazz);
                    Method renderMethod = clazz.getMethod("render", Storyboard.class, long.class, long.class, String[].class);
                    try {
                        final Object[] values = effects.get(clazz);
                        if (values.length >= 3)
                            renderMethod.invoke(clazz.newInstance(), this, values[0], values[1], values[2]);
                        else throw new RuntimeException("Not enough arguments for the effect " + clazz.getSimpleName() + " expected 3 got " + values.length);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        System.err.println("Error invoking render method for class " + clazz.getSimpleName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }

                } catch (NoSuchMethodException e) {
                    System.err.println("No render method found in class " + clazz.getSimpleName());
                }
            }
        });
    }

    public LinkedList<Class<? extends Effect>> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Events]\n");
        stringBuilder.append("//Background and Video events\n");
        stringBuilder.append("//Storyboard Layer 0 (Background)\n");
        getObjects(Layer.BACKGROUND).stream().forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 1 (Fail)\n");
        getObjects(Layer.FAIL).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 2 (Pass)\n");
        getObjects(Layer.PASS).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 3 (Foreground)\n");
        getObjects(Layer.FOREGROUND).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 4 (Overlay)\n");
        getObjects(Layer.OVERLAY).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Sound Samples\n");
        return stringBuilder.toString();
    }

    public Beatmap getBeatmap() {
        if (beatmap == null && diffName != null) {
            return BeatmapManager.detectBeatmap(path, diffName);
        }
        //System.out.println(path + "\\" + beatmap.getArtist() + " - " + beatmap.getTitle() + " (" + beatmap.getMapper() + ").osb");
        return beatmap;
    }

    public void write() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sb").getFile());
        String destinationPath = path + "\\"; // Specify the destination path here
        File sourceDir = file;
        try {
            FileOutputStream outputStream = new FileOutputStream(path + "\\" + beatmap.getArtist() + " - " + beatmap.getTitle() + " (" + beatmap.getMapper() + ").osb");
            byte[] strToBytes = toString().getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
            final FileWriter fileWriter = new FileWriter(path + "\\" + beatmap.getArtist() + " - " + beatmap.getTitle() + " (" + beatmap.getMapper() + ").osb");
            fileWriter.write(toString());
            fileWriter.close();

            File destinationDir = new File(destinationPath);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            deleteDirectory(new File(destinationPath + File.separator + "sb").toPath());
            copyDirectory(sourceDir, new File(destinationPath + File.separator + "sb"));

            System.out.println("Successfully copied 'sb' directory to: " + destinationPath);
            /*deleteDirectory(new File(path + "\\" + file.getName()).toPath());

            if (file.exists()) {
                Files.copy(file.toPath(),
                        (new File(path + "\\" + file.getName())).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                for (File listFile : file.listFiles()) {
                    Files.copy(listFile.toPath(),
                            (new File(path + "\\" + file.getName() + "\\" + listFile.getName())).toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }*/
            //Arrays.stream(file.list()).forEach(name -> System.out.println(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyDirectory(File sourceDir, File destinationDir) throws IOException {
        if (sourceDir.isDirectory()) {
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            String[] files = sourceDir.list();
            for (String file : files) {
                File srcFile = new File(sourceDir, file);
                File destFile = new File(destinationDir, file);
                // Recursive copy
                copyDirectory(srcFile, destFile);
            }
        } else {
            // Copy the file
            try (InputStream in = new FileInputStream(sourceDir);
                 OutputStream out = new FileOutputStream(destinationDir)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }

    private void deleteDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}