import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class task2lab6 extends JFrame {

    private JTable table;
    private JTextArea outputArea;
    private JButton loadButton, findButton;
    private int[][] matrix;

    public task2lab6() {
        super("Аналіз симетричних рядків (Swing)");

        // --- Інтерфейс ---
        setLayout(new BorderLayout());

        // Верхня панель з кнопками
        JPanel topPanel = new JPanel();
        loadButton = new JButton("Зчитати з файлу");
        findButton = new JButton("Знайти симетричні рядки");
        findButton.setEnabled(false);

        topPanel.add(loadButton);
        topPanel.add(findButton);
        add(topPanel, BorderLayout.NORTH);

        // Таблиця для виводу матриці
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Текстова область для результатів
        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // --- Події ---
        loadButton.addActionListener(e -> loadFromFile());
        findButton.addActionListener(e -> findSymmetricRows());

        // --- Вікно ---
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===================== Зчитування з файлу =====================
    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (Scanner sc = new Scanner(file)) {
                int n = sc.nextInt();
                if (n > 20) {
                    throw new TooLargeMatrixException("Розмір матриці перевищує 20!");
                }

                matrix = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (!sc.hasNextInt()) {
                            throw new NumberFormatException("Некоректний формат числа у файлі!");
                        }
                        matrix[i][j] = sc.nextInt();
                    }
                }

                // Заповнюємо JTable
                DefaultTableModel model = new DefaultTableModel(n, n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        model.setValueAt(matrix[i][j], i, j);
                    }
                }
                table.setModel(model);
                outputArea.setText("Матриця успішно зчитана!\n");
                findButton.setEnabled(true);

            } catch (FileNotFoundException ex) {
                showError("Файл не знайдено!");
            } catch (NumberFormatException ex) {
                showError("Помилка формату даних у файлі!");
            } catch (TooLargeMatrixException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Невідома помилка: " + ex.getMessage());
            }
        }
    }

    // ===================== Пошук симетричних рядків =====================
    private void findSymmetricRows() {
        if (matrix == null) {
            outputArea.setText("Матриця не завантажена!");
            return;
        }

        int[] pattern1 = {1, 2, 3, 3, 2, 1};
        int[] pattern2 = {1, 2, 3, 5, 3, 2, 1};
        boolean found = false;

        StringBuilder result = new StringBuilder("Рядки з симетричною послідовністю:\n");

        for (int i = 0; i < matrix.length; i++) {
            if (isSymmetricSequence(matrix[i], pattern1) || isSymmetricSequence(matrix[i], pattern2)) {
                result.append("Рядок № ").append(i + 1).append("\n");
                found = true;
            }
        }

        if (!found) {
            result.append("Немає рядків з вказаними послідовностями.");
        }

        outputArea.setText(result.toString());
    }

    private boolean isSymmetricSequence(int[] row, int[] pattern) {
        if (row.length < pattern.length) return false;
        for (int i = 0; i <= row.length - pattern.length; i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length; j++) {
                if (row[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }

    // ===================== Вивід помилок =====================
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
        outputArea.setText("❌ " + msg);
    }

    // ===================== Власний виняток =====================
    static class TooLargeMatrixException extends ArithmeticException {
        public TooLargeMatrixException(String message) {
            super(message);
        }
    }

    // ===================== Точка входу =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(task2lab6::new);
    }
}
