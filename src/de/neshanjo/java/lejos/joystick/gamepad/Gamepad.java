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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad {

    private static final Logger LOG = LogManager.getLogger(Gamepad.class);
    private static volatile Gamepad INSTANCE;
    // used when testing float values for equality and for value change
    private static final double DELTA = 0.01;

    public static Gamepad getInstance() throws GamepadException {
        if (INSTANCE == null) {
            synchronized (Gamepad.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Gamepad();
                }
            }
        }
        return INSTANCE;
    }

    class PollingThread extends Thread {

        @Override
        public void run() {
            while (runPolling) {
                poll();
                evaluateActions();
                try {
                    Thread.sleep(pollTimeInMs);
                } catch (InterruptedException e) {
                    LOG.warn("Unexpected interruption of sleep", e);
                }
            }
        }
    }

    private final Controller gamepad;
    private long pollTimeInMs = 100;
    private boolean runPolling = true;
    private final Map<GamepadEvent, Action> actions = new HashMap<>();
    private final Map<Identifier, Float> lastValues = new HashMap<>();

    private Gamepad() throws GamepadException {
        final Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            throw new GamepadException("No gamepad connected");
        }
        Controller controller = null;
        if (LOG.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(controllers.length).append(" controllers detected");
            for (Controller c : controllers) {
                sb.append(", ").append(c.getName());
                if (c.getType() == Controller.Type.STICK) {
                    controller = c;
                }
            }
            LOG.debug(sb.toString());
        }

        if (controller == null) {
            throw new GamepadException("No gamepad controller found");
        }
        LOG.debug("Using controller " + controller.getName());
        gamepad = controller;
    }

    public long getPollTimeInMs() {
        return pollTimeInMs;
    }

    public void setPollTimeInMs(long pollTimeInMs) {
        if (pollTimeInMs < 10) {
            throw new IllegalArgumentException("time must not be smaller than 10");
        }
        this.pollTimeInMs = pollTimeInMs;
    }

    public void startPolling() {
        poll();
        initLastValues();
        runPolling = true;
        new PollingThread().start();
    }

    public void stopPolling() {
        runPolling = false;
    }

    private void evaluateActions() {
        for (Entry<GamepadEvent, Action> entry : actions.entrySet()) {
            final Identifier identifier = entry.getKey().getIdentifier();
            final float targetValue = entry.getKey().getValue();
            final float lastValue = lastValues.get(identifier);
            float currentValue = gamepad.getComponent(identifier).getPollData();
            if (hasChanged(lastValue, currentValue) && equal(currentValue, targetValue)) {
                try {
                    entry.getValue().doAction();
                } catch (Exception e) {
                    LOG.warn("Action failed", e);
                }
            }
        }
        initLastValues();
    }

    private boolean hasChanged(float lastValue, float currentValue) {
        return Math.abs(lastValue - currentValue) > DELTA;
    }

    private boolean equal(float x, float y) {
        return Math.abs(x - y) <= DELTA;
    }

    private void initLastValues() {
        lastValues.clear();
        for (Component component : gamepad.getComponents()) {
            lastValues.put(component.getIdentifier(), component.getPollData());
        }
    }

    public void addAction(GamepadEvent event, Action action) {
        actions.put(event, action);
    }

    private void poll() {
        if (!gamepad.poll()) {
            final String message = "Gamepad not accessable anymore.";
            LOG.error(message);
            throw new IllegalStateException(message);
        }
    }
}
