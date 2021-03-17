
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
