package attackgraph;

import computation.Bucket;
import datatypes.Order;
import org.junit.Test;
import support.TestDistribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BucketTest {
    @Test
    public void testAppend() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);

        Bucket bucket = new Bucket(1);
        assertTrue(bucket.isEmpty());
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),null);
        assertEquals(c.getBucket(),null);
        bucket.append(a);
        assertEquals(a.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
        bucket.append(b);
        assertEquals(a.getBucket(),bucket);
        assertEquals(b.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
        bucket.append(c);
        assertEquals(a.getBucket(),bucket);
        assertEquals(b.getBucket(),bucket);
        assertEquals(c.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
    }

    @Test
    public void testRemove() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);

        Bucket bucket = new Bucket(1);
        bucket.append(a);
        bucket.remove(a);
        assertEquals(a.getBucket(),null);
        assertTrue(bucket.isEmpty());
        bucket.append(a);
        bucket.append(b);
        bucket.remove(a);
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),bucket);
        bucket.reset();
        assertTrue(bucket.isEmpty());
        bucket.append(a);
        bucket.remove(a);
        bucket.append(b);
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),bucket);
        bucket.reset();
        bucket.append(a);
        bucket.append(b);
        bucket.append(c);
        bucket.remove(b);
        assertEquals(a.getBucket(),bucket);
        assertEquals(b.getBucket(),null);
        assertEquals(c.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
        bucket.remove(c);
        assertFalse(bucket.isEmpty());
        bucket.remove(a);
        assertTrue(bucket.isEmpty());
    }

    @Test
    public void testExtractFirst() {
        AttackStep a = new AttackStepMin("a", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep b = new AttackStepMin("b", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);
        AttackStep c = new AttackStepMin("c", new TestDistribution(0.2, 10, 1), Order.ENTRYSTEP);

        a.connectToChild(b);

        Bucket bucket = new Bucket(1);
        bucket.append(a);
        bucket.extractFirst();
        assertEquals(a.getBucket(),null);
        assertTrue(bucket.isEmpty());
        bucket.append(a);
        bucket.append(b);
        bucket.extractFirst();
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),bucket);
        bucket.reset();
        assertTrue(bucket.isEmpty());
        bucket.append(a);
        bucket.extractFirst();
        bucket.append(b);
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),bucket);
        bucket.extractFirst();
        assertEquals(b.getBucket(),null);
        assertTrue(bucket.isEmpty());
        bucket.reset();
        bucket.append(a);
        bucket.append(b);
        bucket.append(c);
        bucket.extractFirst();
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),bucket);
        assertEquals(c.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
        bucket.extractFirst();
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),null);
        assertEquals(c.getBucket(),bucket);
        assertFalse(bucket.isEmpty());
        bucket.extractFirst();
        assertEquals(a.getBucket(),null);
        assertEquals(b.getBucket(),null);
        assertEquals(c.getBucket(),null);
        assertTrue(bucket.isEmpty());
    }
}
