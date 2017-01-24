package coven.nym;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;

public interface Author {
	
	// getPublicKey
	public PublicKey getPublicKey();
	
	// verify
	boolean verify( byte[] message, byte[] signature ) throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException ;

	// getId
	String getAuthorId();
	
}
