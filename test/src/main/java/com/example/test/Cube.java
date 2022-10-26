package com.example.test;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Cube extends Object{
    private Canvas canvas;

    public Cube(double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw, Canvas canvas) {
        super(xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw, canvas);
    }
    public Cube(Canvas canvas) {
        super(0, 0, 0, 0, 0, 0, 0, 0, 0, canvas);
    }

    @Override
    public void draw(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        setXpos(xpos);
        setYpos(ypos);
        setZpos(zpos);
        setLengthX(lengthX);
        setLengthY(lengthY);
        setLengthZ(lengthZ);
        setPitch(pitch);
        setRoll(roll);
        setYaw(yaw);
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawFlatSquare(gc, xpos, ypos - lengthY, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos, lengthX, lengthY, lengthZ, pitch, roll, yaw);
        drawVerticalSquare(gc, xpos, ypos, zpos - lengthZ, lengthX, lengthY, lengthZ, pitch, roll, yaw);
    }

    @Override
    public void draw(GraphicsContext gc, double xoff, double yoff, double zoff, double xrot, double yrot, double zrot) {
        gc.clearRect(0, 0, Constants.width, Constants.height);
        drawFlatSquare(gc, getXpos() + xoff, getYpos() + yoff, getZpos() + zoff, getLengthX(), getLengthY(), getLengthZ(), getPitch() + xrot, getRoll() + yrot, getYaw() + zrot);
        drawFlatSquare(gc, getXpos() + xoff, getYpos() + yoff - getLengthY(), getZpos() + zoff, getLengthX(), getLengthY(), getLengthZ(), getPitch() + xrot, getRoll() + yrot, getYaw() + zrot);
        drawVerticalSquare(gc, getXpos() + xoff, getYpos() + yoff, getZpos() + zoff, getLengthX(), getLengthY(), getLengthZ(), getPitch() + xrot, getRoll() + yrot, getYaw() + zrot);
        drawVerticalSquare(gc, getXpos() + xoff, getYpos() + yoff, getZpos() + zoff - getLengthZ(), getLengthX(), getLengthY(), getLengthZ(), getPitch() + xrot, getRoll() + yrot, getYaw() + zrot);
    }

}
