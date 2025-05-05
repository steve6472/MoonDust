package steve6472.moondust.widget.component.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.registry.Serializable;
import steve6472.core.registry.StringValue;

/**
 * Created by steve6472
 * Date: 1/7/2025
 * Project: MoonDust <br>
 */
public abstract class OnDataChanged<T> implements UIEvent, Serializable<T>
{
    protected final Key changedKey;
    public final boolean removed;

    private OnDataChanged(Key changedKey, boolean removed)
    {
        this.changedKey = changedKey;
        this.removed = removed;
    }

    public abstract Type type();

    public static class Num extends OnDataChanged<Num>
    {
        private final double previousValue;
        private final double newValue;

        public static final Codec<Num> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Key.CODEC.fieldOf("key").forGetter(o -> o.changedKey),
            Codec.DOUBLE.fieldOf("previous").forGetter(o -> o.previousValue),
            Codec.DOUBLE.fieldOf("new").forGetter(o -> o.newValue),
            Type.CODEC.fieldOf("type").forGetter(Num::type)
        ).apply(instance, Num::new));

        public Num(Key changedKey, double previousValue, double newValue, Type ignored)
        {
            super(changedKey, false);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        public Num(Key changedKey, double previousValue, double newValue, boolean removed)
        {
            super(changedKey, removed);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        @Override
        public Codec<Num> codec()
        {
            return CODEC;
        }

        @Override
        public Type type()
        {
            return Type.NUM;
        }
    }

    public static class Int extends OnDataChanged<Int>
    {
        private final int previousValue;
        private final int newValue;

        public static final Codec<Int> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Key.CODEC.fieldOf("key").forGetter(o -> o.changedKey),
            Codec.INT.fieldOf("previous").forGetter(o -> o.previousValue),
            Codec.INT.fieldOf("new").forGetter(o -> o.newValue),
            Type.CODEC.fieldOf("type").forGetter(Int::type)
        ).apply(instance, Int::new));

        public Int(Key changedKey, int previousValue, int newValue, Type ignored)
        {
            super(changedKey, false);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        public Int(Key changedKey, int previousValue, int newValue, boolean removed)
        {
            super(changedKey, removed);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        @Override
        public Codec<Int> codec()
        {
            return CODEC;
        }

        @Override
        public Type type()
        {
            return Type.INT;
        }
    }

    public static class String extends OnDataChanged<String>
    {
        private final java.lang.String previousValue;
        private final java.lang.String newValue;

        public static final Codec<String> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Key.CODEC.fieldOf("key").forGetter(o -> o.changedKey),
            Codec.STRING.fieldOf("previous").forGetter(o -> o.previousValue),
            Codec.STRING.fieldOf("new").forGetter(o -> o.newValue),
            Type.CODEC.fieldOf("type").forGetter(String::type)
        ).apply(instance, String::new));

        public String(Key changedKey, java.lang.String previousValue, java.lang.String newValue, Type ignored)
        {
            super(changedKey, false);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        public String(Key changedKey, java.lang.String previousValue, java.lang.String newValue, boolean removed)
        {
            super(changedKey, removed);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        @Override
        public Codec<String> codec()
        {
            return CODEC;
        }

        @Override
        public Type type()
        {
            return Type.STRING;
        }
    }

    public static class Flag extends OnDataChanged<Flag>
    {
        private final boolean previousValue;
        private final boolean newValue;

        public static final Codec<Flag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Key.CODEC.fieldOf("key").forGetter(o -> o.changedKey),
            Codec.BOOL.fieldOf("previous").forGetter(o -> o.previousValue),
            Codec.BOOL.fieldOf("new").forGetter(o -> o.newValue),
            Type.CODEC.fieldOf("type").forGetter(Flag::type)
        ).apply(instance, Flag::new));

        public Flag(Key changedKey, boolean previousValue, boolean newValue, Type ignored)
        {
            super(changedKey, false);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        public Flag(Key changedKey, boolean previousValue, boolean newValue, boolean removed)
        {
            super(changedKey, removed);
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        @Override
        public Codec<Flag> codec()
        {
            return CODEC;
        }

        @Override
        public Type type()
        {
            return Type.FLAG;
        }
    }

    public enum Type implements StringValue
    {
        NUM, INT, STRING, FLAG;

        public static final Codec<Type> CODEC = StringValue.fromValues(Type::values);

        @Override
        public java.lang.String stringValue()
        {
            return switch (this)
            {
                case NUM -> "num";
                case INT -> "int";
                case STRING -> "string";
                case FLAG -> "flag";
            };
        }
    }
}
