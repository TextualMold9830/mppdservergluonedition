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
package com.watabou.pixeldungeon.sprites;

import com.nikita22007.multiplayer.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Heap;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public abstract class ItemSprite  {

	public static final int SIZE	= 16;

	public static void dropEffects(Heap heap) {

		if (heap == null) {
			return;
		}

		if (heap.isEmpty()) {
			return;
		}

		boolean visible = Dungeon.visibleforAnyHero(heap.pos);
		boolean[] visibleForHeroes = Dungeon.visibleForHeroes(heap.pos);

		if (!visible) {
			return; //optimization
		}

		if (heap.peek() instanceof Gold) {
			CellEmitter.center(heap.pos).burst(Speck.factory(Speck.COIN), 5);
			for (int ID = 0; ID < visibleForHeroes.length; ID++) {
				Sample.INSTANCE.play(Assets.SND_GOLD, 1, 1, Random.Float(0.9f, 1.1f));
			}
		} else {
			boolean water = Level.water[heap.pos];
			if (water) {
				GameScene.ripple(heap.pos);
			} else {
				int cell = Dungeon.level.map[heap.pos];
				water = (cell == Terrain.WELL || cell == Terrain.ALCHEMY);
			}
			for (int ID = 0; ID < visibleForHeroes.length; ID++) {
				if (visibleForHeroes[ID]) {
					Sample.INSTANCE.play(water ? Assets.SND_WATER : Assets.SND_STEP, 0.8f, 0.8f, 1.2f, Dungeon.heroes[ID]);
				}
			}
		}
	}

	public static int pick( int index, int x, int y ) {
		/*Bitmap bmp = TextureCache.get( Assets.ITEMS ).bitmap;
		int rows = bmp.getWidth() / SIZE;
		int row = index / rows;
		int col = index % rows;
		return bmp.getPixel( col * SIZE + x, row * SIZE + y );*/
		return 0;
	}

}
