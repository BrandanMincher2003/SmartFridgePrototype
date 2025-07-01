/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.ffsmartapplication.database.DatabaseConnection;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.models.StockItem;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockDBAccessTest {

    private static Connection connection;
    private StockDBAccess stockDBAccess;

    @BeforeAll
    public static void setUpClass() {
        connection = DatabaseConnection.connect();
        assertNotNull(connection, "Database connection should be established");
    }

    @BeforeEach
    public void setUp() {
        stockDBAccess = new StockDBAccess();
    }

    @Test
    public void testAddStockItem() {
        StockItem item = new StockItem("Donuts", 10, LocalDate.now().plusDays(5));
        boolean result = stockDBAccess.addStockItem(item);
        assertTrue(result, "The item should be successfully added to the database");
    }
    @Test
    public void testItemExists() {
        StockItem item = new StockItem("Milk", 10, LocalDate.now().plusDays(5));
        stockDBAccess.addStockItem(item);
        assertTrue(stockDBAccess.itemExists("Milk"), "Item should exist in the database");
    }
    @Test
    public void testGetAllStockItems() {
        List<StockItem> items = stockDBAccess.getAllStockItems();
        assertFalse(items.isEmpty(), "Stock items should be retrieved");
    }

    @Test
    public void testUpdateStockItem() {
        assertTrue(stockDBAccess.updateStockItem("Milk", "Skimmed Milk", 15, LocalDate.now().plusDays(7)), "Stock item should be updated");
        StockItem item = stockDBAccess.getStockItemByName("Skimmed Milk");
        assertNotNull(item, "Updated item should be retrieved");
    }

    @Test
    public void testGetExpiringItems() {
        List<StockItem> items = stockDBAccess.getExpiringItems(7);
        assertFalse(items.isEmpty(), "Expiring items should be retrieved");
    }

    @Test
    public void testGetLowStockItems() {
        List<StockItem> items = stockDBAccess.getLowStockItems(20);
        assertFalse(items.isEmpty(), "Low stock items should be retrieved");
    }

    @Test
    public void testDeleteStockItem() {
        assertTrue(stockDBAccess.deleteStockItem("Skimmed Milk"), "Item should be deleted from the database");
        assertFalse(stockDBAccess.itemExists("Skimmed Milk"), "Item should no longer exist in the database");
    }
    

    @AfterAll
    public static void tearDownClass() throws Exception {
        if (connection != null) connection.close();
    }
}
 