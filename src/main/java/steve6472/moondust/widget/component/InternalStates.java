package steve6472.moondust.widget.component;

import java.lang.reflect.Field;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 * This one does not have blueprint, it is created whenever a widget is created!
 */
public class InternalStates
{
    public boolean hovered;
    public boolean focused;
    public boolean pressStart;




    // Yes, chatGPT wrote this, I was too lazy and quite tired lul
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(" { ");
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            try
            {
                sb.append(field.getName()).append(": ").append(field.get(this)).append(", ");
            } catch (IllegalAccessException e)
            {
                sb.append(field.getName()).append(": [access denied], ");
            }
        }
        if (fields.length > 0)
        {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append(" }");
        return sb.toString();
    }
}
