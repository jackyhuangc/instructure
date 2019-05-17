package com.jacky.common.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.jacky.common.util.DateUtil;

import java.io.IOException;
import java.util.Date;


public class CustomDateSerise extends DateSerializer {

	private static final long serialVersionUID = 8583964516845279606L;

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(DateUtil.format(value));
	}
}
