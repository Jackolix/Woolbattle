// This class was created by Elix on 12.10.22

package codes.Elix.Woolbattle.util;


public class ConsoleAnimator {

    private String lastLine = "";

    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.println("\r" + temp);
        }
        System.out.println("\r" + line);
        lastLine = line;
    }

    private byte anim;

    public void animate(String line) {
        switch (anim) {
            case 1 -> print("Animating.");
            case 2 -> print("Animating..");
            case 3 -> print("Animating...");
            case 4 -> print("Animating....");
            case 5 -> print("Animating.....");
            default -> {
                anim = 0;
                print("Animating");
            }
        }
        anim++;
    }
    /*

    public static void main(String[] args) throws InterruptedException {
        ConsoleHelper consoleHelper = new ConsoleHelper();
        for (int i = 0; i < 20; i++) {
            consoleHelper.animate(i + "");
            //simulate a piece of task
            Thread.sleep(400);
        }
    }

     */







}
