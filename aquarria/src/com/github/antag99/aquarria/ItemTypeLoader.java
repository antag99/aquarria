/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.event.Event;
import com.github.antag99.aquarria.event.ScriptEventListener;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemUsageStyle;

public class ItemTypeLoader extends TypeLoader<ItemType> {
	public ItemTypeLoader() {
		super("item", ItemType.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load(ItemType type, JsonValue config) {
		type.setDisplayName(config.getString("displayName", ""));
		type.setMaxStack(config.getInt("maxStack", 1));
		type.setWidth(config.getFloat("width"));
		type.setHeight(config.getFloat("height"));
		type.setUsageTime(config.getFloat("usageTime", 0f));
		type.setUsageAnimationTime(config.getFloat("usageAnimationTime", type.getUsageTime()));
		type.setUsageRepeat(config.getBoolean("usageRepeat", false));
		type.setUsageStyle(ItemUsageStyle.swing); // TODO: This should be changed
		type.setConsumable(config.getBoolean("consumable", false));

		// Register event handlers
		if (config.has("events")) {
			for (JsonValue event : config.get("events")) {
				Class<?> eventClass;
				String handlerScript;

				try {
					eventClass = Class.forName(event.name);
				} catch (ClassNotFoundException ex) {
					throw new RuntimeException("Event class " + event.name + " not found");
				}

				if (!Event.class.isAssignableFrom(eventClass)) {
					throw new RuntimeException(eventClass.getName() + " is not a subclass of " + Event.class.getName());
				}

				handlerScript = event.asString();
				if (handlerScript == null) {
					throw new RuntimeException("Invalid handler; not a string!");
				}

				LuaValue handler = GameRegistry.getGlobals().loadfile(handlerScript).call();
				type.getEvents().registerListener(new ScriptEventListener<Event>(handler.checkfunction(), (Class<Event>) eventClass, 0f));
			}
		}
	}

	@Override
	public void loadAssets(ItemType type, JsonValue config, AssetManager assetManager) {
		if (config.has("texture")) {
			assetManager.load(config.getString("texture"), TextureRegion.class);
		}
	}

	@Override
	public void getAssets(ItemType type, JsonValue config, AssetManager assetManager) {
		if (config.has("texture")) {
			type.setTexture(assetManager.get(config.getString("texture", null), TextureRegion.class));
		}
	}
}
