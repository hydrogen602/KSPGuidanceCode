from vehicle import Vehicle
import krpc
import time

craft = Vehicle(3750, 15_172, 4)

conn = krpc.connect(name='Sub-orbital flight')
print('connected')
vessel = conn.space_center.active_vessel
refframe = vessel.orbit.body.reference_frame

# aim SAS up
vessel.auto_pilot.target_pitch_and_heading(90, 90)
# actiavte SAS
vessel.auto_pilot.engage()
# 100% throttle
vessel.control.throttle = 1
# nap
time.sleep(1)

print('Launch!')
vessel.control.activate_next_stage()

mean_altitude = conn.get_call(getattr, vessel.flight(), 'surface_altitude')
expr = conn.krpc.Expression.greater_than(
    conn.krpc.Expression.call(mean_altitude),
    conn.krpc.Expression.constant_double(50))

event = conn.krpc.add_event(expr)
with event.condition:
    event.wait()

print('Altitude Reached')
vessel.control.throttle = 0

surface_flight_info = vessel.flight(vessel.orbit.body.reference_frame)

flight_info = vessel.flight()
altitude = conn.add_stream(getattr, flight_info, 'surface_altitude')
verticalVel = conn.add_stream(getattr, surface_flight_info, 'vertical_speed')
while True:
    h = altitude()
    v = verticalVel()
    print(verticalVel())
    if v < -5:
        vessel.control.throttle = 1
    else:
        vessel.control.throttle = 0

'''
['__class__', '__delattr__', '__dict__', '__dir__', '__doc__', '__eq__',
'__format__', '__ge__', '__getattribute__', '__gt__', '__hash__',
'__init__', '__init_subclass__', '__le__', '__lt__', '__module__',
'__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__',
'__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_add_method',
'_add_property', '_add_static_method', '_class_name', '_client', '_object_id',
'_service_name', 'aerodynamic_force', 'angle_of_attack', 'anti_normal',
'anti_radial', 'atmosphere_density', 'ballistic_coefficient',
'bedrock_altitude', 'center_of_mass', 'direction', 'drag', 'drag_coefficient',
'dynamic_pressure', 'elevation', 'equivalent_air_speed', 'g_force', 'heading',
'horizontal_speed', 'latitude', 'lift', 'lift_coefficient', 'longitude',
'mach', 'mean_altitude', 'normal', 'pitch', 'prograde', 'radial',
'retrograde', 'reynolds_number', 'roll', 'rotation', 'sideslip_angle',
'simulate_aerodynamic_force_at', 'speed', 'speed_of_sound', 'stall_fraction',
'static_air_temperature', 'static_pressure', 'static_pressure_at_msl',
'surface_altitude', 'terminal_velocity', 'thrust_specific_fuel_consumption',
'total_air_temperature', 'true_air_speed', 'velocity', 'vertical_speed']
'''

flight_info = vessel.flight()
print(dir(flight_info))
altitude = conn.add_stream(getattr, flight_info, 'surface_altitude')
while True:
    print(altitude())