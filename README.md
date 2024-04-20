# Storyboard

> [!WARNING]
> This is only in BETA

## Purpose:
Create osu storyboards using **Java** with custom Effects or use the ones already created for now 
for example: `Background`, `ProgressBar` and more to come...

## Tutorial:

> Creating a Storyboard:
```java
final Storyboard sb = new Storyboard(beatmapPath, diffName);
```

> Creating a Sprite:
```java
final Sprite sprite = new Sprite(spriteName, layer, origin, filePath);
```
- `spriteName`: This is a personal choice to get the sprite later by its name.
- `layer`: Layer of the sprite e.g: `Layer.BACKGROUND`.
- `origin`: Origin of the sprite e.g: `Origin.CENTRE`.
- `filePath`: Path of the sprite e.g: `sb\\img.png`

> Creating a Sprite with coordinates:
```java
final Sprite sprite = new Sprite(spriteName, Layer.BACKGROUND, Origin.CENTRE, filePath, x, y);
```
- `x`: x coordinate
- `y`: y coordinate

> Manipulation of a Sprite

Give this a read: [Osu storyboarding](https://osu.ppy.sh/wiki/en/Storyboard/Scripting).
```java
Sprite#(Command)(args);
```

### Example of creating a loop and add commands to it:
```java
Command loop = particle.createLoop(startTime, loopCount);
loop.addSubCommand(particle.Fade(Easing.EASING_OUT, 0, (int) (loopDuration * 0.2), 0, color.getAlpha()));
loop.addSubCommand(particle.Move(EASING, 0, (int) loopDuration, startPosition.x, startPosition.y, endPosition.x, endPosition.y));
```

### Add a Sprite to a Storyboard:
```java
sb.addObject(sprite);
```

### Adding an effect:
```java
sb.addEffect(clazz, startTime, endTime, parameters);
```
- `class`: Class of the desired Effect.
- `startTime`: Time when the effect will start in milliseconds.
- `endTime`: Time when the effect will finish in milliseconds.
- `parameters`: Usually depends on the effect for example:
    - **Progressbar**:
        - `x`: x coordinate
        - `y`: y coordinate
        - `barLength`: length of the bar
    - **Background**:
        - `path`: file path of the background image e.g: 'sb\\bg.png'
        - `startFade`: fade value when the bg will appear
        - `endFade`: fade value when the bg will disappear

### Creating an effect:
```java
public class TestEffect implements Effect {
    
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
    }
}
```

### Adding a text with or without effects:
```java
SBText sbText = new SBText(spriteName, storyboard, text, font, startTime, endTime, x, y, color);
```
- `spriteName`: This is a personal choice to get the sprite later by its name.
- `storyboard`: Storyboard instance.
- `text`: Text string.
- `font`: Font you can get a custom font using this:
    - `FontUtils.getCustomFont(fontName, size)`:
        - You have to add a font named fontName in the resources directory in a directory named "customFont"
    - `FontUtils.getFont(fontName, size)`:
        - You can also get a normal font like Calibri for example
- `startTime`: Time when the text will appear in milliseconds.
- `endTime`: Time when the text will disappear in milliseconds.
- `x`: x coordinate
- `y`: y coordinate

### Adding filters to the text:
`sbText.addFilter(filter)`
- `filter`: You can either create one or use an existing one like these:
  -  `new GlitchFilter()`
  -  `new ZoomFilter(scale)`
### Create a text filter:
```java
public class TestFilter implements TextFilter { 

    @Override
    public void apply(SBText text) {
        
    }
}
```

After finishing the text:
`sbText.apply();`

### Exporting the storyboard:
```java
sb.write();
```


