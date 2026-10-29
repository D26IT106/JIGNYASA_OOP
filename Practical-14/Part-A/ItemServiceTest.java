import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemServiceTest
{
    @Test
    public void testAddItemIncreasesCount()
    {
        ItemService service = new ItemService();
        assertEquals(0, service.getItemCount());

        service.addItem(new Item(1, "Laptop"));
        assertEquals(1, service.getItemCount());
    }

    @Test
    public void testAddNullItemThrowsException()
    {
        ItemService service = new ItemService();
        
        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.addItem(null);
        });
    }

    @Test
    public void testGetInvalidIndexThrowsException()
    {
        ItemService service = new ItemService();
        
        assertThrows(IndexOutOfBoundsException.class, () -> 
        {
            service.getItem(5);
        });
    }
}