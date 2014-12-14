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
package com.github.antag99.aquarria.world.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.world.World;

public class EntityRenderer extends WorldRendererCallback {
	@SuppressWarnings("rawtypes")
	private ObjectMap<Class, EntityTypeRenderer> entityTypeRenderers = new ObjectMap<Class, EntityTypeRenderer>();

	public EntityRenderer() {
		addEntityTypeRenderer(new PlayerEntityRenderer());
		addEntityTypeRenderer(new ItemEntityRenderer());
	}

	public void queueAssets(AssetManager assetManager) {
		for (EntityTypeRenderer<?, ?> entityRenderer : entityTypeRenderers.values()) {
			entityRenderer.queueAssets(assetManager);
		}
	}

	public void getAssets(AssetManager assetManager) {
		for (EntityTypeRenderer<?, ?> entityRenderer : entityTypeRenderers.values()) {
			entityRenderer.getAssets(assetManager);
		}
	}

	public void addEntityTypeRenderer(EntityTypeRenderer<?, ?> entityTypeRenderer) {
		entityTypeRenderers.put(entityTypeRenderer.getEntityClass(), entityTypeRenderer);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void render(Batch batch, World world, int startX, int startY, int endX, int endY) {
		for (Entity entity : world.getEntities()) {
			if (entity.isActive()) {
				batch.setColor(Color.WHITE);

				EntityTypeRenderer entityTypeRenderer = entityTypeRenderers.get(entity.getClass());
				entityTypeRenderer.renderEntity(batch, entity.getView());
			}
		}
	}

}
