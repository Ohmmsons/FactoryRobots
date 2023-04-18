import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyChecker implements KeyListener {
    private boolean ePressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public boolean isEPressed() {
        return ePressed;
    }
}