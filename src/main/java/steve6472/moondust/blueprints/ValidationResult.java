package steve6472.moondust.blueprints;

/**
 * Created by steve6472
 * Date: 6/9/2025
 * Project: MoonDust <br>
 */
public interface ValidationResult
{
    ValidationResult PASS = pass("generic pass");
    ValidationResult FAIL = fail("unknown fail");

    static ValidationResult fail(String messaage, Object... args)
    {
        return new Fail(String.format(messaage, args));
    }

    static ValidationResult pass(String message)
    {
        return new Pass(message);
    }

    default ValidationResult withNumberFix(Number number)
    {
        if (isPass())
            return new PassFixNumber(getMessage(), number);
        else
            return this;
    }

    default Number fixNumber()
    {
        return null;
    }

    boolean isPass();

    String getMessage();

    final class Pass implements ValidationResult
    {
        private final String message;

        private Pass(String message)
        {
            this.message = message;
        }

        @Override
        public boolean isPass()
        {
            return true;
        }

        @Override
        public String getMessage()
        {
            return message;
        }
    }

    final class PassFixNumber implements ValidationResult
    {
        private final String message;
        private final Number newNumber;

        private PassFixNumber(String message, Number newNumber)
        {
            this.message = message;
            this.newNumber = newNumber;
        }


        @Override
        public Number fixNumber()
        {
            return newNumber;
        }

        @Override
        public boolean isPass()
        {
            return true;
        }

        @Override
        public String getMessage()
        {
            return message;
        }
    }

    final class Fail implements ValidationResult
    {
        private final String message;

        private Fail(String message)
        {
            this.message = message;
        }

        @Override
        public boolean isPass()
        {
            return false;
        }

        @Override
        public String getMessage()
        {
            return message;
        }
    }
}
