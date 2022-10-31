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
    private double xrot = 0;
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
        AtomicInteger UP = new AtomicInteger();
        AtomicInteger DOWN = new AtomicInteger();
        AtomicReference<Double> yspeed = new AtomicReference<>((double) 0);
        Cube cube = new Cube(0 + xoff, Constants.footLevel + yoff, -1000 + zoff, 100, 100, 100, xrot, 0, zrot, canvas);
        Pyramid pyramid1 = new Pyramid(200 + xoff, Constants.footLevel + yoff, -500 + zoff, 300, 100, 200, xrot, 0, zrot, canvas);
        Pyramid pyramid2 = new Pyramid(300 + xoff, Constants.footLevel + yoff, -500 + zoff, 100, 300, 100, xrot, 0, zrot, canvas);

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
            if (event.getCode() == KeyCode.UP) {
                UP.set(1);
            }
            if (event.getCode() == KeyCode.DOWN) {
                DOWN.set(1);
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
            if (event.getCode() == KeyCode.UP) {
                UP.set(0);
            }
            if (event.getCode() == KeyCode.DOWN) {
                DOWN.set(0);
            }
        });
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                double delta = Time.deltaTime();
                zrot += 1 * delta * RIGHT.get();
                zrot -= 1 * delta * LEFT.get();
                xoff -= (Constants.movementXSpeed * delta * D.get()) * Math.cos(zrot) + (Constants.movementZSpeed * delta * W.get()) * Math.sin(zrot);
                xoff += (Constants.movementXSpeed * delta * A.get()) * Math.cos(zrot) + (Constants.movementZSpeed * delta * S.get()) * Math.sin(zrot);
                zoff += (Constants.movementZSpeed * delta * W.get()) * Math.cos(zrot) + (Constants.movementXSpeed * delta * A.get()) * Math.sin(zrot);
                zoff -= (Constants.movementZSpeed * delta * S.get()) * Math.cos(zrot) + (Constants.movementXSpeed * delta * D.get()) * Math.sin(zrot);
                yoff += yspeed.get();

                yspeed.set(yspeed.get() - (9.8 * delta));
                if (yoff < -(50 * SHIFT.get())) {
                    yspeed.set(-5d);
                    yoff = -(50 * SHIFT.get());
                }

                drawScreen(canvas, cube, pyramid1, pyramid2);

            }
        }.start();
        stage.setScene(scene);
        drawScreen(canvas);
        stage.show();
        stage.setFullScreen(true);
    }

    public void drawScreen(Canvas canvas, Object... entity) {
        for (Object ob : entity) {
            ob.draw(canvas.getGraphicsContext2D(),xoff, yoff, zoff, xrot, 0, zrot);
        }
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
}

