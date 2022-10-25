package com.example.test;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Object {
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
    public Object(double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw, Canvas canvas) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lengthZ = lengthZ;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.canvas = canvas;
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
        if (!(coords1[2] > Constants.renderDistance || coords4[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords4[0], coords4[1]);
        }
        if (!(coords2[2] > Constants.renderDistance || coords1[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords4[2] > Constants.renderDistance)) {
            gc.strokeLine(coords4[0], coords4[1], coords3[0], coords3[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords2[2] > Constants.renderDistance)) {
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
        if (!(coords1[2] > Constants.renderDistance || coords4[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords4[0], coords4[1]);
        }
        if (!(coords2[2] > Constants.renderDistance || coords1[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords4[2] > Constants.renderDistance)) {
            gc.strokeLine(coords4[0], coords4[1], coords3[0], coords3[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords2[2] > Constants.renderDistance)) {
            gc.strokeLine(coords3[0], coords3[1], coords2[0], coords2[1]);
        }
    }
    public double[] cameraToScreen(double[] camera) {
        double[] screen = {camera[0] / -(camera[2]/1000), camera[1] / -(camera[2]/1000), camera[2]};
        screen[0] = (screen[0] + (Constants.width / 2d)) / Constants.width;
        screen[1] = (screen[1] + (Constants.height / 2d)) / Constants.height;
        screen[0] *= Constants.width;
        screen[1] *= Constants.height;
        return screen;
    }
    public double[] rotate(double pitch, double roll, double yaw, double[] coords) {
        double cosa = Math.cos(yaw);
        double sina = Math.sin(yaw);

        double cosb = Math.cos(pitch);
        double sinb = Math.sin(pitch);

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

    public void drawSlantedTriangleFront(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        double[] coords1 = rotate(pitch,roll,yaw, new double[]{xpos, ypos, zpos});
        double[] coords2 = rotate(pitch, roll, yaw, new double[]{xpos - (lengthX / 2), ypos - lengthY, zpos - (lengthZ / 2)});
        double[] coords3 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos, zpos});
        coords1 = cameraToScreen(coords1);
        coords2 = cameraToScreen(coords2);
        coords3 = cameraToScreen(coords3);
        if (!(coords1[2] > Constants.renderDistance || coords2[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
        }
        if (!(coords2[2] > Constants.renderDistance || coords3[2] > Constants.renderDistance)) {
            gc.strokeLine(coords2[0], coords2[1], coords3[0], coords3[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords1[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords3[0], coords3[1]);
        }
    }

    public void drawSlantedTriangleBack(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw) {
        double[] coords1 = rotate(pitch,roll,yaw, new double[]{xpos, ypos, zpos});
        double[] coords2 = rotate(pitch, roll, yaw, new double[]{xpos - (lengthX / 2), ypos - lengthY, zpos + (lengthZ / 2)});
        double[] coords3 = rotate(pitch, roll, yaw, new double[]{xpos - lengthX, ypos, zpos});
        coords1 = cameraToScreen(coords1);
        coords2 = cameraToScreen(coords2);
        coords3 = cameraToScreen(coords3);
        if (!(coords1[2] > Constants.renderDistance || coords2[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords2[0], coords2[1]);
        }
        if (!(coords2[2] > Constants.renderDistance || coords3[2] > Constants.renderDistance)) {
            gc.strokeLine(coords2[0], coords2[1], coords3[0], coords3[1]);
        }
        if (!(coords3[2] > Constants.renderDistance || coords1[2] > Constants.renderDistance)) {
            gc.strokeLine(coords1[0], coords1[1], coords3[0], coords3[1]);
        }
    }

    public abstract void draw(GraphicsContext gc, double xpos, double ypos, double zpos, double lengthX, double lengthY, double lengthZ, double pitch, double roll, double yaw);

    public abstract void draw(GraphicsContext gc);
}
