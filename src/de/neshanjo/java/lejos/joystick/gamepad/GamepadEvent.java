/*******************************************************************************
 * Copyright (C) 2016, 2017 Johannes C. Schneider
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.neshanjo.java.lejos.joystick.gamepad;

import net.java.games.input.Component.Identifier;

public class GamepadEvent {

    public static final float BUTTON_UP = 0.0f;
    public static final float BUTTON_DOWN = 1.0f;
    public static final float AXIS_NEUTRAL = 0f;
    public static final float AXIS_LEFT = -1.0f;
    public static final float AXIS_RIGHT = 1.0f;
    public static final float AXIS_FOREWARD = -1.0f;
    public static final float AXIS_BACK = 1.0f;
    
    
    private final Identifier identifier;
    private final float value;
    
    public GamepadEvent(Identifier identifier, float value) {
        super();
        this.identifier = identifier;
        this.value = value;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public float getValue() {
        return value;
    }
    
}
