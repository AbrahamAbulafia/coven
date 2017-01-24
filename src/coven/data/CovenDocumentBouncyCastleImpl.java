package coven.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Map;

import coven.nym.Nym;
import coven.util.Base58;

public class CovenDocumentBouncyCastleImpl implements CovenDocument {
	
		byte[] dataHash;
		byte[] finalHash;
		byte[] sig;
		
		Nym authorNym;
		
		Map<String,Object> fields;
	
	public static CovenDocumentBouncyCastleImpl create( Map< String, Object > fields, Nym authorNym ) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, NoSuchProviderException {
		
		CovenDocumentBouncyCastleImpl doc = new CovenDocumentBouncyCastleImpl();
				
		doc.finalHash = generateFieldHash(fields, authorNym);
		
		// signing that is the data sig
		
		doc.sig = authorNym.sign( doc.finalHash );
		
		doc.fields = fields;
		
		doc.authorNym = authorNym;
		
		return doc;
		
	}

	private static byte[] generateFieldHash (Map<String, Object> fields, Nym authorNym )
			throws NoSuchAlgorithmException, IOException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		// here we use BC to generate the hash
		
		ByteArrayOutputStream dataHash = new ByteArrayOutputStream( );
		for( String fieldname : fields.keySet() ) {
			
		// for each field
			
			ByteArrayOutputStream fieldBuffer = new ByteArrayOutputStream( );
		
			// concat the fieldname + 0x00 + data
			
			fieldBuffer.write( fieldname.getBytes() );
			fieldBuffer.write( (byte)0 );
			fieldBuffer.write( ((String)fields.get( fieldname ) ).getBytes() );
		
			// hash it
			md.update( fieldBuffer.toByteArray() );
		
			// add the hash to StringBuffer
			dataHash.write( md.digest() );

			
		}
		
		// hash the whole thing
		
		md.update( dataHash.toByteArray() );
		
		// take that hash and concat + 0x00 + authorID
		ByteArrayOutputStream finalHash = new ByteArrayOutputStream();
		
		byte[] dataHashBytes = md.digest();
		
		finalHash.write( dataHashBytes );
		finalHash.write( (byte)0 );
		finalHash.write( authorNym.getAuthorId().getBytes() );
		
		// final hash = dataId
		
		md.update( finalHash.toByteArray() );
		return md.digest();
		
	}

	public String getAuthor() {
		
		return authorNym.getAuthorId();
		
	}

	public String getSignature() {
		
		return Base58.encode( sig );
		
	}

	public String getDataId() {
		
		return Base58.encode(finalHash);
		
	}

	public String getGUID() {
		
		return getAuthor() + "-" + getDataId();
		
	}

	public Map<String, Object> getFields() {
		
		return fields;
		
	}
	

}
