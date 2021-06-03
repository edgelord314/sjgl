/*
 * Copyright 2018 Malte Dostal
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.edgelord.saltyengine.example.serialization;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.io.serialization.Serializable;
import de.edgelord.sanjo.SJClass;

public class AdvancedSaver implements Serializable {

    private static final String CAMERA_POSITION_TAG = "camPos";
    private static final String COUNTER_TAG = "counter";
    private static final String REDUNDANT_MESSAGE_TAG = "message";
    private int counter = 0;

    @Override
    public void serialize(final SJClass data) {
        data.addValue(COUNTER_TAG, ++counter);
        data.addValue(REDUNDANT_MESSAGE_TAG, "This is just some random longer string");
        data.addValue(CAMERA_POSITION_TAG, Game.getCamera().getX() + "," + Game.getCamera().getY());
    }

    @Override
    public void deserialize(final SJClass data) {
        counter = data.getValue(COUNTER_TAG).get().intValue();

        System.out.println("This example started " + counter + " times before on this computer!");
        System.out.println(data.getValue(REDUNDANT_MESSAGE_TAG));
    }

    @Override
    public String getDataSetName() {
        return "advancedSaver";
    }
}
