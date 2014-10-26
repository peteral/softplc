softplc
=======

Simulation of a Simatic PLC for integration testing of SCADA systems.

Target functionality:
- multiple configurable CPUs per PLC
- each CPU executes a JavaScript program
- serves PUT/GET protocol over RFC1006 ISO on TCP
- network communication monitoring
- manual access to memory via "variable tables"
- save / load memory snapshots
- GUI

Supported PUT / GET commands:
- connect
- read bytes 
- write bytes 
- set bit
 
GUI features:
- display actual status
- start / stop
- monitor communication
- manage variable tables
- manage memory snapshots

Licence: http://opensource.org/licenses/MIT

Special thanks to:
- https://github.com/ 
- http://sourceforge.net/projects/libnodave/
- https://www.wireshark.org/ 
- http://astah.net/editions/community
