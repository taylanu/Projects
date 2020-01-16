import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

/**
 * @author taylan unal (taylanu)
 */
public class Java2dDrawingApplication
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        DrawingApplicationFrame frame = new DrawingApplicationFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,500);
        frame.setVisible(true);
    }
}

