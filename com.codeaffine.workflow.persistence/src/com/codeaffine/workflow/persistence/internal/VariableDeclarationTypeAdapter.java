/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.persistence.internal;

import java.lang.reflect.Type;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.persistence.ClassFinder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;


public class VariableDeclarationTypeAdapter
  implements JsonSerializer<VariableDeclaration<?>>, JsonDeserializer<VariableDeclaration<?>>
{

  private final ClassFinder classFinder;

  public VariableDeclarationTypeAdapter( ClassFinder classFinder ) {
    this.classFinder = classFinder;
  }

  public static Type getType() {
    return new TypeToken<VariableDeclaration<?>>() {}.getType();
  }

  @Override
  public JsonElement serialize(
    VariableDeclaration<?> src, Type typeOfSrc, JsonSerializationContext context )
  {
    JsonObject attributes = new JsonObject();
    attributes.add( "type", new JsonPrimitive( src.getType().getName() ) );
    attributes.add( "attributeName", new JsonPrimitive( src.getName() ) );
    JsonObject result = new JsonObject();
    result.add( "VariableDeclaration", attributes );
    return result;
  }

  @Override
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public VariableDeclaration<?> deserialize(
    JsonElement json, Type typeOfT, JsonDeserializationContext context )
    throws JsonParseException
  {
    JsonObject attributes = json.getAsJsonObject().getAsJsonObject( "VariableDeclaration" );
    String type = attributes.get( "type" ).getAsString();
    String name = attributes.get( "attributeName" ).getAsString();
    return new VariableDeclaration( name, classFinder.find( type ) );
  }
}