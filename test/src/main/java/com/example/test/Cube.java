package com.example.test;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Cube extends Object{
    private double xpos;
    private double ypos;
    private double zpos;
    private double lengthX;
    private double lengthY;
    private double lengthZ;
    private double pitch;
    private double roll;
    private double yaw;
    private Canvas canvas;

    public Cube(double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw, Canvas canvas) {
        super(xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw, canvas);
    }
    public Cube(Canvas canvas) {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, canvas);
    }

    @Override
    public void draw(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lengthZ = lengthZ;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawFlatSquare(gc, xpos, ypos - lengthY, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos - lengthZ, lengthX, lengthY, lengthZ, pitch, roll, yaw);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawFlatSquare(gc, xpos, ypos - lengthY, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos - lengthZ, lengthX, lengthY, lengthZ, pitch, roll, yaw);
    }

}
