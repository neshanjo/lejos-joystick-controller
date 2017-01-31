/*******************************************************************************
 * Copyright (C) 2016 Johannes C. Schneider
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
package de.neshanjo.java.lejos.joystick;

import de.neshanjo.java.lejos.joystick.gamepad.Action;
import de.neshanjo.java.lejos.joystick.gamepad.Gamepad;
import de.neshanjo.java.lejos.joystick.gamepad.GamepadEvent;
import de.neshanjo.java.lejos.joystick.gamepad.GamepadException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import lejos.hardware.Sound;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Component.Identifier.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoystickController {

    private static final String IP_ADDRESS = "10.0.1.1";
    //private static final String IP_ADDRESS = "192.168.0.42";
    private static final Logger LOG = LogManager.getLogger(Gamepad.class);
    private static int pattern = 0;
    
    public static void main(String[] args) throws RemoteException, GamepadException {

        RemoteEV3 ev3 = null;
        try {
            ev3 = new RemoteEV3(IP_ADDRESS);
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            LOG.error("Could not connect to EV3", e);
            System.exit(2);
        }
        Sound.beep();
        final RMIRegulatedMotor motorL = ev3.createRegulatedMotor("B", 'L');
        final RMIRegulatedMotor motorR = ev3.createRegulatedMotor("C", 'L');
        final RMIRegulatedMotor motorC = ev3.createRegulatedMotor("A", 'M');
        
        motorL.setSpeed(600);
        motorR.setSpeed(600);
        motorC.setSpeed(100);
        //motorL.setAcceleration(360);
        //motorR.setAcceleration(360);

        Gamepad gamepad = Gamepad.getInstance();

        gamepad.addAction(new GamepadEvent(Button._0, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                motorC.forward();
            }
        });

        gamepad.addAction(new GamepadEvent(Button._0, GamepadEvent.BUTTON_UP), new Action() {

            @Override
            public void doAction() throws Exception {
                motorC.stop(true);
            }
        });

        
        gamepad.addAction(new GamepadEvent(Button._2, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                motorC.backward();
            }
        });

        gamepad.addAction(new GamepadEvent(Button._2, GamepadEvent.BUTTON_UP), new Action() {

            @Override
            public void doAction() throws Exception {
                motorC.stop(true);;
            }
        });
        
        gamepad.addAction(new GamepadEvent(Button._6, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.backward();
                motorR.forward();
            }
        });

        gamepad.addAction(new GamepadEvent(Button._6, GamepadEvent.BUTTON_UP), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.stop(true);
                motorR.stop(true);
            }
        });
        
        gamepad.addAction(new GamepadEvent(Button._7, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.forward();
                motorR.backward();
            }
        });

        gamepad.addAction(new GamepadEvent(Button._7, GamepadEvent.BUTTON_UP), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.stop(true);
                motorR.stop(true);
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.X, GamepadEvent.AXIS_NEUTRAL), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.setSpeed(600);
                motorR.setSpeed(600);
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.X, GamepadEvent.AXIS_LEFT), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.setSpeed(300);
                motorR.setSpeed(800);
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.X, GamepadEvent.AXIS_RIGHT), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.setSpeed(800);
                motorR.setSpeed(300);
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.Y, GamepadEvent.AXIS_NEUTRAL), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.stop(true);
                motorR.stop(true);
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.Y, GamepadEvent.AXIS_FOREWARD), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.forward();
                motorR.forward();
            }
        });

        gamepad.addAction(new GamepadEvent(Axis.Y, GamepadEvent.AXIS_BACK), new Action() {

            @Override
            public void doAction() throws Exception {
                motorL.backward();
                motorR.backward();
            }
        });
        
        gamepad.addAction(new GamepadEvent(Button._4, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                LOG.info("Shutdown.");
                lejos.hardware.Button.LEDPattern(0);
                try {
                    motorL.close();
                } finally {
                    try{ 
                        motorR.close();
                    } finally {
                        motorC.close();
                        System.exit(0);
                    }
                }
            }
        });
        
        gamepad.addAction(new GamepadEvent(Button._1, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                Sound.beepSequenceUp();
            }
        });

        gamepad.addAction(new GamepadEvent(Button._3, GamepadEvent.BUTTON_DOWN), new Action() {

            @Override
            public void doAction() throws Exception {
                pattern = (pattern + 1) % 10;
                lejos.hardware.Button.LEDPattern(pattern);
            }
        });

//        gamepad.addAction(new GamepadEvent(Button._3, GamepadEvent.BUTTON_DOWN), new Action() {
//
//            @Override
//            public void doAction() throws Exception {
//                lejos.hardware.Button.LEDPattern(0);
//            }
//        });


        gamepad.setPollTimeInMs(10);
        gamepad.startPolling();
        while (true) {
            Delay.msDelay(5000);
        }
    }
}
