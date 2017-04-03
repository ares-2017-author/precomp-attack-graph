package attackgraph;


import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class OrdinalTtcTest {

    /**
     * Unit Testing of the Min Function
//     */
//
//    @Ignore
//    @Test
//    public void minANYANY(){
//        /* If both parents have the same ordinal value, then this value is min */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    @Ignore
//    @Test
//    public void minUNDEFUNDEF(){
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.UNDEFINED);
//    }
//
//    @Ignore
//    @Test
//    public void minZERO(){
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ZERO);
//    }
//
//    @Ignore
//    @Test
//    public void minZERO2(){
//
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ZERO);
//    }
//
//    @Ignore
//    @Test
//    public void minINFINITE(){
//
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void minINFINITE2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    @Test
//    public void minUNDEF(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void minUNDEF2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void minLTESOURCE1(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void minLTESOURCE2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void minLTESOURCE3(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ZERO);
//    }
//
//    @Test
//    public void minLTESOURCE4(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ZERO);
//    }
//
//    @Test
//    public void minANYSOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void minSOURCEANY(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void minSOURCEGTESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void minGTESOURCESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void minGTESOURCEANY(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    @Test
//    public void minANYGTESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.min(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    /**
//     * Unit Testing of the Max Function
//     */
//
//    @Test
//    public void maxANYANY(){
//        /* If both parents have the same ordinal value, then this value is max */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    @Test
//    public void maxUNDEFUNDEF(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.UNDEFINED);
//    }
//
//    @Test
//    public void maxZERO(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void maxZERO2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxINFINITE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.INFINITE);
//    }
//
//    @Test
//    public void maxINFINITE2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.INFINITE);
//    }
//
//    @Test
//    public void maxUNDEF(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.LTESOURCE);
//    }
//
//    @Test
//    public void maxUNDEF2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.UNDEFINED);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxGTESOURCE1(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxGTESOURCE2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxGTESOURCE3(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxGTESOURCE4(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxANYSOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxSOURCEANY(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void maxSOURCELTESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void maxLTESOURCESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void maxLTESOURCEANY(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    @Test
//    public void maxANYLTESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtc comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc();
//        testOTtc.setValue(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.max(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
//
//    /**
//     * Unit Testing of the Plus Function
//     */
//
//    @Test
//    public void plusINFINITE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.INFINITE);
//    }
//
//    @Test
//    public void plusINFINITE2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.INFINITE);
//    }
//
//    @Test
//    public void plusINFINITE3(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.INFINITE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.INFINITE);
//    }
//
//    @Test
//    public void plusZERO(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void plusZERO2(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusZERO3(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.ZERO);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ZERO);
//    }
//
//    @Test
//    public void plusGTESOURCE(){
//        /* If both parents are ANY, then it is ANY */
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusGTESOURCE2(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusGTESOURCE3(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusGTESOURCE4(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusSOURCE(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.GTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusSOURCE2(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusSOURCE3(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ZERO);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.SOURCE);
//    }
//
//    @Test
//    public void plusSOURCE4(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.SOURCE);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.LTESOURCE);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.GTESOURCE);
//    }
//
//    @Test
//    public void plusANYANY(){
//
//        OrdinalTtcValue comparedOTtc = mock(OrdinalTtc.class);
//        when(comparedOTtc.getValue()).thenReturn(OrdinalTtcValue.ANY);
//
//        OrdinalTtc testOTtc = new OrdinalTtc(OrdinalTtcValue.ANY);
//
//        OrdinalTtcValue resultOTtc = testOTtc.plus(comparedOTtc);
//        assertEquals(resultOTtc.getValue(),OrdinalTtcValue.ANY);
//    }
}
