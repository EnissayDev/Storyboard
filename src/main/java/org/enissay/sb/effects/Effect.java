package org.enissay.sb.effects;

import org.enissay.sb.Storyboard;

public interface Effect {
    void render(Storyboard storyboard, long startTime, long endTime, Object... params);
}
