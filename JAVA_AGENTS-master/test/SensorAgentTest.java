import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SensorAgentTest {

    @Test
    public void LastTempShouldBeSmallerThanLastTempPlusOne() {
        SensorAgent tester = new SensorAgent(); // MyClass is tested
        assertEquals(true, tester.lastTemp<tester.lastTemp);
    }
}