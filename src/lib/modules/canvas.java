package lib.modules;

import lib.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;

public class canvas implements Module {

    private static final NumberValue MINUS_ONE = new NumberValue(-1);

    private static JFrame frame;
    private static CanvasPanel panel;
    private static Graphics2D graphics;
    private static BufferedImage img;

    private static NumberValue lastKey;
    private static boolean[] keyStates = new boolean[256]; // ASCII + виртуальные коды
    private static ArrayValue mouseHover;
    private static NumberValue mouseButton;

    @FunctionalInterface
    private interface IntConsumer4 {
        void accept(int i1, int i2, int i3, int i4);
    }

    private static Function intConsumer4Convert(IntConsumer4 consumer) {
        return args -> {
            if (args.length != 4) throw new RuntimeException("Four args expected");
            int x = (int) args[0].asNumber();
            int y = (int) args[1].asNumber();
            int w = (int) args[2].asNumber();
            int h = (int) args[3].asNumber();
            consumer.accept(x, y, w, h);
            return NumberValue.ZERO;
        };
    }

    @Override
    public void init() {
        // Константы клавиш
        Variables.set("VK_UP", new NumberValue(KeyEvent.VK_UP));
        Variables.set("VK_DOWN", new NumberValue(KeyEvent.VK_DOWN));
        Variables.set("VK_LEFT", new NumberValue(KeyEvent.VK_LEFT));
        Variables.set("VK_RIGHT", new NumberValue(KeyEvent.VK_RIGHT));
        Variables.set("VK_FIRE", new NumberValue(KeyEvent.VK_ENTER));
        Variables.set("VK_ESCAPE", new NumberValue(KeyEvent.VK_ESCAPE));

        // Создание окна
        Functions.set("window", args -> {
            String title = "";
            int width = 640;
            int height = 480;
            switch (args.length) {
                case 1:
                    title = args[0].asString();
                    break;
                case 2:
                    width = (int) args[0].asNumber();
                    height = (int) args[1].asNumber();
                    break;
                case 3:
                    title = args[0].asString();
                    width = (int) args[1].asNumber();
                    height = (int) args[2].asNumber();
                    break;
            }
            panel = new CanvasPanel(width, height);

            frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
            return NumberValue.ZERO;
        });

        // Диалоговое окно с запросом ввода
        Functions.set("prompt", args -> {
            if (args.length != 1) throw new RuntimeException("prompt ожидает 1 аргумент");
            final String v = JOptionPane.showInputDialog(args[0].asString());
            return new StringValue(v == null ? "0" : v);
        });

        // Получение последней нажатой клавиши
        Functions.set("keypressed", args -> lastKey);

        // В методе init(), после регистрации keypressed:
        Functions.set("keyIsDown", args -> {
            if (args.length != 1) {
                throw new RuntimeException("keyIsDown ожидает 1 аргумент: код клавиши");
            }
            int code = (int) args[0].asNumber();
            if (code >= 0 && code < keyStates.length) {
                return new NumberValue(keyStates[code] ? 1 : 0);
            }
            return NumberValue.ZERO;
        });

        // Получение координат мыши
        Functions.set("mousehover", args -> mouseHover);

        // Рисование линии
        Functions.set("line", intConsumer4Convert((x1, y1, x2, y2) -> graphics.drawLine(x1, y1, x2, y2)));

        // Рисование контура овала
        Functions.set("oval", intConsumer4Convert((x, y, w, h) -> graphics.drawOval(x, y, w, h)));

        // Рисование залитого овала
        Functions.set("foval", intConsumer4Convert((x, y, w, h) -> graphics.fillOval(x, y, w, h)));

        // Рисование контура прямоугольника
        Functions.set("rect", intConsumer4Convert((x, y, w, h) -> graphics.drawRect(x, y, w, h)));

        // Рисование залитого прямоугольника
        Functions.set("frect", intConsumer4Convert((x, y, w, h) -> graphics.fillRect(x, y, w, h)));

        // Установка области отсечения (clip)
        Functions.set("clip", intConsumer4Convert((x, y, w, h) -> graphics.setClip(x, y, w, h)));

        // Вывод текста
        Functions.set("drawstring", args -> {
            if (args.length != 3) throw new RuntimeException("drawstring ожидает 3 аргумента: текст, x, y");
            int x = (int) args[1].asNumber();
            int y = (int) args[2].asNumber();
            graphics.drawString(args[0].asString(), x, y);
            return NumberValue.ZERO;
        });

        // Установка цвета
        Functions.set("color", args -> {
            if (args.length == 1) {
                graphics.setColor(new Color((int) args[0].asNumber()));
            } else if (args.length == 3) {
                int r = (int) args[0].asNumber();
                int g = (int) args[1].asNumber();
                int b = (int) args[2].asNumber();
                graphics.setColor(new Color(r, g, b));
            } else {
                throw new RuntimeException("color ожидает 1 (RGB int) или 3 (r, g, b) аргумента");
            }
            return NumberValue.ZERO;
        });

        Functions.set("mousebutton", args -> {
            return mouseButton;
        });

        // Перерисовка окна
        Functions.set("repaint", args -> {
            panel.invalidate();
            panel.repaint();
            return NumberValue.ZERO;
        });

        // Очистка экрана белым цветом
        Functions.set("clear", args -> {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            return NumberValue.ZERO;
        });

        // Установка толщины линии
        Functions.set("stroke", args -> {
            if (args.length != 1) throw new RuntimeException("stroke ожидает 1 аргумент");
            float width = (float) args[0].asNumber();
            graphics.setStroke(new BasicStroke(width));
            return NumberValue.ZERO;
        });

        // Заливка всего окна текущим цветом
        Functions.set("fill", args -> {
            graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            return NumberValue.ZERO;
        });

        // Задержка (sleep)
        Functions.set("sleep", args -> {
            if (args.length != 1) throw new RuntimeException("sleep ожидает 1 аргумент");
            try {
                Thread.sleep((long) args[0].asNumber());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return NumberValue.ZERO;
        });

        // ширина холста
        Functions.set("width", args -> new NumberValue(img != null ? img.getWidth() : 0));

        // высота холста
        Functions.set("height", args -> new NumberValue(img != null ? img.getHeight() : 0));

        // X курсора
        Functions.set("mouseX", args -> new NumberValue(mouseHover.get(0).asNumber()));

        // Y курсора
        Functions.set("mouseY", args -> new NumberValue(mouseHover.get(1).asNumber()));

        // нажата ли кнопка мыши
        Functions.set("mouseDown", args -> new NumberValue(mouseButton.asNumber() > 0 ? 1 : 0));

        // прозрачность рисования (0.0 - 1.0)
        Functions.set("alpha", args -> {
            float a = (float) args[0].asNumber();
            java.awt.Color c = graphics.getColor();
            graphics.setColor(new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(a * 255)));
            return NumberValue.ZERO;
        });

        // дуга (углы в градусах)
        Functions.set("arc", args -> {
            int x=(int)args[0].asNumber(), y=(int)args[1].asNumber(), w=(int)args[2].asNumber(), h=(int)args[3].asNumber();
            int start=(int)args[4].asNumber(), end=(int)args[5].asNumber();
            graphics.drawArc(x, y, w, h, start, end);
            return NumberValue.ZERO;
        });

        // залитая дуга
        Functions.set("fillArc", args -> {
            int x=(int)args[0].asNumber(), y=(int)args[1].asNumber(), w=(int)args[2].asNumber(), h=(int)args[3].asNumber();
            int start=(int)args[4].asNumber(), end=(int)args[5].asNumber();
            graphics.fillArc(x, y, w, h, start, end);
            return NumberValue.ZERO;
        });

        // стиль 0=plain, 1=bold, 2=italic
        Functions.set("font", args -> {
            int fStyle = ((int)args[1].asNumber() == 1) ? java.awt.Font.BOLD : (((int)args[1].asNumber() == 2) ? java.awt.Font.ITALIC : java.awt.Font.PLAIN);
            graphics.setFont(new java.awt.Font(args[0].asString(), fStyle, (int)args[2].asNumber()));
            return NumberValue.ZERO;
        });

        // ширина текста текущим шрифтом
        Functions.set("textWidth", args -> new NumberValue(graphics.getFontMetrics().stringWidth(args[0].asString())));

        // 11. saveImage(path) -> сохранение холста в PNG
        Functions.set("saveImage", args -> {
            try { ImageIO.write(img, "png", new File(args[0].asString())); return NumberValue.ONE; }
            catch (Exception e) { return NumberValue.ZERO; }
        });

        // отрисовка изображения
        Functions.set("drawImage", args -> {
            try {
                java.awt.Image image = ImageIO.read(new File(args[0].asString()));
                if (args.length == 5) graphics.drawImage(image, (int)args[1].asNumber(), (int)args[2].asNumber(), (int)args[3].asNumber(), (int)args[4].asNumber(), null);
                else graphics.drawImage(image, (int)args[1].asNumber(), (int)args[2].asNumber(), null);
                return NumberValue.ONE;
            } catch (Exception e) { return NumberValue.ZERO; }
        });

        // алгоритм заливки через стек
        Functions.set("floodFill", args -> {
            if (img == null) throw new RuntimeException("Сначала вызовите window()");
            int sx = (int)args[0].asNumber(), sy = (int)args[1].asNumber(), r = (int)args[2].asNumber(), g = (int)args[3].asNumber(), b = (int)args[4].asNumber();
            int target = img.getRGB(sx, sy), fill = new java.awt.Color(r, g, b).getRGB();
            if (target == fill) return NumberValue.ZERO;
            int[] stack = new int[img.getWidth() * img.getHeight()]; int top = 0;
            stack[top++] = sx; stack[top++] = sy;
            int w = img.getWidth(), h = img.getHeight();
            while (top > 0) {
                int cy = stack[--top], cx = stack[--top];
                if (cx < 0 || cx >= w || cy < 0 || cy >= h || img.getRGB(cx, cy) != target) continue;
                img.setRGB(cx, cy, fill);
                stack[top++] = cx+1; stack[top++] = cy;
                stack[top++] = cx-1; stack[top++] = cy;
                stack[top++] = cx; stack[top++] = cy+1;
                stack[top++] = cx; stack[top++] = cy-1;
            }
            return NumberValue.ZERO;
        });

        // сменить заголовок окна
        Functions.set("windowTitle", args -> {
            if (frame != null) {
                frame.setTitle(args[0].asString());
            }
            return NumberValue.ZERO;
        });

        // закрыть окно
        Functions.set("closeWindow", args -> { if (frame != null) frame.dispose(); return NumberValue.ZERO; });


        // Инициализация состояния
        lastKey = MINUS_ONE;
        mouseHover = new ArrayValue(new Value[]{NumberValue.ZERO, NumberValue.ZERO});
        mouseButton = NumberValue.ZERO;
    }

    // Внутренний класс панели для рисования
    private static class CanvasPanel extends JPanel {

        public CanvasPanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFocusable(true);
            requestFocus();

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int code = e.getKeyCode();
                    lastKey = new NumberValue(code);
                    if (code >= 0 && code < keyStates.length) {
                        keyStates[code] = true;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code >= 0 && code < keyStates.length) {
                        keyStates[code] = false;
                    }
                    // lastKey сбрасываем только если отпущенная клавиша была "последней"
                    if (code == lastKey.asNumber()) {
                        lastKey = MINUS_ONE;
                    }
                }
            });

            // Слушатель нажатий/отпусканий кнопок мыши
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseButton = new NumberValue(e.getButton());
                    mouseHover.set(0, new NumberValue(e.getX()));
                    mouseHover.set(1, new NumberValue(e.getY()));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseButton = NumberValue.ZERO;
                    mouseHover.set(0, new NumberValue(e.getX()));
                    mouseHover.set(1, new NumberValue(e.getY()));
                }
            });

            // Слушатель движений мыши
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseHover.set(0, new NumberValue(e.getX()));
                    mouseHover.set(1, new NumberValue(e.getY()));
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    mouseHover.set(0, new NumberValue(e.getX()));
                    mouseHover.set(1, new NumberValue(e.getY()));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }
}