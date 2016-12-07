package com.company;

import Media.*;
import BasicIO.*;
//import sun.jvm.hotspot.code.PCDesc;

import java.awt.*;
import static BasicIO.Formats.*;
import static java.lang.Math.*;
import static java.awt.Color.*;

public class Main {

    public static void main(String[] args) {
        //PartA_1 a1 = new PartA_1();
        //PartA_2 a2 = new PartA_2();
        //PartA_3 a3 = new PartA_3();
        //PartB_1 b1 = new PartB_1();
        //PartB_2 b2 = new PartB_2();
        PartB_3 b3 = new PartB_3();
    }

}

class PartA_1 {

    private PictureDisplayer display;

    public PartA_1() {
        Picture pic1;
        Picture pic2;

        pic1 = new Picture();
        display = new PictureDisplayer(pic1.getWidth(),pic1.getHeight());
        display.placePicture(pic1);
        display.waitForUser();

        pic2 = new Picture();
        display.placePicture(pic2);
        display.waitForUser();

        makeBands(pic1,pic2);
        display.close();

    }

    private void makeBands(Picture pic1,Picture pic2) {
        int width = pic1.getWidth();
        int height = pic1.getHeight();
        int temp = pic1.getHeight()/10;
        int count = 0;

        for(int i=0;i<height;i++) {
            count++;
            for (int j = 0; j < width; j++) {
                pic2.getPixel(j, i).setColor(pic1.getPixel(j, i).getColor());
            }
            if(count == temp) {
                i+=temp;
                count = 0;
            }

        }

    }

}

class PartA_2 {

    private PictureDisplayer display;

    public PartA_2 () {

        Picture pic;

        pic = new Picture();
        display = new PictureDisplayer(pic.getWidth(),pic.getHeight());

        display.placePicture(pic);
        display.waitForUser();
        Picture crop = crop(pic,125,170,100,30);
        Picture flip = flip(crop);
        paste(pic,flip,125,170);
        display.close();


    }

    private Picture crop (Picture aPic, int x, int y,int width, int height) {
        Picture pic = new Picture(width,height);

        for(int i=y;i<height+y;i++)
            for(int j=x;j<width+x;j++) {
                pic.next().setColor(aPic.getPixel(j,i).getColor());
            }

        return pic;
    }

    private Picture flip (Picture aPic) {
        Picture pic = new Picture(aPic.getWidth(), aPic.getHeight());

        for(int i=0;i<aPic.getHeight();i++)
            for(int j=0;j<aPic.getWidth();j++)
                pic.getPixel(j,i).setColor(aPic.getPixel(j,aPic.getHeight()-1-i).getColor());

        return pic;
    }

    private void paste (Picture orig, Picture paste, int x, int y) {
        int height = paste.getHeight();
        int width = paste.getWidth();

        for(int i=y;i<y+height;i++)
            for(int j=x;j<x+width;j++)
                orig.getPixel(j,i).setColor(paste.next().getColor());
    }
}

class PartA_3 {

    private PictureDisplayer display;

    public PartA_3() {
        Picture pic1;
        Picture pic2;

        pic1 = new Picture();
        display = new PictureDisplayer(pic1.getWidth(),pic1.getHeight());
        display.placePicture(pic1);
        display.waitForUser();

        pic2 = new Picture();
        display.placePicture(pic2);
        display.waitForUser();

        pic2 = applyMask(pic1,pic2);
        display.close();
    }

    private Picture applyMask(Picture aPic, Picture mask) {
        Pixel pix1;
        Pixel pix2;

        while(mask.hasNext()) {
            pix1 = aPic.next();
            pix2 = mask.next();
            if(pix2.getDistance(BLACK)<=50) {
               pix2.setColor(pix1.getColor());
            }
        }
        return mask;
    }
}

class PartB_1 {
    private ASCIIDataFile file;
    private ReportPrinter printer;

    PartB_1() {
        file = new ASCIIDataFile();
        printer = new ReportPrinter();

        setUpReport("Daily Weather");

        for( ; ; ) {
            processRollData();
            if(file.isEOF()) break;

        }

        file.close();
        printer.close();

    }

    private void processRollData() {
        String date;
        String day;
        int max;
        int min;
        int total;
        double average;
        int read;

        date = file.readString();
        day = file.readString();

        read = file.readInt();
        max = read;
        min = read;
        total=0;
        total+=read;

        for(int x=0;x<23;x++) {
            read = file.readInt();

            max = max(max,read);
            min = min(min,read);
            total+=read;
        }
        average = (double)total/24;
        if(file.isEOF()) return;
        writeDetail(date,day,max,min,average);

    }

    private void setUpReport(String title) {
        printer.setTitle(title);
        printer.newLine();
        printer.addField("date","Date",10);
        printer.addField("day","Day",10);
        printer.addField("high","High",10);
        printer.addField("low","Low",10);
        printer.addField("average","Average",getDecimalInstance(1),10);

    }

    private void writeDetail(String date,String day,int max,int min,double average) {
        printer.writeString("date",date);
        printer.writeString("day",day);
        printer.writeInt("high",max);
        printer.writeInt("low",min);
        printer.writeDouble("average",average);
    }
}

class PartB_2 {
    private BasicForm etch;
    private Turtle yertle;

    public PartB_2() {
        int control;
        int length;
        int x=0;
        int y=0;

        setUpForm();

        for( ; ; ) {
            control = etch.accept();
            length = etch.readInt("length");
            if(control == 4)
                break;
            switch(control) {
                case 0:
                    x-=length;
                    break;
                case 1:
                    y+=length;
                    break;
                case 2:
                    y-=length;
                    break;
                case 3:
                    x+=length;
                    break;
            }
            yertle.moveTo(x,y);
        }
        etch.close();

    }

    private void setUpForm() {
        etch = new BasicForm("Left","Up","Down","Right","Quit");
        etch.setTitle("Etch-a-Sketch");
        etch.addCanvas("display",300,300,5,10);
        etch.addTextField("length","Line Length",10,4,350);
        yertle = new Turtle();
        etch.placeTurtle("display",yertle);
        yertle.penDown();

    }
}

class PartB_3 {
    BasicForm display;

    public PartB_3() {
        int balance;
        int wager;
        int control;

        balance = 200;
        setUpForm();

        for( ; ; ) {
            wager = 2;
            display.writeInt("wager",wager);
            display.writeInt("balance",balance);
            control = display.accept();
            wager = display.readInt("wager");
            if(control == 1)
                break;

            if(roll())
                balance+=wager;
            else
                balance-=wager;
        }
        display.close();

    }

    private boolean roll() {
        int die1;
        int die2;
        int total;
        boolean result;

        die1 = (int)(random()*6+1);
        die2 = (int)(random()*6+1);
        total = die1+die2;
        //System.out.println(total);

        display.writeInt("one",die1);
        display.writeInt("two",die2);
        result = total>=9;

        if(result)
            display.writeString("outcome","WIN");
        else
            display.writeString("outcome","LOSE");
        return result;
    }


    private void setUpForm() {
        display = new BasicForm("Roll","Cash Out");
        display.setTitle("Nine Or Above");
        display.addTextField("one","Die One",5,4,8);
        display.addTextField("two","Die Two",5,150,8);
        display.addTextField("wager","Wager",getCurrencyInstance(),5,4,38);
        display.addTextField("outcome","Outcome",5,4,68);
        display.addTextField("balance","Balance",getCurrencyInstance(),10,4,98);
        display.setEditable("one",false);
        display.setEditable("two",false);
        display.setEditable("outcome",false);
        display.setEditable("balance",false);

    }
}
