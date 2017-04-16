package com.moorhenapps.bluehex.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.moorhenapps.bluehex.utils.Colour;
import com.moorhenapps.bluehex.utils.FloatPoint;
import com.moorhenapps.bluehex.utils.IntPoint;

import java.util.Arrays;
import java.util.Random;

public class ColouredHexDrawer {

    private int speed;
    private int[] colours;
    private int horzTileCount;
    private int vertTileCount;

    private int[] currentColour;
    private int[] targetColour;
    private long[] startTime;
    private long[] endTime;

    private int width, height;
    private Random random = new Random();
    private FloatPoint center = new FloatPoint(0,0);
    private FloatPoint[] corner = new FloatPoint[] {new FloatPoint(0,0),new FloatPoint(0,0),new FloatPoint(0,0),new FloatPoint(0,0),new FloatPoint(0,0),new FloatPoint(0,0)};
    private float size = 0;
    private float halfSize = 0;
    private Paint paint = new Paint();
    private Path path = new Path();
    private IntPoint hexPoint = new IntPoint(0, 0);

    ColouredHexDrawer() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeWidth(0);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        resetColours();
    }

    public void setColour(Colour colour){
        colours = colour.getColourList();
        resetColours();
    }

    public void setTileSize(int sizeDp){
        size = sizeDp;
        halfSize = sizeDp / 2;
        calcSizes();
    }

    public void setCanvasSize(int width, int height){
        this.width = width;
        this.height = height;
        calcSizes();
    }

    private void calcSizes(){
        if(width == 0 || size == 0){
            return;
        }
        horzTileCount = (int) (width/size);
        vertTileCount = (int) (height/size);

        currentColour = new int[horzTileCount * vertTileCount];
        targetColour = new int[horzTileCount * vertTileCount];
        startTime = new long[horzTileCount * vertTileCount];
        endTime = new long[horzTileCount * vertTileCount];

        center = getPxCenterForCell(new IntPoint(0,0));
        for(int i = 0; i < 6; i++){
            corner[i] = hexCorner(center, i);
        }
        path.reset();
        path.moveTo(corner[0].getX(), corner[0].getY());
        for(int i = 1; i < 6; i++) {
            path.lineTo(corner[i].getX(), corner[i].getY());
        }
        path.close();

        resetColours();
    }

    private void resetColours(){
        if(startTime == null){
            return;
        }
        Arrays.fill(startTime, System.currentTimeMillis());
        Arrays.fill(endTime, System.currentTimeMillis() + 100);
        for(int i = 0; i < currentColour.length; i++){
            currentColour[i] = colours[random.nextInt(colours.length)];
            targetColour[i] = colours[random.nextInt(colours.length)];
        }
    }

    void draw(Canvas canvas){
        for(int x = 0; x < horzTileCount; x++){
            for(int y = 0; y < vertTileCount; y++){
                drawHexCell(canvas, hexPoint.set(x, y));
            }
        }
    }

    private int getCellIndex(IntPoint cell){
        // r , q + r * 0.5
        return cell.getX() + cell.getY() * horzTileCount;
    }

    private float calcFraction(double start, double end){
        if(end - start == 0){
            return 0;
        }
        return (float) ((System.currentTimeMillis() - start) / (end - start));
    }

    private FloatPoint getPxCenterForCell(IntPoint cell){
        center.setX((float) (cell.getX() * size * 1.5));
        center.setY((float) (cell.getY() * size * 1.72));
        if(cell.getX() % 2 == 0){
            center.setY(center.getY() + size * 0.85f);
        }
        return center;
    }

    private void drawHexCell(Canvas canvas, IntPoint cell){
        int index = getCellIndex(cell);
        center = getPxCenterForCell(cell);

        canvas.save();
        canvas.translate(center.getX() - halfSize, center.getY() - halfSize);

        float fraction = calcFraction(startTime[index], endTime[index]);
        if(fraction > 1){
            fraction = 0;
            currentColour[index] = targetColour[index];
            targetColour[index] = colours[random.nextInt(colours.length)];
            startTime[index] = System.currentTimeMillis();
            endTime[index] = startTime[index] + random.nextInt(speed)+400;
        }
        int colour = evaluate(fraction, currentColour[index], targetColour[index]);

        paint.setColor(colour);

        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private FloatPoint hexCorner(FloatPoint center, int cornerIndex){
        double angle = 2 * Math.PI / 6 * cornerIndex;
        corner[cornerIndex].setX((float) (center.getX() + size * Math.cos(angle)));
        corner[cornerIndex].setY((float) (center.getY() + size * Math.sin(angle)));
        return corner[cornerIndex];
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int)(fraction * (endA - startA))) << 24 |
                (startR + (int)(fraction * (endR - startR))) << 16 |
                (startG + (int)(fraction * (endG - startG))) << 8 |
                (startB + (int)(fraction * (endB - startB)));
    }
}
