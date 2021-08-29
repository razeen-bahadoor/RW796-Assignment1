import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {

    private static final String INITIAL = "Hello World";

    private StringUtils str1;
    private  StringUtils str2;

    @Before
    public void init() {
        str1 = new StringUtils(INITIAL);
        str2 = new StringUtils();
    }


    /**
     * Tests append method
     */
    @Test
    public void testAppend() {
        assertEquals("Hello World!", str1.append('!').toString());
    }

    @Test
    public void testCharAtNegativeIdx() {
        assertThrows(IndexOutOfBoundsException.class, () ->{
            str1.charAt(-1);
        });
    }

    @Test
    public void testCharAtIdxOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class, () ->{
            str1.charAt(100);
        });
    }

    @Test
    public void testCharAt() {
        assertEquals('H', str1.charAt(0));
    }


    @Test
    public void testDeleteCharAtNegativeIdx() {
        assertThrows(IndexOutOfBoundsException.class, () ->{
            str1.deleteCharAt(-1);
        });
    }

    @Test
    public void testDeleteCharAtIdxOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class, () ->{
            str1.deleteCharAt(100);
        });
    }

    @Test
    public void testDeleteCharAt() {
        assertEquals("ello World", str1.deleteCharAt(0).toString());
    }



    @Test
    public void testReverse() {
        assertEquals("dlroW olleH", str1.reverse().toString());
    }

//    @Test
//    public void failTest() {
//        fail();
//    }


}
