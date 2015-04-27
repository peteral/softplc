package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.CommunicationTask;

@SuppressWarnings("javadoc")
public class PutGetConnectResponseFactoryTest
{
    private static final byte[] TEST_RESULT = { 0x32, 0x01, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00,
        0x00, 0x00, 0x00, (byte) 0x03, (byte) 0xFC };

    private PutGetConnectResponseFactory factory;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        factory = new PutGetConnectResponseFactory();
    }

    @Test
    public void canHandle_PutGetConnectTask_ReturnsTrue()
    {
        CommunicationTask task = mock(PutGetConnectTask.class);

        assertTrue(factory.canHandle(task));
    }

    @Test
    public void canHandle_NotNutGetConnectTask_ReturnsFalse()
    {
        CommunicationTask task = mock(CommunicationTask.class);

        assertFalse(factory.canHandle(task));
    }

    @Test
    @Ignore
    // FIXME Ignored test
    public void createResponse_MaxSize1000_ReturnsCorrectByteArray()
    {
        PutGetConnectTask task = mock(PutGetConnectTask.class);
        when(task.getMaxDataSize()).thenReturn(1000);

        assertArrayEquals(TEST_RESULT, factory.createResponse(task));
    }
}
