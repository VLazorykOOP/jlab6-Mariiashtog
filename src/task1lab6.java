import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class task1lab6 extends JFrame {

    private int x = 50;              // Початкова позиція точки по осі X
    private int y = 100;             // Позиція по осі Y
    private int speed = 5;           // Початкова швидкість руху точки
    private Timer timer;

    public task1lab6() {
        super("Рух точки");

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton increaseButton = new JButton("Збільшити швидкість");
        JButton decreaseButton = new JButton("Зменшити швидкість");

        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);

        // Додаємо панель кнопок внизу фрейму
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Панель для малювання точки
        DrawPanel drawPanel = new DrawPanel();
        this.add(drawPanel, BorderLayout.CENTER);

        // Таймер для анімації точки
        timer = new Timer(50, e -> {
            x += speed;
            if (x > drawPanel.getWidth()) {
                x = 0;
            }
            drawPanel.repaint();
        });
        timer.start();

        // Обробка кнопок
        increaseButton.addActionListener(e -> speed += 2);
        decreaseButton.addActionListener(e -> speed = Math.max(0, speed - 2)); // не даємо швидкості стати від’ємною

        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Панель для малювання точки
    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillOval(x, y, 20, 20); // малюємо точку (коло)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(task1lab6::new);
    }
}
