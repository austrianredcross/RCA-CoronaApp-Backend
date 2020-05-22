package at.roteskreuz.covidapp.service;

import at.roteskreuz.covidapp.model.Publish;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author zolika
 */
//@SpringBootTest
public class AndroidNonceServiceTest {
	
	@Autowired
	private AndroidNonceService nonceService;	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//@Test
	public void testNonce() throws JsonProcessingException {
		String json = "{\"temporaryTracingKeys\": [\n" +
"	{\"key\": \"aeiukfhdcliukshdf==\", \"intervalNumber\": 12, \"intervalCount\": 144, \"transmissionRisk\": 1}, \n" +
"	{\"key\": \"aisdhjl;ahksdkl==,\", \"intervalNumber\": 24, \"intervalCount\": 10, \"transmissionRisk\": 4},\n" +
"	{\"key\": \"wedfuhlihkalsd333==\", \"intervalNumber\": 133, \"intervalCount\": 100, \"transmissionRisk\": 5}], \n" +
"  \"regions\": [\"US\", \"CA\", \"mx\"],\n" +
"  \"appPackageName\": \"com.foo.app\",\n" +
"  \"diagnosisStatus\": 2,\n" +
"  \"deviceVerificationPayload\": \"asldkfhjlkajsbdf==\",\n" +
"  \"verificationAuthorityName\": \"RedCross\",\n" +
"  \"verificationPayload\": \"aslkxj;alkxj\"\n" +
"}";
		Publish publish = objectMapper.readValue(json, Publish.class);
		
		System.out.println(nonceService.nonce(publish));
	}	
	
}
