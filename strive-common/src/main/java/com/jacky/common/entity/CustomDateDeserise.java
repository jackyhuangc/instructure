package com.jacky.common.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.jacky.common.util.DateUtil;

import java.io.IOException;
import java.util.Date;

public class CustomDateDeserise extends DateDeserializer {

	private static final long serialVersionUID = 1599753894430312668L;

	@Override
	protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
		try {
			return DateUtil.parseDate(p.getText().trim());
		} catch (Exception e) {
			return null;
		}
	}
}
