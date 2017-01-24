package coven.data;

import java.util.Map;

public interface CovenDocument {
	
	String getAuthor();
	String getSignature();
	String getDataId();
	String getGUID();
	
	Map<String,Object> getFields();

}
