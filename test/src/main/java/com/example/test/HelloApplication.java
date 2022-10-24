package com.example.test;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HelloApplication extends Application {
    private double xoff = 0;
    private double yoff = 0;
    private double zoff = 0;
    private final double[][] localToWorld = {{0.718672, 0.615033, -0.3324214, 0},
            {-0.393732, 0.744416, 0.5539277, 0},
            {0.573024, -0.259959, 0.777216, 0},
            {0.526967, 1.254234, -2.53215, 1}};
    private final double[][] inverse = {{0.71506512294878439573, -0.38753000039982901159, 0.58203453231055897194, 0},
            {0.6169530762982229319, 0.74126839150965075212, -0.26443188230077389605	, 0},
            {-0.32084642139533217849, 0.53365265349345474029, 0.76907680293026311148, 0},
            {-1.963050513278967783, 0.62578006850743281385, 1.9723642926173962546, 1}};
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Pane pane = new Pane();
        Canvas canvas = new Canvas(Constants.width, Constants.height);
        root.getChildren().add(canvas);
        root.getChildren().add(pane);
        Scene scene = new Scene(root,Constants.width,Constants.height);
        AtomicInteger W = new AtomicInteger();
        AtomicInteger S = new AtomicInteger();
        AtomicInteger A = new AtomicInteger();
        AtomicInteger D = new AtomicInteger();
        AtomicReference<Double> yspeed = new AtomicReference<>((double) 0);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                W.set(1);
            }
            if (event.getCode() == KeyCode.S) {
                S.set(1);
            }
            if (event.getCode() == KeyCode.D) {
                D.set(1);
            }
            if (event.getCode() == KeyCode.A) {
                A.set(1);
            }
            if (event.getCode() == KeyCode.SPACE) {
                if (yoff <= 0) {
                    yspeed.set(7d);
                }
            }

        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W) {
                W.set(0);
            }
            if (event.getCode() == KeyCode.S) {
                S.set(0);
            }
            if (event.getCode() == KeyCode.D) {
                D.set(0);
            }
            if (event.getCode() == KeyCode.A) {
                A.set(0);
            }
        });
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                double delta = Time.deltaTime();
                xoff-=50*delta * D.get();
                xoff+=50*delta * A.get();
                zoff+=0.1*delta * W.get();
                zoff-=0.1*delta * S.get();
                yoff+=yspeed.get();
                yspeed.set(yspeed.get()-(9.8*delta));
                if (yoff<0) {
                    yoff = 0;
                }
                System.out.println(yspeed.get());
                drawCube(canvas.getGraphicsContext2D(), 0+ xoff , Constants.footLevel + yoff, -1 +zoff);

            }
        }.start();

        stage.setScene(scene);
        drawCube(canvas.getGraphicsContext2D(), 0, 0, -1);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public double[] localToWorldCoords(double[] local) {
         double[] world = {(local[0]*localToWorld[0][0])+(local[1]*localToWorld[1][0])+(local[2]*localToWorld[2][0])+localToWorld[3][0],
                 (local[0]*localToWorld[0][1])+(local[1]*localToWorld[1][1])+(local[2]*localToWorld[2][1])+localToWorld[3][1],
                 (local[0]*localToWorld[0][3])+(local[1]*localToWorld[1][3])+(local[2]*localToWorld[2][3])+localToWorld[3][3]};
         return world;
    }
    public double[] worldToLocalCoords(double[] world) {
        double[] local = {(world[0]*inverse[0][0])+(world[1]*inverse[1][0])+(world[2]*inverse[2][0])+inverse[3][0],
                (world[0]*inverse[0][1])+(world[1]*inverse[1][1])+(world[2]*inverse[2][1])+inverse[3][1],
                (world[0]*inverse[0][3])+(world[1]*inverse[1][3])+(world[2]*inverse[2][3])+inverse[3][3]};
        return local;
    }
    public double[] cameraToScreen(double[] camera) {
        double[] screen = {camera[0]/-camera[2], camera[1]/-camera[2]};
        screen[0] = (screen[0] + (Constants.width/2d))/Constants.width;
        screen[1] = (screen[1] + (Constants.height/2d))/Constants.height;
        screen[0] *= Constants.width;
        screen[1] *= Constants.height;
        return screen;
    }
    public void drawFlatSquare(GraphicsContext gc, double xpos, double ypos, double zpos) {
        double[] coords1 = cameraToScreen(new double[]{xpos, ypos, zpos});
        double[] coords2 = cameraToScreen(new double[]{xpos, ypos, zpos-0.1});
        double[] coords3 = cameraToScreen(new double[]{xpos-100, ypos, zpos-.1});
        double[] coords4 = cameraToScreen(new double[]{xpos-100, ypos, zpos});
        gc.setLineWidth(5);
        if (!(zpos > 0)) {
            gc.strokeLine(coords1[0],coords1[1],coords4[0],coords4[1]);
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords4[0],coords4[1],coords3[0],coords3[1]);
        }
        if (!(zpos-0.1 > 0)) {
            gc.strokeLine(coords3[0], coords3[1], coords2[0], coords2[1]);
        }


    }

    public void drawVerticalSquare(GraphicsContext gc, double xpos, double ypos, double zpos) {
        double[] coords1 = cameraToScreen(new double[]{xpos, ypos, zpos});
        double[] coords2 = cameraToScreen(new double[]{xpos, ypos-100, zpos});
        double[] coords3 = cameraToScreen(new double[]{xpos-100, ypos-100, zpos});
        double[] coords4 = cameraToScreen(new double[]{xpos-100, ypos, zpos});
        gc.setLineWidth(5);
        if (!(zpos > 0)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords3[0], coords3[1], coords2[0], coords2[1]);
            gc.strokeLine(coords4[0], coords4[1], coords3[0], coords3[1]);
            gc.strokeLine(coords1[0], coords1[1], coords4[0], coords4[1]);
        }
    }

    public void drawCube(GraphicsContext gc, double xpos, double ypos, double zpos) {
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, xpos, ypos, zpos);
        drawFlatSquare(gc, xpos, ypos-100, zpos);
        drawVerticalSquare(gc, xpos, ypos, zpos);
        drawVerticalSquare(gc, xpos, ypos, zpos-.1);
    }
}