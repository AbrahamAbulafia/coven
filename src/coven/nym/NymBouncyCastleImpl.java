package coven.nym;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import coven.util.Base58;
import coven.util.UtilFunctions;

public class NymBouncyCastleImpl implements Nym {
	
	String shortName;
	PrivateKey privKey;
	PublicKey pubKey;
	String authorID;
	
	public static NymBouncyCastleImpl create( String shortName ) throws NoSuchAlgorithmException {
		
		NymBouncyCastleImpl newObj = new NymBouncyCastleImpl();
		
		newObj.shortName = shortName;
		
		// compiles a new key
        
        KeyPairGenerator keyGen;
		
		keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
	    KeyPair key = keyGen.generateKeyPair();
	    newObj.privKey = key.getPrivate();
	    newObj.pubKey = key.getPublic();

	    MessageDigest messageDigest;
		
		messageDigest = MessageDigest.getInstance("SHA-256");
		//newObj.authorID = messageDigest.digest( newObj.pubKey.getEncoded() );
		byte[] pubkeyHash = messageDigest.digest( newObj.pubKey.getEncoded() );
		
		// this is for error checking. virtually impossible to mistype an address
		Checksum checksum = new Adler32();
        checksum.update(pubkeyHash,0,pubkeyHash.length);
        Long lngChecksum = checksum.getValue();
        
        byte[] finalAddress = UtilFunctions.concatenateByteArrays(pubkeyHash, new byte[] {lngChecksum.byteValue()} );
		
        String encodedAddress = Base58.encode(finalAddress);
        newObj.authorID = encodedAddress;
        
		return newObj;
        
	}

	public PublicKey getPublicKey() {
		return pubKey;
	}

	public boolean verify(byte[] message, byte[] signature)
			throws SignatureException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
		
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(pubKey);
	    sig.update(message);
	    return sig.verify(signature);
	    
	}

	public String getAuthorId() {
		return authorID;
	}

	public PrivateKey getPrivateKey() {
		return privKey;
	}

	public String getShortName() {
		return shortName;
	}

	public byte[] sign(byte[] message)
			throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
		
		Signature signer = Signature.getInstance( "SHA1withRSA");
		signer.initSign(privKey);
		signer.update(message);
		return signer.sign();
		
	}
	

}
