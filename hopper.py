import time
import krpc
from vehicle import HopperMK1

conn = krpc.connect(name='Sub-orbital flight')
print('connected')
vessel = conn.space_center.active_vessel
vessel.control.activate_next_stage()

conn.space_center.quicksave()


# [50, 100, 200, 400]
# [400, 700]
for h in [50, 100, 200, 400, 800, 1600, 3200, 6400]:
    conn.space_center.quickload()
    craft = HopperMK1(vessel, conn)
    craft.shutoffHeight = h
    print(f'Aiming for {craft.shutoffHeight}')
    while craft.update():
        pass
    time.sleep(5)
    del craft