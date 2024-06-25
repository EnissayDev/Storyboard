package org.enissay.sb.text.filters;

import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.text.SBText;
import org.enissay.sb.text.TextFilter;

import java.util.LinkedList;
import java.util.Random;

public class ShakeFilter implements TextFilter {

    private int STRENGTH;
    private int ITERATIONS;
    private int TIME_BETWEEN_SHAKES;

    public ShakeFilter(int strength, int iterations, int timeBetweenShakes) {
        this.STRENGTH = strength;
        this.ITERATIONS = iterations;
        this.TIME_BETWEEN_SHAKES = timeBetweenShakes;
    }

    @Override
    public void apply(SBText text) {
        /*int interval = 10;
        int verticalStrength = 3;
        int horizontalStrength = 3;
        int iterations = 5;
        int loopCount = 10;*/

        /*double startX = text.getX();
        double startY = text.getY();*/

        //long duration = (text.getEndTime() - LIFE_TIME)- text.getStartTime();

        //int loopCount = Math.max(1, (int) Math.floor(duration / LIFE_TIME));
        //int loopCount = LIFE_TIME/100;
        int loopCount = 5;
        Random random = new Random();
        //float loopDuration = duration / loopCount;

        //int loopCount = (int) (duration * iterations / 1000);
        //Random random = new Random();

        if (text.isSeperatedChars()) {
            int[][] values = getRandomValues(ITERATIONS, STRENGTH);

            int finalLoopCount = loopCount;
            text.getChars().forEach(sbChar -> {
                final double startX = sbChar.getSprite().getX();
                final double startY = sbChar.getSprite().getY();
                final Command loop = sbChar.getSprite().createLoop(text.getStartTime(), finalLoopCount);

                for (int i = 0; i < ITERATIONS; i++) {
                /*int randomX = random.nextInt(-horizontalStrength, horizontalStrength);
                int randomY = random.nextInt(-verticalStrength, verticalStrength);*/
                    int randomX = values[i][0];
                    int randomY = values[i][1];

                    loop.addSubCommand(sbChar.getSprite().MoveX(TIME_BETWEEN_SHAKES * i, startX + randomX));
                    loop.addSubCommand(sbChar.getSprite().MoveY(TIME_BETWEEN_SHAKES * i, startY + randomY));
                }
                loop.addSubCommand(sbChar.getSprite().MoveX(TIME_BETWEEN_SHAKES * (ITERATIONS - 1), startX));
                loop.addSubCommand(sbChar.getSprite().MoveY(TIME_BETWEEN_SHAKES * (ITERATIONS - 1), startY));
                text.getStoryboard().addObject(sbChar.getSprite());
            });
        }else {
            final double startX = text.getMainSprite().getX();
            final double startY = text.getMainSprite().getY();
            final Command loop = text.getMainSprite().createLoop(text.getStartTime(), loopCount);
            for (int i = 0; i < ITERATIONS; i++) {
                int randomX = random.nextInt(-STRENGTH, STRENGTH);
                int randomY = random.nextInt(-STRENGTH, STRENGTH);

                Command moveX = text.getMainSprite().MoveX(TIME_BETWEEN_SHAKES * i, startX, startX + randomX);
                Command moveY = text.getMainSprite().MoveY(TIME_BETWEEN_SHAKES * i, startY, startY + randomY);
                loop.addSubCommand(moveX);
                loop.addSubCommand(moveY);
            }
            text.getStoryboard().addObject(text.getMainSprite());
        }

        /**
         * int interval = 50;
         *             Vector2 initialPostion = new Vector2(320, 240);
         *             int verticalStrenght = 7;
         *             int horizontalStength = 3;
         *             int iterations = 5;
         *             int loopCount = 100;
         *             double starttime = 0;
         *
         *             text.StartLoopGroup(starttime, loopCount);
         *             for (int i = 0; i < iterations; i++)
         *             {
         *                 int randomX = Random(-horizontalStength, horizontalStength);
         *                 int randomY = Random(-verticalStrenght, verticalStrenght);
         *
         *                 text.MoveX(interval * i, initialPostion.X + randomX);
         *                 text.MoveY(interval * i, initialPostion.Y + randomY);
         *             }
         *             text.EndGroup();
         */
    }

    public int[][] getRandomValues(int iterations, int strength) {
        Random random = new Random();
        final int[][] array = new int[iterations][iterations];

        for (int i = 0; i < iterations; i++) {
            int randomX = random.nextInt(-strength, strength);
            int randomY = random.nextInt(-strength, strength);

            array[i] = new int[]{randomX, randomY};
        }
        return array;
    }
}
