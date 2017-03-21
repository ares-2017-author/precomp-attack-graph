package attackGraph;

import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import support.TestDistribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BucketListTest {

    double min;
    double max;
    int nBuckets;
    double bucketSize;

    @Before
    public void setup() {
        min = 0;
        max = 1000;
        nBuckets = 4096;
        bucketSize = (max - min) / (nBuckets);
    }

    @Test
    public void testInit() {
        BucketList bl = new BucketList(min,max,nBuckets);
        assertTrue(bl.isEmpty());

        AttackStep a = new AttackStepMin("a", new TestDistribution(5, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(5, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new ConstantRealDistribution(0), Order.ENTRYSTEP);

        a.sample();
        b.sample();
        c.sample();

        a.setTtc(a.getLocalTtc());
        b.setTtc(b.getLocalTtc());
        c.setTtc(c.getLocalTtc());

        bl.append(a);
        bl.append(b);
        bl.append(c);

        int aId = Integer.min(((int) (a.getTtc() / bucketSize)), nBuckets + 1);
        int bId = Integer.min(((int) (b.getTtc() / bucketSize)), nBuckets + 1);
        int cId = Integer.min(((int) (c.getTtc() / bucketSize)), nBuckets + 1);

        assertEquals(a.getBucket(),bl.getBucket(aId));
        assertEquals(b.getBucket(),bl.getBucket(bId));
        assertEquals(c.getBucket(),bl.getBucket(cId));
    }
}
