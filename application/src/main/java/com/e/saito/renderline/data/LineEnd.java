package com.e.saito.renderline.data;

/**
 * Created by e.saito on 2014/06/10.
 */
public class LineEnd implements Comparable<LineEnd> {
    public int lineIndex;
    public float y;

    public LineEnd(int lineIndex, float y) {
        this.lineIndex = lineIndex;
        this.y = y;
    }

    @Override
    public int compareTo(LineEnd another) {
        if (this.y > another.y) {
            return 1;
        } else if (this.y < another.y) {
            return -1;
        }
        return 0;
    }
}
