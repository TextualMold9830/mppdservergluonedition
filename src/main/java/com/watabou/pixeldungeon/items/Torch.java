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
package com.watabou.pixeldungeon.items;

import com.nikita22007.multiplayer.noosa.particles.Emitter;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Light;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.particles.FlameParticle;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Torch extends Item {

	public static final String AC_LIGHT	= "LIGHT";

	public static final float TIME_TO_LIGHT = 1;

	{
		name = "torch";
		image(ItemSpriteSheet.TORCH);

		stackable = true;

		defaultAction = AC_LIGHT;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_LIGHT );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		if (action == AC_LIGHT) {

			hero.spend( TIME_TO_LIGHT );
			hero.busy();

			hero.getSprite().operate( hero.pos );

			detach( hero.belongings.backpack );
			Buff.affect( hero, Light.class, Light.DURATION );

			Emitter emitter = hero.getSprite().centerEmitter();
			emitter.start( FlameParticle.FACTORY, 0.2f, 3 );

		} else {

			super.execute( hero, action );

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int price() {
		return 10 * getQuantity();
	}

	@Override
	public String info() {
		return
			"It's an indispensable item in The Demon Halls, which are notorious for their poor ambient lighting.";
	}
}
