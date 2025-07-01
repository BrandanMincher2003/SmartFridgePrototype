import com.mycompany.ffsmartapplication.database.OrdersDBAccess;
import com.mycompany.ffsmartapplication.models.Order;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDBAccessTest {

    private OrdersDBAccess orderDBAccess;

    @BeforeEach
    public void setUp() {
        orderDBAccess = new OrdersDBAccess();
    }

    @Test
    public void testAddOrder() {
        Order order = new Order(0, "Milk", 10, "SupplierX");
        orderDBAccess.addOrder(order);
        assertTrue(order.getOrderNumber() > 0, "Order should have a generated order number");
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(0, "Bread", 5, "SupplierY");
        orderDBAccess.addOrder(order);

        Order fetchedOrder = orderDBAccess.getOrderById(order.getOrderNumber());
        assertNotNull(fetchedOrder, "Order should exist in the database");
        assertEquals("Bread", fetchedOrder.getItemName(), "Order item name should match");
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = orderDBAccess.getAllOrders();
        assertFalse(orders.isEmpty(), "Orders should be retrieved from the database");
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order(0, "Juice", 3, "SupplierZ");
        orderDBAccess.addOrder(order);

        orderDBAccess.deleteOrder(order.getOrderNumber());

        Order deletedOrder = orderDBAccess.getOrderById(order.getOrderNumber());
        assertNull(deletedOrder, "Order should be deleted from the database");
    }
}
