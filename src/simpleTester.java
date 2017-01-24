import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.HashMap;

import coven.data.CovenDocument;
import coven.data.CovenDocumentBouncyCastleImpl;
import coven.nym.Nym;
import coven.nym.NymBouncyCastleImpl;

public class simpleTester {

	public static void main(String[] args) throws 
	  NoSuchAlgorithmException, 
	  InvalidKeyException, 
	  SignatureException, 
	  NoSuchProviderException, 
	  IOException 
	
	{
		
		Nym myNym = NymBouncyCastleImpl.create("Abe");
		
		System.out.println( "my new authorID is " + myNym.getAuthorId() );
		
		HashMap<String,Object> fields = new HashMap<String,Object>();
		
		// add some fields
		
		fields.put("title", "A Fox Tale");
		fields.put("body", "A quick brown fox jumped over the lazy dog.");
		fields.put("desc", "a harrowing tale of a fox and a dog. a tour de force.");

		CovenDocument myDoc = CovenDocumentBouncyCastleImpl.create( fields, myNym);
		
		System.out.println( "my new document GUID: " + myDoc.getGUID() );

	}

}
