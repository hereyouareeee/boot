package com.boot.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 自定义json序列化,
 * 对于Intger Double Boolean 等转成String
 */
public interface CustomJsonSerialize {
    class NullObjectSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {
            if (object == null) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        }
    }

    class NullSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {

            if(object == null){
                jsonGenerator.writeString("");
            }

        }
    }

    class ArrayNullSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {
            if (object == null) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
        }
    }

    class NumberNullSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {
            if (object == null) {
                jsonGenerator.writeNumber(0);
            }
        }
    }

    class OriginalNullSerialize extends JsonSerializer<Object> {
        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {
            if (object == null) {
                jsonGenerator.writeNull();
            }
        }
    }

    class DecimalSerialize extends JsonSerializer<Object> {
        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {
            if (object instanceof BigDecimal) {
                jsonGenerator.writeNumber(
                        ((BigDecimal) object).setScale(2, BigDecimal.ROUND_CEILING));
            }
        }
    }

    class AnySerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider
                serializerProvider) throws IOException {

            if (object == null) {
                jsonGenerator.writeString("");
            }
            if(object instanceof BigDecimal){
                jsonGenerator.writeString(String.valueOf(
                        ((BigDecimal) object).setScale(2,BigDecimal.ROUND_CEILING)));
                return;
            }
            if(object instanceof Number){
                jsonGenerator.writeString(String.valueOf(object));
                return;
            }
            if(object instanceof Boolean){
                if(object == Boolean.TRUE){
                    jsonGenerator.writeString(String.valueOf(1));
                }else{
                    jsonGenerator.writeString(String.valueOf(0));
                }
            }

            if(object instanceof Date){
                jsonGenerator.writeString(String.valueOf(((Date) object).getTime()));
            }

            if (object instanceof String) {
                jsonGenerator.writeString((String)object);
            }

            jsonGenerator.writeObject(object);
        }
    }
}
