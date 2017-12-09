package grapher.graph;

import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;

import static java.lang.Math.*;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.TRANSPARENT;

public class LineGraph extends Pane {
    private int interval;
    private int currentX;

    private String path;
    private ArrayList<Point2D> points;

    //Styling values
    Paint stroke;
    Paint fill;
    double strokeWidth;
    double smoothing;
    StrokeLineJoin strokeLineJoin;


    public LineGraph(double width, double height) {
        this.interval = 1;
        this.currentX = 10;
        this.setWidth(width);
        this.setHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);
        this.points = new ArrayList<>();

        //Styling values
        this.stroke = BLACK;
        this.fill = TRANSPARENT;
        this.strokeWidth = 1;
        this.smoothing = 0.2;
        this.strokeLineJoin = StrokeLineJoin.ROUND;
    }

    public void render(){
        path= "M0,"+points.get(0).getY();
        renderPoints();
        SVGPath svg = new SVGPath();

        svg.setStrokeWidth(strokeWidth);
        svg.setStroke(stroke);
        svg.setFill(fill);
        svg.setContent(path);
        svg.setStrokeLineJoin(strokeLineJoin);

        this.getChildren().add(svg);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void addValue(int value){
        addPoint(new Point2D(currentX, value));
        currentX+= interval;
    }

    /**
     * Adds a point to the graph.
     * @param point using cartesian coordinates.
     */
    public void addPoint(Point2D point){
        point = translate(point);
        points.add(point);
    }

    public void renderPoints(){

        //Calculate smooth curve
        //see: https://medium.com/@francoisromain/smooth-a-svg-path-with-cubic-bezier-curves-e37b49d46c74
        Point2D controlStart = getControlPoint(null, null, points.get(0), false);
        Point2D controlEnd = getControlPoint(null, points.get(0), points.get(1), true);

        path += "C"+controlStart.getX()+","+controlStart.getY()+","+controlEnd.getX()+","+controlEnd.getY()+","+
                points.get(0).getX()+","+points.get(0).getY();

        controlStart = getControlPoint(null, points.get(0), points.get(1), false);
        controlEnd = getControlPoint(points.get(0), points.get(1), points.get(2), true);

        path += "C"+controlStart.getX()+","+controlStart.getY()+","+controlEnd.getX()+","+controlEnd.getY()+","+
                points.get(1).getX()+","+points.get(1).getY();

        for(int i = 2; i<points.size()-1; i++){
            controlStart = getControlPoint(points.get(i-2), points.get(i -1), points.get(i), false);
            controlEnd = getControlPoint(points.get(i-1), points.get(i), points.get(i+1), true);
            path += "C"+controlStart.getX()+","+controlStart.getY()+","+controlEnd.getX()+","+controlEnd.getY()+","+
                    points.get(i).getX()+","+points.get(i).getY();
        }
    }

    private Point2D getControlPoint(@Nullable Point2D prevPoint, Point2D point, @Nullable Point2D nextPoint, boolean reverse){
        if(point == null)
            point = new Point2D(0,0);
        if(prevPoint == null)
            prevPoint = point;
        if(nextPoint == null)
            nextPoint = point;

        double[] opposedLine = line(prevPoint, nextPoint);

        double length = opposedLine[0] * smoothing;
        double angle = reverse? opposedLine[1]+ PI: opposedLine[1];

        double x = point.getX() + sin(angle)*length;
        double y = point.getY() + cos(angle)*length;

        return new Point2D(x,y);
    }

    private double[] line(Point2D prevPoint, Point2D point){
        double lengthX = point.getX() - prevPoint.getX();
        double lengthY = point.getY() - prevPoint.getY();

        double length = sqrt(pow(lengthX,2) + pow(lengthY,2));
        double angle = atan2(lengthX, lengthY);

        return new double[]{length, angle};
    }

    private Point2D translate(Point2D point2D){
        return new Point2D(point2D.getX(), this.getHeight()-point2D.getY()+1);
    }

    private int translate(int v){
        return (int) this.getHeight() - v +1 ;
    }

}
