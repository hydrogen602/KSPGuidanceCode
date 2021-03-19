from typing import List, Tuple
import matplotlib.pyplot as plt
import json

data: List[Tuple[int, float, float]]

with open('flight.json') as f:
    data = json.load(f)

time = []
altitude = []
vertical_vel = []
for t, a, v in data:
    time.append(t/1000)
    altitude.append(a)
    vertical_vel.append(v)

plt.plot(time, altitude)
plt.title('Altitude vs Time')
plt.show()

plt.plot(time, vertical_vel)
plt.title('Vertical Velocity vs Time')
plt.show()