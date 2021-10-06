package br.unb.cic.goda.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GodaUtils {

	public static String objectToString(Object obj)  {
		String result;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			result = objectMapper.writeValueAsString(obj);
			return result;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	

	public static String objectToJSON(Object obj)  {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson(obj);

		return result;
	}

}
