import java.util.ArrayList;
import java.util.List;

public class ItemService
{
    private List<Item> items = new ArrayList<>();

    // Adds an item to the list
    public void addItem(Item item)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item cannot be null");
        }
        items.add(item);
    }

    // Returns total count of items
    public int getItemCount()
    {
        return items.size();
    }

    // Fetches item by index
    public Item getItem(int index)
    {
        if(index < 0 || index >= items.size())
        {
            throw new IndexOutOfBoundsException("Invalid index provided");
        }
        return items.get(index);
    }
}