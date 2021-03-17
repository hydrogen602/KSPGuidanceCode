import krpc
from vehicle import HopperMK1

conn = krpc.connect(name='Sub-orbital flight')
print('connected')
vessel = conn.space_center.active_vessel

craft = HopperMK1(vessel, conn)

while True:
    craft.update()