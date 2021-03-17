
from enum import Enum
import time
from typing import Any, Callable


class Vehicle:

    def __init__(self, mass: float, engineThrust: float, numOfEngines: int = 1) -> None:
        self.mass: float = mass
        self.engineThrust: float = engineThrust
        self.numOfEngines: float = numOfEngines
        self.thrust: float = engineThrust * numOfEngines
        # F = ma
        # F/m = a
        # -g + F/m
        self.accelerationWithEngines: float = -9.8 + self.thrust / self.mass

    def suicideBurnHeight(self, vel: float) -> float:
        return 3/2 * self.mass * vel * vel / (self.thrust - self.mass * -9.8)


class Phase(Enum):
    PRE_LAUNCH = 0
    ASCENT = 1
    DESCENT = 2
    SUICIDE_BURN = 3
    LANDED = 4


class HopperMK1(Vehicle):

    def __init__(self, vessel: Any, conn: Any) -> None:
        super().__init__(3750, 15_172, 4)

        self.surface_flight_info = vessel.flight(vessel.orbit.body.reference_frame)
        self.flight_info = vessel.flight()
        self.vessel = vessel

        self.__altitude: Callable[[], float] = conn.add_stream(getattr, self.flight_info, 'surface_altitude')
        self.__verticalVel: Callable[[], float] = conn.add_stream(getattr, self.surface_flight_info, 'vertical_speed')

        self.phaseOfFlight: Phase = Phase.PRE_LAUNCH
    
    def update(self):
        altitude = self.__altitude()
        vertical_vel = self.__verticalVel()

        if self.phaseOfFlight == Phase.PRE_LAUNCH:
            # aim SAS up
            self.vessel.auto_pilot.target_pitch_and_heading(90, 90)
            # actiavte SAS
            self.vessel.auto_pilot.engage()
            # self.vessel.auto_pilot.sas = True
            # print(self.vessel.auto_pilot.sas)
            # 100% throttle
            self.vessel.control.throttle = 1
            # nap
            time.sleep(1)

            print('Launch!')
            self.vessel.control.activate_next_stage()
            self.phaseOfFlight = Phase.ASCENT
        
        elif self.phaseOfFlight == Phase.ASCENT:

            if altitude > 50:
                print('Altitude Reached')
                self.vessel.control.throttle = 0
                self.phaseOfFlight = Phase.DESCENT
        
        elif self.phaseOfFlight == Phase.DESCENT:
            suicideBurnHeight = self.suicideBurnHeight(vertical_vel) + 15
            #print(suicideBurnHeight, altitude)
            if vertical_vel < 0 and suicideBurnHeight >= altitude:
                self.vessel.control.throttle = 1
                print('Suicide burn time')
                print(suicideBurnHeight, altitude)
                self.phaseOfFlight = Phase.SUICIDE_BURN
        
        elif self.phaseOfFlight == Phase.SUICIDE_BURN:

            if altitude < 3:
                self.vessel.control.throttle = 0
                print('Landed')
                print(f'vel = {vertical_vel}, alt = {altitude}')
                self.phaseOfFlight = Phase.LANDED
        
        elif self.phaseOfFlight == Phase.LANDED:
            raise Exception('Done!')
        
    

    
    


if __name__ == '__main__':
    craft = Vehicle(3750, 15_172, 4)
