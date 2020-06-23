package at.roteskreuz.covidapp.sign;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Before;

/**
 * Filesystem Signer Tests
 *
 * @author TuxCoder <git@o-g.at>
 */
public class FilesystemSignerTests extends AbstractSignerTests {

	@Autowired
	private FilesystemSigner filesystemSigner;

	@Before
	public void setUp() throws Exception {
		// overwrite protected variable from abstract test
		signer = filesystemSigner;
	}

	//tests are in the abstract class
}

