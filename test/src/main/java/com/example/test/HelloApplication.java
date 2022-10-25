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
    private double zrot = 0;
    private final double[][] localToWorld = {{0.718672, 0.615033, -0.3324214, 0},
            {-0.393732, 0.744416, 0.5539277, 0},
            {0.573024, -0.259959, 0.777216, 0},
            {0.526967, 1.254234, -2.53215, 1}};
    private final double[][] inverse = {{0.71506512294878439573, -0.38753000039982901159, 0.58203453231055897194, 0},
            {0.6169530762982229319, 0.74126839150965075212, -0.26443188230077389605, 0},
            {-0.32084642139533217849, 0.53365265349345474029, 0.76907680293026311148, 0},
            {-1.963050513278967783, 0.62578006850743281385, 1.9723642926173962546, 1}};

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Pane pane = new Pane();
        Canvas canvas = new Canvas(Constants.width, Constants.height);
        root.getChildren().add(canvas);
        root.getChildren().add(pane);
        Scene scene = new Scene(root, Constants.width, Constants.height);
        AtomicInteger W = new AtomicInteger();
        AtomicInteger S = new AtomicInteger();
        AtomicInteger A = new AtomicInteger();
        AtomicInteger D = new AtomicInteger();
        AtomicInteger SHIFT = new AtomicInteger();
        AtomicInteger RIGHT = new AtomicInteger();
        AtomicInteger LEFT = new AtomicInteger();
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
            if (event.getCode() == KeyCode.SHIFT) {
                SHIFT.set(1);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                RIGHT.set(1);
            }
            if (event.getCode() == KeyCode.LEFT) {
                LEFT.set(1);
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
            if (event.getCode() == KeyCode.SHIFT) {
                SHIFT.set(0);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                RIGHT.set(0);
            }
            if (event.getCode() == KeyCode.LEFT) {
                LEFT.set(0);
            }
        });
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                double delta = Time.deltaTime();
                xoff -= 60 * delta * D.get();
                xoff += 60 * delta * A.get();
                zoff += 15 * delta * W.get();
                zoff -= 15 * delta * S.get();
                yoff += yspeed.get();
                zrot += 1 * delta * RIGHT.get();
                zrot -= 1 * delta * LEFT.get();
                yspeed.set(yspeed.get() - (9.8 * delta));
                if (yoff < -(50 * SHIFT.get())) {
                    yspeed.set(-5d);
                    yoff = -(50 * SHIFT.get());
                }

                drawScreen(canvas);

            }
        }.start();

        stage.setScene(scene);
        drawScreen(canvas);
        stage.show();
    }

    public void drawScreen(Canvas canvas) {
        drawCube(canvas.getGraphicsContext2D(), 0 + xoff, Constants.footLevel + yoff, -100 + zoff, 100, 300, 50, zrot, 0, 0);
        drawPyramid(canvas.getGraphicsContext2D(), 200 + xoff, Constants.footLevel + yoff, -50 + zoff, 300, 100, 10);
        drawPyramid(canvas.getGraphicsContext2D(), 300 + xoff, Constants.footLevel + yoff, -50 + zoff, 100, 300, 10);
    }

    public static void main(String[] args) {
        launch();
    }

    public double[] localToWorldCoords(double[] local) {
        double[] world = {(local[0] * localToWorld[0][0]) + (local[1] * localToWorld[1][0]) + (local[2] * localToWorld[2][0]) + localToWorld[3][0],
                (local[0] * localToWorld[0][1]) + (local[1] * localToWorld[1][1]) + (local[2] * localToWorld[2][1]) + localToWorld[3][1],
                (local[0] * localToWorld[0][3]) + (local[1] * localToWorld[1][3]) + (local[2] * localToWorld[2][3]) + localToWorld[3][3]};
        return world;
    }

    public double[] worldToLocalCoords(double[] world) {
        double[] local = {(world[0] * inverse[0][0]) + (world[1] * inverse[1][0]) + (world[2] * inverse[2][0]) + inverse[3][0],
                (world[0] * inverse[0][1]) + (world[1] * inverse[1][1]) + (world[2] * inverse[2][1]) + inverse[3][1],
                (world[0] * inverse[0][3]) + (world[1] * inverse[1][3]) + (world[2] * inverse[2][3]) + inverse[3][3]};
        return local;
    }

    public double[] cameraToScreen(double[] camera) {
        double[] screen = {camera[0] / -(camera[2]/100), camera[1] / -(camera[2]/100)};
        screen[0] = (screen[0] + (Constants.width / 2d)) / Constants.width;
        screen[1] = (screen[1] + (Constants.height / 2d)) / Constants.height;
        screen[0] *= Constants.width;
        screen[1] *= Constants.height;
        return screen;
    }

    public void drawFlatSquare(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        double[] coords1 = rotate(pitch, roll, yaw, new double[]{xpos, ypos, zpos});
        double[] coords2 = rotate(pitch, roll, yaw, new double[]{xpos, ypos, zpos - lengthZ});
        double[] coords3 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos, zpos - lengthZ});
        double[] coords4 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos, zpos});
        coords1 = cameraToScreen(coords1);
        coords2 = cameraToScreen(coords2);
        coords3 = cameraToScreen(coords3);
        coords4 = cameraToScreen(coords4);
        gc.setLineWidth(5);
        if (!(zpos > 0)) {
            gc.strokeLine(coords1[0], coords1[1], coords4[0], coords4[1]);
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords4[0], coords4[1], coords3[0], coords3[1]);
        }
        if (!(zpos - 0.1 > 0)) {
            gc.strokeLine(coords3[0], coords3[1], coords2[0], coords2[1]);
        }


    }

    public void drawVerticalSquare(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        double[] coords1 = rotate(pitch, roll, yaw, new double[]{xpos, ypos, zpos});
        double[] coords2 = rotate(pitch, roll, yaw, new double[]{xpos, ypos - lengthY, zpos});
        double[] coords3 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos - lengthY, zpos});
        double[] coords4 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos, zpos});
        coords1 = cameraToScreen(coords1);
        coords2 = cameraToScreen(coords2);
        coords3 = cameraToScreen(coords3);
        coords4 = cameraToScreen(coords4);
        gc.setLineWidth(5);
        if (!(zpos > 0)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords3[0], coords3[1], coords2[0], coords2[1]);
            gc.strokeLine(coords4[0], coords4[1], coords3[0], coords3[1]);
            gc.strokeLine(coords1[0], coords1[1], coords4[0], coords4[1]);
        }
    }

    public void drawCube(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawFlatSquare(gc, xpos, ypos - lengthY, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos - lengthZ, lengthX, lengthY, lengthZ, pitch, roll, yaw);
    }

    public void drawSlantedTriangleFront(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ) {
        double[] coords1 = cameraToScreen(new double[]{xpos, ypos, zpos});
        double[] coords2 = cameraToScreen(new double[]{xpos - (lengthX / 2), ypos - lengthY, zpos - (lengthZ / 2)});
        double[] coords3 = cameraToScreen(new double[]{xpos - lengthX, ypos, zpos});
        if (!(zpos > 0)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords2[0], coords2[1], coords3[0], coords3[1]);
            gc.strokeLine(coords1[0], coords1[1], coords3[0], coords3[1]);
        }
    }

    public void drawSlantedTriangleBack(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ) {
        double[] coords1 = cameraToScreen(new double[]{xpos, ypos, zpos});
        double[] coords2 = cameraToScreen(new double[]{xpos - (lengthX / 2), ypos - lengthY, zpos + (lengthZ / 2)});
        double[] coords3 = cameraToScreen(new double[]{xpos - lengthX, ypos, zpos});
        if (!(zpos > -lengthZ)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
            gc.strokeLine(coords2[0], coords2[1], coords3[0], coords3[1]);
            gc.strokeLine(coords1[0], coords1[1], coords3[0], coords3[1]);
        }
    }

    public void drawPyramid(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ) {
        drawFlatSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, 0, 0, 0);
        drawSlantedTriangleFront(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ);
        drawSlantedTriangleBack(gc, xpos, ypos, zpos - lengthZ, lengthX, lengthY, lengthZ);
    }

    public double[] rotate(double pitch, double roll, double yaw, double[] coords) {
        double cosa = Math.cos(yaw);
        double sina = Math.sin(yaw);

        double cosb = Math.cos(pitch);
        var sinb = Math.sin(pitch);

        double cosc = Math.cos(roll);
        double sinc = Math.sin(roll);

        double Axx = cosa*cosb;
        double Axy = cosa*sinb*sinc - sina*cosc;
        double Axz = cosa*sinb*cosc + sina*sinc;

        double Ayx = sina*cosb;
        double Ayy = sina*sinb*sinc + cosa*cosc;
        double Ayz = sina*sinb*cosc - cosa*sinc;

        double Azx = -sinb;
        double Azy = cosb*sinc;
        double Azz = cosb*cosc;

        double px = coords[0];
        double py = coords[1];
        double pz = coords[2];

        return new double[] {Axx*px + Axy*py + Axz*pz, Ayx*px + Ayy*py + Ayz*pz, Azx*px + Azy*py + Azz*pz};
    }
}

