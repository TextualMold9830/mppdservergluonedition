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
import com.nikita22007.multiplayer.noosa.particles.Emitter;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.utils.PointF;

public class BlacksmithSprite extends MobSprite {

	private Emitter emitter;

	public BlacksmithSprite() {
		super();

		texture( Assets.TROLL );


		idle = new Animation( 15, true );

		run = new Animation( 20, true );

		die = new Animation( 20, false );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		emitter = new Emitter();

		emitter.pos(this, new PointF(7, 12));
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );

		if (visible && emitter != null && anim == idle) {
			emitter.burst( Speck.factory( Speck.FORGE ), 3 );
			//todo
			//float volume = 0.2f / (Level.distance( ch.pos, Dungeon.hero.pos )); //Это будет чтено  при  передаче
			float volume = 0.2f / (Level.distance( ch.pos, ch.pos));
			Sample.INSTANCE.play( Assets.SND_EVOKE, volume, volume, 0.8f  );
		}
	}

}
