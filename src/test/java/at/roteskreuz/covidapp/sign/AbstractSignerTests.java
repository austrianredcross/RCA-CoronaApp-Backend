package at.roteskreuz.covidapp.sign;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Abstract Signer Tests
 * Implements the tests for all implementations
 *
 * @author TuxCoder <git@o-g.at>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractSignerTests  {
	protected AbstractSigner signer;


	@Test()
	public void simpleStringSignTest() throws Exception {
		byte[] input = "Hallo Welt".getBytes();
		byte[] ret = signer.sign(input);
		assertNotNull(ret);
		assertTrue(ret.length > 0);
	}

	@Test()
	public void longStringTest() throws Exception {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<1000; i++) {
			sb.append("Hallo Welt");
		}
		byte[] input = sb.toString().getBytes();
		byte[] ret = signer.sign(input);
		assertNotNull(ret);
		assertTrue(ret.length > 0);
	}

	@Test(expected = NullPointerException.class)
	public void nullPointerTest() throws Exception {
		signer.sign(null);
	}
}

