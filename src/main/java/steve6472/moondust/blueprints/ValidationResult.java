package steve6472.moondust.blueprints;

/**
 * Created by steve6472
 * Date: 6/9/2025
 * Project: MoonDust <br>
 */
public class ValidationResult
{
    public static final ValidationResult PASS = new ValidationResult(true, "generic pass");
    public static final ValidationResult FAIL = new ValidationResult(false, "unknown fail");

    private final boolean isPass;
    private final String message;

    private ValidationResult(boolean isPass, String message)
    {
        this.isPass = isPass;
        this.message = message;
    }

    public static ValidationResult fail(String messaage, Object... args)
    {
        return new ValidationResult(false, String.format(messaage, args));
    }

    public static ValidationResult pass(String message)
    {
        return new ValidationResult(true, message);
    }

    public boolean isPass()
    {
        return isPass;
    }

    public String getMessage()
    {
        return message;
    }
}
