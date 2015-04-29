package de.peteral.softplc.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.datatype.DataTypeFactory;
import de.peteral.softplc.datatype.DataTypeUtils;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;

/**
 * Default {@link Memory} implementation.
 * <p>
 * Maintains a set of {@link MemoryArea}.
 *
 * @author peteral
 */
public class MemoryImpl
    implements Memory
{

    private final Map<String, MemoryArea> memoryAreas = new HashMap<>();
    private final AddressParserFactory addressParserFactory;
    private final DataTypeFactory dataTypeFactory;

    /**
     * Creates a new instance.
     * <p>
     *
     * @param addressParserFactory
     *        {@link AddressParserFactory} instance.
     * @param dataTypeFactory
     *        {@link DataTypeFactory} instance.
     * @param areas
     *        set of {@link MemoryArea} managed within this {@link Memory}
     */
    public MemoryImpl(AddressParserFactory addressParserFactory,
                      DataTypeFactory dataTypeFactory,
                      MemoryArea... areas)
    {
        this.addressParserFactory = addressParserFactory;
        this.dataTypeFactory = dataTypeFactory;
        for ( MemoryArea memoryArea : areas )
        {
            memoryAreas.put(memoryArea.getAreaCode(), memoryArea);
        }
    }

    @Override
    public MemoryArea getMemoryArea(String key)
    {
        MemoryArea result = memoryAreas.get(key);

        if ( result != null )
        {
            return result;
        }

        throw new MemoryAccessViolationException("Invalid memory area [" + key
            + "]");
    }

    @Override
    public byte[] readBytes(String area, int offset, int length)
    {
        return getMemoryArea(area).readBytes(offset, length);
    }

    @Override
    public void writeBytes(String area, int offset, byte[] data)
    {
        getMemoryArea(area).writeBytes(offset, data);
    }

    @Override
    public Object read(String address)
    {
        ParsedAddress parsedAddress = addressParserFactory.parse(address);

        if ( parsedAddress.getTypeName().equals("X") )
        {
            return getBit(address);
        }

        MemoryArea memoryArea = getMemoryArea(parsedAddress.getAreaCode());

        byte[] bytes =
            memoryArea.readBytes(parsedAddress.getOffset(),
                                 dataTypeFactory.getTotalSize(parsedAddress));

        Object result = dataTypeFactory.fromBytes(bytes, parsedAddress);

        if ( memoryArea.getLogger() != null && memoryArea.getLogger().isLoggable(Level.FINER) )
        {
            memoryArea.getLogger().finer("Read: " + address + " = " + result);
        }

        return result;
    }

    @Override
    public void write(String address, Object value)
    {
        ParsedAddress parser = addressParserFactory.parse(address);

        if ( parser.getTypeName().equals("X") )
        {
            setBit(address, (Boolean) value);
            return;
        }

        MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

        memoryArea.writeBytes(parser.getOffset(),
                              dataTypeFactory.toBytes(value, parser));

        if ( memoryArea.getLogger() != null && memoryArea.getLogger().isLoggable(Level.FINE) )
        {
            memoryArea.getLogger().fine("Write: " + address + " = " + value);
        }
    }

    @Override
    public boolean getBit(String address)
    {
        ParsedAddress parser = addressParserFactory.parse(address);

        MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

        int byteValue =
            DataTypeUtils.byteToInt(memoryArea.readBytes(parser.getOffset(), 1)[0]);
        int mask = 1 << parser.getBitNumber();

        return (byteValue & mask) == mask;
    }

    @Override
    public void setBit(String address, boolean value)
    {
        ParsedAddress parser = addressParserFactory.parse(address);

        MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

        if ( memoryArea.getLogger() != null &&  memoryArea.getLogger().isLoggable(Level.FINE) )
        {
            memoryArea.getLogger().fine("setBit: " + address + " = " + value);
        }

        int byteValue =
            DataTypeUtils.byteToInt(memoryArea.readBytes(parser.getOffset(), 1)[0]);

        if ( value )
        {
            int mask = 1 << parser.getBitNumber();
            byteValue = byteValue | mask;
        }
        else
        {
            int mask = 1 << parser.getBitNumber();
            mask = ~mask;
            byteValue = byteValue & mask;
        }
        memoryArea.writeBytes(parser.getOffset(),
                              new byte[] { (byte) byteValue });
    }
}
