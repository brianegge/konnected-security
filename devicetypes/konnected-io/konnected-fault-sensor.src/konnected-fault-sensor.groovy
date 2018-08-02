/**
 *  Konnected Fault Sesnor
 *
 *  Copyright 2017 konnected.io
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
  // Useful for connecting to a boiler, low water cutoff, high level alarm
  definition (name: "Konnected Fault Sensor", namespace: "konnected-io", author: "konnected.io", mnmn: "SmartThings", vid: "generic-contact") {
    capability "Switch"
    capability "Sensor"
  }

  preferences {
    input name: "normalState", type: "enum", title: "Normal State",
	  options: ["Normally Closed", "Normally Open"],
      defaultValue: "Normally Open",
      description: "By default, the alarm state is triggered when the sensor circuit is closed (NO). Select Normally Open (NO) when a closed circuit indicates an alarm."
  }

  tiles {
    multiAttributeTile(name:"main", type: "generic", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState ("off", label: "OK",    icon:"st.thermostat.heat",  backgroundColor:"#ffffff")
        attributeState ("on",  label: "Fault!", icon:"st.thermostat.emergency-heat", backgroundColor:"#e86d13")
      }
    }
    main "main"
    details "main"
  }
}


def isClosed() {
  normalState == "Normally Closed" ? "on" : "off"
}

def isOpen() {
  normalState == "Normally Closed" ? "off" : "on"
}

// Update state sent from parent app
def setStatus(state) {
  def stateValue = state == "1" ? isOpen() : isClosed()
  sendEvent(name: "switch", value: stateValue)
  log.debug "$device.label is $stateValue"
}
