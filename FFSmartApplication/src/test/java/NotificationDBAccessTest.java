import com.mycompany.ffsmartapplication.database.DatabaseConnection;
import com.mycompany.ffsmartapplication.database.NotificationDBAccess;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationDBAccessTest {

    private static Connection connection;

    @BeforeAll
    public static void setUpClass() {
        connection = DatabaseConnection.connect();
        assertNotNull(connection, "Database connection should be established");
    }

    @BeforeEach
    public void setupDatabase() {
        try (PreparedStatement clearNotifications = connection.prepareStatement("DELETE FROM notifications");
             PreparedStatement clearConsumables = connection.prepareStatement("DELETE FROM consumables");
             PreparedStatement insertConsumable = connection.prepareStatement(
                     "INSERT INTO consumables (consumable_id, name, quantity, expiry_date) VALUES (?, ?, ?, ?)")) {

            // Clear old data
            clearNotifications.executeUpdate();
            clearConsumables.executeUpdate();

            // Insert test consumable (required for foreign key)
            insertConsumable.setInt(1, 1);
            insertConsumable.setString(2, "Apple");
            insertConsumable.setInt(3, 10);
            insertConsumable.setString(4, "2025-02-14"); // Expiry date
            insertConsumable.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogNotificationValidData() {
        int testItemId = 1; // Must match the inserted consumable
        String validReason = "expiry_warning"; 
        String validMessage = "Item expiring soon: Apple (ID: 1, Qty: 10, Expiry: 2025-02-14)";

        NotificationDBAccess.logNotification(testItemId, validReason, validMessage);

        boolean exists = checkNotificationExists(testItemId, validReason, validMessage);
        assertTrue(exists, "Notification should be stored in the database");
    }

    @Test
    public void testPreventDuplicateNotificationWithinHour() {
        int testItemId = 1;
        String validReason = "low_stock_warning"; 
        String validMessage = "Low stock alert: Apple (ID: 1, Qty: 10)";

        NotificationDBAccess.logNotification(testItemId, validReason, validMessage);
        NotificationDBAccess.logNotification(testItemId, validReason, validMessage); // Should not insert duplicate

        int count = getNotificationCount(testItemId, validReason);
        assertEquals(1, count, "Only one notification should be stored within an hour");
    }

    @Test
    public void testGetRecentNotifications() {
        NotificationDBAccess.logNotification(1, "low_stock_warning", "Low stock alert: Apple");
        NotificationDBAccess.logNotification(1, "expiry_warning", "Item expiring soon: Apple");

        List<String> notifications = NotificationDBAccess.getRecentNotifications();
        assertEquals(2, notifications.size(), "Should retrieve the last 2 notifications");
    }

    private boolean checkNotificationExists(int itemId, String reason, String message) {
        String query = "SELECT COUNT(*) FROM notifications WHERE consumable_id = ? AND reason = ? AND message = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setString(2, reason);
            stmt.setString(3, message);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getNotificationCount(int itemId, String reason) {
        String query = "SELECT COUNT(*) FROM notifications WHERE consumable_id = ? AND reason = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.setString(2, reason);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        if (connection != null) connection.close();
    }
}
