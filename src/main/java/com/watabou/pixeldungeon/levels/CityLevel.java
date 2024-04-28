/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.levels;

import com.nikita22007.multiplayer.noosa.particles.Emitter;
import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.pixeldungeon.levels.Room.Type;
import com.watabou.pixeldungeon.network.SendData;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class CityLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}
	
	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
	}
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}
	}
	
	@Override
	protected void decorate() {
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) { 
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) { 
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		while (true) {
			int pos = roomEntrance.random();
			if (pos != entrance) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
	}
	
	@Override
	protected void createItems() {
		super.createItems();
		
		Imp.Quest.spawn( this, roomEntrance );
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Suspiciously colored water";
		case Terrain.HIGH_GRASS:
			return "High blooming flowers";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.ENTRANCE:
			return "A ramp leads up to the upper depth.";
		case Terrain.EXIT:
			return "A ramp leads down to the lower depth.";
		case Terrain.WALL_DECO:
		case Terrain.EMPTY_DECO:
			return "Several tiles are missing here.";
		case Terrain.EMPTY_SP:
			return "Thick carpet covers the floor.";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "The statue depicts some dwarf standing in a heroic stance.";
		case Terrain.BOOKSHELF:
			return "The rows of books on different disciplines fill the bookshelf.";
		default:
			return super.tileDesc( tile );
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				new Smoke( i );
			}
		}
	}
	
	private static class Smoke extends Emitter {
		
		private int pos;

		private static final Factory factory = new Factory() {

			@Override
			public String factoryName() {
				return "smoke_level";
			}
		};

		public static void addSmoke(int pos) {
			JSONObject actionObj = new JSONObject();
			try {
				actionObj.put("action_type", "emitter_decor");
				actionObj.put("type", "smoke");
				actionObj.put("pos", pos);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			SendData.sendCustomActionForAll(actionObj);
		}

		public Smoke( int pos ) {
			super();
			addSmoke(pos);

			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 4, p.y - 2, 4, 0 );
			
			pour( factory, 0.2f );
		}
	}

}
