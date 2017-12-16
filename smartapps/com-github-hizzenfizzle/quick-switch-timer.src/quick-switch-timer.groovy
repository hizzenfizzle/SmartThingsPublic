/**
 *  Quick Switch Timer
 *
 *  Copyright 2017 SCOTT HOSFELD github.com/hizzenfizzle
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
definition(
    name: "Quick Switch Timer",
    namespace: "com.github.hizzenfizzle",
    author: "SCOTT HOSFELD",
    description: "Turns on a switch/light for a specific amount of time.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("What to Turn On") {
		paragraph: "Choose what to turn on" 
        input "switches", "capability.switch", required:true, title:"Switches?", multiple: true
	}
    section("How long"){
    	paragraph: "How long should the item(s) remain on?"
        input "minutes", "number", required: true, title: "Minutes?"
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(app, appTouchHandler)
}

def appTouchHandler(evt) {
    log.debug "app event ${evt.name}:${evt.value} received"
    
    switchesOn()
    turnOffLater()
}

def switchesOn(){
	log.debug "handler called to turn switches on"
	switches.each{
    	it.on()
        log.debug "ON command sent to [$it]"
    }
}

def switchesOff(){
	log.debug "handler called to turn off switches"
	switches.each {
    	it.off()
        log.debug "OFF command sent to [$it]"
    }
}

    
def turnOffLater(){
	def runSeconds = minutes * 60
    runIn(runSeconds, switchesOff)
    log.debug "runIn called for $runSeconds seconds"
}
