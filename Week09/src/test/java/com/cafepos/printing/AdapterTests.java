package com.cafepos.printing;

import vendor.legacy.LegacyThermalPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AdapterTests {

    @Test
    public void adapter_converts_text_to_bytes() {
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer p = new LegacyPrinterAdapter(fake);
        
        p.print("ABC");
        
        Assertions.assertTrue(fake.lastLen >= 3, 
            "Adapter should convert text to bytes. Expected length >= 3, got: " + fake.lastLen);
    }

    @Test
    public void adapter_handles_empty_string() {
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer p = new LegacyPrinterAdapter(fake);
        
        p.print("");
        
        Assertions.assertEquals(0, fake.lastLen, "Empty string should produce 0-byte payload");
    }

    @Test
    public void adapter_handles_multiline_text() {
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer p = new LegacyPrinterAdapter(fake);
        
        String multiLine = "Line 1\nLine 2\nLine 3";
        p.print(multiLine);
        
        Assertions.assertTrue(fake.lastLen > 10, "Multi-line text should produce larger byte array");
    }

    // Helper class to capture legacy printer calls
    static class FakeLegacyPrinter extends LegacyThermalPrinter {
        int lastLen = -1;
        
        @Override
        public void legacyPrint(byte[] payload) {
            lastLen = payload.length;
        }
    }
}


