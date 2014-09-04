package org.springframework.data.semantic.support.util;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public final class ValueUtils {
	
	public static final String RDF_TYPE_PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	private static final ValueFactory valueFactory = ValueFactoryImpl.getInstance();
	
	/**
	 * Checks whether the given string is a valid {@link URI}.
	 * @param source
	 * @return
	 */
	public static boolean isAbsoluteURI(String source){
		int schemeSepIdx = source.indexOf(':');
		if((schemeSepIdx < 0) || (source.indexOf(' ') > -1) || (source.indexOf('|') > 0) ){
			return false;
		}
		else {
			String rest = source.contains("//") ? source.substring(schemeSepIdx+3) : source.substring(schemeSepIdx+1);
			return parse(rest);
			
		}
	}
	
	private static boolean parse (CharSequence source){
		int atIdx = -1;
		int qmIdx = -1;
		int diazIdx = -1;
		int slLastIdx = -1;
		int dcolLastIdx = -1;
		for(int i = 0; i < source.length(); i++){
			char c = source.charAt(i);
			if(c == ':'){
				if(qmIdx < 0 && diazIdx < 0){
					dcolLastIdx = i;
				}
				else{
					return false;
				}
			}
			if(c == '@'){
				if(atIdx < 0 && qmIdx < 0 && diazIdx < 0 && slLastIdx < 0){
					atIdx = i;
				}
				else{
					return false;
				}
			}
			if(c == '/'){
				if(qmIdx < 0 && diazIdx < 0){
					slLastIdx = i;
				}
				else{
					return false;
				}
			}
			if(c == '?'){
				if(qmIdx < 0 && diazIdx < 0){
					qmIdx = i;
				}
				else{
					return false;
				}
			}
			if(c == '#'){
				if(diazIdx < 0){
					diazIdx = i;
				}
				else{
					return false;
				}
			}
		}
		if(atIdx < 0 && qmIdx < 0 && diazIdx < 0 && slLastIdx < 0 && dcolLastIdx < 0){
			return false;
		}
		return true;
	}
	
	public static URI createUri(String source){
		return valueFactory.createURI(source);
	}
	
	public static URI createUri(String namespace, String localName){
		return valueFactory.createURI(namespace, localName);
	}

}