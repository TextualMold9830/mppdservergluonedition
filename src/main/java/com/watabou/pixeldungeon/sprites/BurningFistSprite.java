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
import com.nikita22007.multiplayer.server.effects.MagicMissile;
import com.watabou.pixeldungeon.Assets;

public class BurningFistSprite extends MobSprite {

	public BurningFistSprite() {
		super();
		texture( Assets.BURNING );
		idle = new Animation( 2, true );
		play( idle );
	}

	private int posToShoot;

	@Override
	public void attack( int cell ) {
		posToShoot = cell;
		super.attack( cell );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == attack) {

			Sample.INSTANCE.play( Assets.SND_ZAP );
			MagicMissile.shadow( ch.pos, posToShoot );
			idle();
			ch.onAttackComplete();

		} else {
			super.onComplete( anim );
		}
	}
}
