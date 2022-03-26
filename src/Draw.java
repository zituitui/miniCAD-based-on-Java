import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Draw extends JFrame {
    private final int HEIGHT = 800;
    private final int WIDTH = 1000;

    private final int COLORWIDTH = 30;
    private DrawListener myMouse;

    Draw() {
        super("3190101086 miniCAD");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setLayout(new BorderLayout());

        myMouse = new DrawListener();
        this.addMouseListener(myMouse);
        this.addMouseMotionListener(myMouse);
        this.addKeyListener(myMouse);

        JPanel board = new JPanel();
        JPanel toolBar = new JPanel();
        this.add(board, BorderLayout.CENTER);
        this.add(toolBar, BorderLayout.EAST);
        board.setBackground(new Color(255, 255, 225));

        toolBar.setPreferredSize(new Dimension(205, HEIGHT));
        toolBar.setBackground(Color.LIGHT_GRAY);

        JPanel ColorBar = new JPanel();
        JPanel ButtonBar = new JPanel();
        ColorBar.setLayout(new GridLayout(3, 4));
        ButtonBar.setLayout(new GridLayout(6, 2));

        String[] shapeName = { "Open", "Save", "Choose", "Line", "Rectangle", "Oval", "Delete", "Text", "Undo", "Redo",
                "Size++", "Size--" };
        for (int i = 0; i < shapeName.length; i++) {
            JButton nowButton = new JButton(shapeName[i]);
            nowButton.setPreferredSize(new Dimension(100, 60));
            nowButton.addActionListener(myMouse);
            nowButton.setToolTipText(shapeName[i]);
            ButtonBar.add(nowButton);
        }

        Color[] colorArray = { Color.red, Color.pink, Color.orange, Color.yellow, Color.green, Color.blue, Color.cyan,
                Color.black, Color.gray, Color.white, Color.magenta, Color.DARK_GRAY };
        for (int i = 0; i < colorArray.length; i++) {
            JButton nowButton = new JButton();
            nowButton.setBackground(colorArray[i]);
            nowButton.setPreferredSize(new Dimension(30, 30));
            nowButton.addActionListener(myMouse);
            nowButton.setToolTipText(colorArray[i].toString());
            ColorBar.add(nowButton);
        }

        // toolBar.add(ColorBar, BorderLayout.SOUTH);
        // toolBar.add(ButtonBar, BorderLayout.CENTER);
        toolBar.add(ColorBar);
        toolBar.add(ButtonBar);
        JTextArea TextBar = new JTextArea(
                "help: \n  1.Use choose to select the target icon \nbefore dragging or changing the size \n  2.Save does not require \nsuffix and address\n  3.Open does not require suffix \nand address\n  4.Choose and then modify the \ntoning operation\n  5.The contents of delete cannot \nbe redo\n ");
        TextBar.setPreferredSize(new Dimension(180, 250));
        // TextBar.setSize(30, 40);
        toolBar.add(TextBar);

        this.setVisible(true);

        myMouse.myGraph = (Graphics2D) this.getGraphics(); // Get the current Graphics
        myMouse.myFrame = this; // It'll be convenient for repaint()
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.getContentPane().setBackground(Color.white);
        myMouse.paint();
    }

    public static void main(String argv[]) {
        new Draw();
    }
}

class DrawListener implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
    private Stack<Shape> DoList = new Stack<>();
    private Stack<Shape> TodoList = new Stack<>();
    //
    // typingArea=new JTextField(20);typingArea.addKeyListener(this);

    private enum Operation {
        Texting, Saving, Opening, Screening, OTHER
    };

    private JFrame fileFrame;
    private JTextField content;
    private JButton OKbutton;
    private Operation operation;

    JFrame myFrame;
    Graphics2D myGraph;
    Color nowColor = Color.black;
    String nowSelect = "Choose";
    Shape nowShape = null;
    Boolean Dragging = false;

    private int curX, curY, stX, stY;

    DrawListener() {
        OKbutton = new JButton("OK");
        OKbutton.addActionListener(this);
        curX = -1;
        curY = -1;
        operation = Operation.OTHER;
    }

    public void keyReleased(KeyEvent e) {
        if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_ENTER)) {
            System.out.println(" crtl+enter");
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println(" ");
        } else {
            System.out.println(e.getKeyText(e.getKeyCode()) + ",,,," + e.getKeyChar() + ",,,,," + e.getKeyCode());

        }
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("$$");
    }

    public void keyTyped(KeyEvent e) {
        System.out.println("###");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        curX = stX = e.getX();
        curY = stY = e.getY();
        if (nowSelect.equals("Choose")) {
            for (Shape cur : DoList)
                if (cur.in(curX, curY))
                    nowShape = cur;
        } else if (nowSelect.equals("Text")) {
            buildFrame("Texting", "Please enter a string. ");
            operation = Operation.Texting;
        }
    }

    void buildFrame(String frameName, String labelName) {
        JLabel label = new JLabel(labelName);
        content = new JTextField(32);
        fileFrame = new JFrame(frameName);
        Container contentPane = fileFrame.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(label);
        contentPane.add(content);
        contentPane.add(OKbutton);
        fileFrame.setSize(500, 100);
        fileFrame.setLocationRelativeTo(null);
        fileFrame.setVisible(true);
    }

    public void mouseReleased(MouseEvent e) {
        Dragging = false;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int edX = e.getX(), edY = e.getY();
        int x1 = Math.min(stX, edX), x2 = Math.max(stX, edX);
        int y1 = Math.min(stY, edY), y2 = Math.max(stY, edY);

        if (nowSelect.equals("Choose")) {
            if (nowShape != null) {
                nowShape.move(edX - curX, edY - curY);
                DoList.remove(nowShape);
                DoList.push(nowShape);
                myFrame.paint(myGraph);
            }
        } else if (nowSelect.equals("Line") || nowSelect.equals("Rectangle") || nowSelect.equals("Oval")) {
            if (Dragging) {
                if (!DoList.isEmpty())
                    DoList.pop();
            } else {
                Dragging = true;
            }
            if (nowSelect.equals("Line"))
                nowShape = DoList.push(new Line("Line", stX, stY, edX, edY, nowColor));
            else if (nowSelect.equals("Rectangle"))
                nowShape = DoList.push(new Rectangle("Rectangle", x1, y1, x2 - x1, y2 - y1, nowColor));
            else if (nowSelect.equals("Oval")) {
                nowShape = DoList.push(new Oval("Oval", x1, y1, x2 - x1, y2 - y1, nowColor));
            }
            myFrame.paint(myGraph);
        }
        curX = edX;
        curY = edY;
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("")) {
            JButton nowButton = (JButton) e.getSource();
            nowColor = nowButton.getBackground();
            if (nowShape != null) {
                nowShape.setColor(nowColor);
                myFrame.paint(myGraph);
            }
        } else {
            nowSelect = e.getActionCommand();
            if (nowSelect.equals("Choose")) {
                nowShape = null;
            } else if (nowSelect.equals("Delete")) {
                if (nowShape != null) {
                    // TodoList.add(nowShape);
                    DoList.remove(nowShape);
                    myFrame.paint(myGraph);
                }
                nowSelect = "Choose";
            } else if (nowSelect.equals("Save")) {
                operation = Operation.Saving;
                buildFrame("Save as .mycad file", "Please enter your file name");
                nowSelect = "Choose";
            } else if (nowSelect.equals("Open")) {
                operation = Operation.Opening;
                buildFrame("Opening .mycad file", "Please enter your file name");
                nowSelect = "Choose";
            } else if (nowSelect.equals("OK")) { // OK buttons in saving or opening
                fileFrame.setVisible(false);
                if (!content.getText().isEmpty()) {
                    System.out.println(content.getText());
                    switch (operation) {
                    case Texting:
                        nowShape = DoList.push(new Text("Text", curX, curY, content.getText(), nowColor));
                        myFrame.paint(myGraph);
                        break;
                    case Saving:
                        ObjectOutputStream oos;
                        try {
                            // save to D, no need for user to input .mycad
                            oos = new ObjectOutputStream(new FileOutputStream("D:\\" + content.getText() + ".mycad"));
                            oos.writeObject(DoList);
                        } catch (IOException ie) {
                            System.out.println(ie.getMessage());
                        }
                        break;
                    case Opening:
                        ObjectInputStream ios;
                        try {
                            ios = new ObjectInputStream(new FileInputStream("D:\\" + content.getText() + ".mycad"));
                            Object obj = ios.readObject();
                            DoList = (Stack<Shape>) obj;
                            myFrame.paint(myGraph);
                        } catch (Exception ie) {
                            System.out.println("###" + ie.getMessage());
                        }
                        break;

                    }
                }
                nowSelect = "Choose";
                operation = Operation.OTHER;
            } else if (nowSelect.equals("Undo") && !DoList.isEmpty()) {
                TodoList.push(DoList.pop());
                myFrame.paint(myGraph);
                nowSelect = "Choose";
            } else if (nowSelect.equals("Redo") && !TodoList.isEmpty()) {
                DoList.push(TodoList.pop());
                myFrame.paint(myGraph);
                nowSelect = "Choose";
            } else if (nowSelect.equals("Size++")) {
                if (nowShape != null)
                    nowShape.changeSize(0.5f);
                myFrame.paint(myGraph);
                nowSelect = "Choose";
            } else if (nowSelect.equals("Size--")) {
                if (nowShape != null)
                    nowShape.changeSize(-0.5f);
                myFrame.paint(myGraph);
                nowSelect = "Choose";
            }
        }
    }

    public void paint() {
        for (Shape shape : DoList)
            shape.draw(myGraph);
    }
}
