softplc
=======

Simulation of a Simatic PLC for integration testing of SCADA systems.

Target functionality:
- multiple configurable CPUs per PLC
- each CPU executes a JavaScript program
- serves PUT/GET protocol over RFC1006 ISO on TCP
-- connect
-- read bytes
-- write bytes
-- set bit
- network communication monitoring
- manual access to memory via "variable tables"
- save / load memory snapshots
- GUI
-- display actual status
-- start / stop
-- monitor communication
-- manage variable tables
-- manage memory snapshots
