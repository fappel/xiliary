package com.codeaffine.workflow.persistence.internal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.VariableDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class WorkflowContextTypeAdapter
  implements JsonDeserializer<Map<VariableDeclaration<?>, Object>>,
             JsonSerializer<Map<VariableDeclaration<?>, Object>>
{

  public static Type getType() {
    return new TypeToken<Map<VariableDeclaration<?>, Object>>() {}.getType();
  }

  @Override
  public JsonElement serialize(
    Map<VariableDeclaration<?>, Object> src, Type typeOfSrc, JsonSerializationContext context )
  {
    JsonArray result = new JsonArray();
    for( VariableDeclaration<?> variableDeclaration : src.keySet() ) {
      JsonArray entry = new JsonArray();
      entry.add( context.serialize( variableDeclaration, VariableDeclarationTypeAdapter.getType() ) );
      entry.add( context.serialize( src.get( variableDeclaration ), variableDeclaration.getType() ) );
      result.add( entry );
    }
    return result;
  }

  @Override
  public Map<VariableDeclaration<?>, Object> deserialize(
    JsonElement json, Type typeOfT, JsonDeserializationContext context )
    throws JsonParseException
  {
    Map<VariableDeclaration<?>, Object> result = new HashMap<VariableDeclaration<?>, Object>();
    JsonArray jsonEntries = json.getAsJsonArray();
    for( int i = 0; i < jsonEntries.size(); i++ ) {
      JsonArray jsonEntry = jsonEntries.get( i ).getAsJsonArray();
      VariableDeclaration<?> key = context.deserialize( jsonEntry.get( 0 ), VariableDeclarationTypeAdapter.getType() );
      Object value = context.deserialize( jsonEntry.get( 1 ), key.getType() );
      result.put( key, value );
    }
    return result;
  }
}