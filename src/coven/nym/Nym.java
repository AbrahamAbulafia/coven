package coven.nym;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;


public interface Nym extends Author {
	
	// getPrivateKey
	public PrivateKey getPrivateKey();
	
	// getShortName
	public String getShortName();
	
	// sign
	public byte[] sign( byte[] message ) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException;
	
	
}
