/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author acv
 * @author taylan unal (tuu2)
 */
public class DrawingApplicationFrame extends JFrame{
    private final JButton undo;
    private final JButton clear;
    private final JButton color1;
    private final JButton color2;
    
    private final JComboBox<String> shapesComboBox;
    
    private final JTextField strokeWidth;
    private final JTextField strokeDashWidth;   
    
    private final JLabel lineWidth;
    private final JLabel dashWidth;
    private final JLabel mouseLoc = new JLabel("(0,0)");
    
    private final JCheckBox filledIn;
    private final JCheckBox gradient;
    private final JCheckBox dashedLine;
    
    private Color color_1 = Color.BLACK;
    private Color color_2 = Color.BLACK;
        
    public DrawingApplicationFrame(){
        super("Java 2D Drawings");
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        undo = new JButton("Undo");
        topPanel.add(undo);
        undo.addActionListener(new clearAndUndo());
        
        clear = new JButton("Clear");
        topPanel.add(clear);
        clear.addActionListener(new clearAndUndo());
        
        String [] drawItems = {"Line","Rectangle","Oval"};
        shapesComboBox = new JComboBox<String>(drawItems);
        shapesComboBox.setSelectedIndex(0);
        topPanel.add(shapesComboBox);
        
        filledIn = new JCheckBox("Filled");
        topPanel.add(filledIn);
        
        gradient = new JCheckBox("Use Gradiant");
        bottomPanel.add(gradient);
        
        color1 = new JButton("Color 1");
        bottomPanel.add(color1);
        colorSelector select = new colorSelector();
        color1.addActionListener(select);
        
        color2 = new JButton("Color 2");
        bottomPanel.add(color2);
        color2.addActionListener(select);
        
        lineWidth = new JLabel("Line Width:");
        bottomPanel.add(lineWidth);
        strokeWidth = new JTextField("10",4);
        bottomPanel.add(strokeWidth);
        
        dashWidth = new JLabel("Dash Width:");
        bottomPanel.add(dashWidth);
        strokeDashWidth = new JTextField("2",4);
        bottomPanel.add(strokeDashWidth);
        
        dashedLine = new JCheckBox("Dashed");
        bottomPanel.add(dashedLine);
        
        //Manages the layout for the GUI 
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel,BorderLayout.NORTH);
        container.add(bottomPanel,BorderLayout.CENTER);
        
        add(container,BorderLayout.NORTH);
        
       //The paint panel for the user 
        drawPanel = new DrawPanel(mouseLoc,this);
        drawPanel.setBackground(Color.white);
        add(drawPanel,BorderLayout.CENTER);
        
        add(mouseLoc,BorderLayout.SOUTH);
    }
    
    public void findMouse(MouseEvent e){
       mouseLoc.setText("(" + e.getX() + "," + e.getY() + ")");
    }
    
    public boolean getFill(){
        return filledIn.isSelected();
    }
    
    public boolean getGradient(){
        return gradient.isSelected();
    }
    
    public boolean isDashed(){
        return dashedLine.isSelected();
    }
    
    public String getComboBox(){
        return shapesComboBox.getSelectedItem().toString();
    }
    
    public float getStrokeWidth(){
        return Float.parseFloat(strokeWidth.getText());
    }
    
    public float[] getDashWidth(){
        float[] temp = new float[]{Float.parseFloat(strokeDashWidth.getText())};
        return temp;
    }
    

    private class colorSelector implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == color1)
                color_1 = JColorChooser.showDialog(DrawingApplicationFrame.this,"Choose a color",color_1);
            else if (e.getSource() == color2)
                color_2 = JColorChooser.showDialog(DrawingApplicationFrame.this,"Choose a color",color_2);
        }
    }

    public Color getColor_1() {
        return color_1;
    }

    public Color getColor_2() {
        return color_2;
    }
    
    private class clearAndUndo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clear)
                drawPanel.removeAll();
            else if (e.getSource()== undo)
                drawPanel.removeLast();
        }
    }
    //INSERT PRIVATE INNER CLASS FOR DRAWPANEL
    // Create a private inner class for the DrawPanel.
private class DrawPanel extends JPanel{
    //private DrawingApplicationFrame drawingFrame; //create inner drawing application instance

    private ArrayList<MyShapes> shapes = new ArrayList<MyShapes>();
    //private MyShapes currShape;
    private Color color1;
    private Color color2;
    private boolean filledShape;
    private String shapeType;
    private GradientPaint paint;
    private float strokeWidth;
    private BasicStroke stroke;
    private final JLabel mouseLoc;

    public DrawPanel(JLabel mouseFind, DrawingApplicationFrame drawingFrame){
        this.setLayout(new BorderLayout());

        this.drawingFrame = drawingFrame;

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);

        mouseLoc = mouseFind;
        this.add(mouseLoc, BorderLayout.SOUTH);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (MyShapes i : shapes){ //loop through and draw each shape in the shapes arraylist
            i.draw(g2d);//add shape to panel
            repaint();//repaint panel
        }
    }

    public ArrayList<MyShapes> getShapes() {
        return shapes;
    }

    public void removeAll() {
        shapes = new ArrayList<MyShapes>();
        repaint();
    }

    public void removeLast() {
        shapes.remove(getShapes().size() - 1);
        repaint();
    }

    private class MouseHandler extends MouseAdapter implements MouseMotionListener{
        public void mousePressed(MouseEvent event){
            shapeType = da.getComboBox();
            filledShape = da.getFill();
            
            if (da.getGradient() == true){
                color1 = da.getColor_1();
                color2 = da.getColor_2();
            }
            else
                color1 = color2 = da.getColor_1();

            paint = new GradientPaint( 0, 0, color1, 50, 50, color2, true );

            strokeWidth = da.getStrokeWidth();
            
            if (da.isDashed() == true)
                stroke = new BasicStroke( strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, da.getDashWidth() , 0 );
            else
                stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            
            if (shapeType.equals("Line"))
                currentShape = new Line(event.getPoint(), event.getPoint(), paint, stroke);
            else if (shapeType.equals("Rectangle"))
                currentShape = new Rect(event.getPoint(), event.getPoint(), paint, stroke, filledShape);
            else if (shapeType.equals("Oval"))
                currentShape = new Oval(event.getPoint(), event.getPoint(), paint, stroke, filledShape);
            
            getShapes().add(currentShape);
            repaint();
        }

        public void mouseReleased(MouseEvent event){
            getShapes().get(getShapes().size() - 1).setEp(event.getPoint());
            repaint();
            currentShape = null;
        }

        @Override
        public void mouseDragged(MouseEvent event){
            da.findMouse(event);
            getShapes().get(getShapes().size() - 1).setEp(event.getPoint());
            repaint();            
        }

        @Override
        public void mouseMoved(MouseEvent event){
            da.findMouse(event);
        }
    }  
}
}